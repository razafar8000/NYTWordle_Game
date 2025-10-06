package control;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

/**
 * GuessValidator
 * Validates user-entered words using the Dictionary API.
 * If offline or the API fails, it quietly rejects the guess.
 */

public class GuessValidator {
    private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    public static boolean isValid(String word) {
        if (word == null || word.length() != 5) return false;

        try {
            URI uri = new URI(API_URL + word.toLowerCase());
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            int code = conn.getResponseCode();
            return code == 200;
        } catch (Exception e) {
            System.err.println("⚠️ GuessValidator API error: " + e.getMessage());
            return false;
        }
    }
}

