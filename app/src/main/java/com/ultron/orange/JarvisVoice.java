package com.ultron.orange;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
public class JarvisVoice {
    private TextToSpeech tts;
    public JarvisVoice(Context c){
        tts = new TextToSpeech(c, status -> {
            if(status==TextToSpeech.SUCCESS){
                tts.setLanguage(Locale.UK);
                tts.setPitch(0.78f); // Video jaisi bhaari awaz
                tts.setSpeechRate(0.82f); // thoda slow robot jaisa
            }
        });
    }
    public void speak(String t){ tts.speak(t, TextToSpeech.QUEUE_FLUSH, null, null); }
    public void shutdown(){ if(tts!=null) tts.shutdown(); }
}
