package model;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

/*
RandomWordFetcher
    - pulls random five-letter word from random word api
    - formats output into String
    - returns String format of random word

 */


public class RandomWordFetcher {
    private static final String API_URL = "https://random-word-api.herokuapp.com/word?length=5";
    private static final String API_URL2 = "https://random-word-api.vercel.app/api?words=1&length=5";

    public static String fetchRandomWord() throws Exception {
        URI uri = new URI(API_URL2);
        URL url = uri.toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        int status = conn.getResponseCode();
        if (status != 200) {
            throw new RuntimeException("API call failed with HTTP status " + status);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String raw = response.toString();
        String word = raw.replaceAll("[\\[\\]\"]", "");
        return word;
    }
}
