package com.xsoftwareabdullah.jarvis;
import java.io.BufferedReader; 
import java.io.InputStreamReader; 
import java.net.HttpURLConnection; 
import java.net.URL; 
import java.net.URLEncoder;
public class FreeAiService {
    public static String askJarvis(String prompt) throws Exception {
        String searchContext = "";
        if(prompt.toLowerCase().contains("search") || prompt.toLowerCase().contains("who is") || prompt.toLowerCase().contains("news") || prompt.toLowerCase().contains("what is")){
            searchContext = WebSearchHelper.search(prompt);
        }
        String finalPrompt = "You are JARVIS like Iron Man, British, call user Sir. Web Context: " + searchContext + " User: " + prompt;
        String urlStr = "https://text.pollinations.ai/" + URLEncoder.encode(finalPrompt, "UTF-8");
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET"); conn.setConnectTimeout(15000);
        BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder s = new StringBuilder(); String l; while ((l = r.readLine()) != null) s.append(l).append("\n"); r.close();
        return s.toString();
    }
}
