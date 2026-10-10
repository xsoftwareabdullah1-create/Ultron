package com.xsoftwareabdullah.jarvis;
import java.io.BufferedReader; 
import java.io.InputStreamReader; 
import java.net.HttpURLConnection; 
import java.net.URL; 
import java.net.URLEncoder;
public class WebSearchHelper {
    public static String search(String query) {
        try {
            String q = URLEncoder.encode(query, "UTF-8");
            URL url = new URL("https://api.duckduckgo.com/?q=" + q + "&format=json&pretty=1");
            HttpURLConnection c = (HttpURLConnection) url.openConnection(); c.setRequestMethod("GET");
            BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder s = new StringBuilder(); String l; while ((l = r.readLine()) != null) s.append(l);
            return s.toString().substring(0, Math.min(800, s.length()));
        } catch (Exception e) { return ""; }
    }
}
