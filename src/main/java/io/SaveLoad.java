package io;

import com.google.gson.Gson;
import model.WordleModel;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// SaveLoad.java
// Manages saving and loading of game data using JSON serialization through Gson.
// Stores the secret word, score, and past guesses to maintain persistence between sessions.


public class SaveLoad {

    public static void saveState(WordleModel model) {
        Gson gson = new Gson();
        List<String> guesses = new ArrayList<String>(); // use explicit type if Java 6

        if (model.getGuessCount() > 0) {
            for (int i = 0; i < model.getGuessCount(); i++) {
                guesses.add(model.getGuesses()[i].getGuess());
            }
        }

        SaveState state = new SaveState(model.getGameScore(), model.getSecretWord(), guesses);

        try (FileWriter fw = new FileWriter("wordle_save.json")) {
            gson.toJson(state, fw);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadState(WordleModel model) {
        Gson gson = new Gson();
        File file = new File("wordle_save.json");

        if (file.exists()) {
            try (FileReader fr = new FileReader(file)) {
                SaveState state = gson.fromJson(fr, SaveState.class);

                model.setSecretWord(state.secretWord);
                model.setGameScore(state.gameScore);

                if (state.guessesMade != null) {
                    for (String s : state.guessesMade) {
                        model.makeGuess(s);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
