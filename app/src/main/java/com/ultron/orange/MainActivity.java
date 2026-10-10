package com.ultron.orange;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText inputField;
    TextView chatText;
    ImageView reactor;
    Button btnSend, btnMic, btnBg, btnSettings;
    JarvisVoice jarvisVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Reactor Animation - Video jaisa ghumega
        reactor = findViewById(R.id.arcReactor);
        ObjectAnimator anim = ObjectAnimator.ofFloat(reactor, "rotation", 0f, 360f);
        anim.setDuration(3500);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.start();

        inputField = findViewById(R.id.inputField);
        chatText = findViewById(R.id.chatText);
        btnSend = findViewById(R.id.btnSend);
        btnMic = findViewById(R.id.btnMic);
        btnBg = findViewById(R.id.btnBgListening);
        btnSettings = findViewById(R.id.btnSettings);
        jarvisVoice = new JarvisVoice(this);

        btnSend.setOnClickListener(v -> sendToJarvis());
        btnMic.setOnClickListener(v -> startVoiceInput());
        btnBg.setOnClickListener(v -> {
            startService(new Intent(this, JarvisService.class));
            jarvisVoice.speak("Background listening activated");
        });
        btnSettings.setOnClickListener(v -> startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS)));
    }

    void sendToJarvis(){
        String text = inputField.getText().toString().trim();
        if(text.isEmpty()) return;
        chatText.append("\n\nYOU: "+text);
        inputField.setText("");
        chatText.append("\nULTRON: Thinking...");
        // Pulse animation jab soche
        reactor.animate().scaleX(1.2f).scaleY(1.2f).setDuration(300).withEndAction(() -> reactor.animate().scaleX(1f).scaleY(1f).setDuration(300).start()).start();

        new Thread(() -> {
            try{
                String reply = FreeAiService.askJarvis(text);
                runOnUiThread(() -> {
                    chatText.setText(chatText.getText().toString().replace("ULTRON: Thinking...","ULTRON: "+reply));
                    jarvisVoice.speak(reply);
                });
            }catch(Exception e){
                runOnUiThread(() -> chatText.setText(chatText.getText().toString().replace("ULTRON: Thinking...","ULTRON: Offline Sir")));
            }
        }).start();
    }
    void startVoiceInput(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(i, 100);
    }
    @Override protected void onActivityResult(int r,int rc,Intent d){super.onActivityResult(r,rc,d);if(r==100&&rc==RESULT_OK&&d!=null){ArrayList<String> res=d.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);inputField.setText(res.get(0));sendToJarvis();}}
    @Override protected void onDestroy(){jarvisVoice.shutdown();super.onDestroy();}
                       }
