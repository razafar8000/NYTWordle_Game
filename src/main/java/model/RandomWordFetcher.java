package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * RandomWordFetcher
 * -----------------
 * Fetches a random 5-letter English word from the Random Word API.
 * Used only for secret word generation; does not validate guesses.
 */
public class RandomWordFetcher {

    private static final String API_URL = "https://random-word-api.vercel.app/api?words=1&length=5";

    /** Returns a random 5-letter word fetched from the API. */
    public static String fetchRandomWord() throws Exception {
        URI uri = new URI(API_URL);
        URL url = uri.toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(4000);
        conn.setReadTimeout(4000);

        int status = conn.getResponseCode();
        if (status != 200) {
            throw new RuntimeException("API call failed with HTTP status " + status);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            // remove ["",] characters
            String word = response.toString().replaceAll("[\\[\\]\"]", "").trim();
            return word.toLowerCase();
        }
    }

    /** Stub for validation: always true (API only). */
    public static boolean isValidWord(String word) {
        return word != null && word.length() == 5;
    }
}
