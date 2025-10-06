package io;

import java.util.List;

// SaveState.java
// Data transfer object used for serializing and deserializing the game state,
// containing the current score, secret word, and list of previous guesses.

public class SaveState {
    int gameScore;
    String secretWord;
    List<String> guessesMade;

    public SaveState(int gameScore, String secretWord, List<String> guessesMade) {
        this.gameScore = gameScore;
        this.secretWord = secretWord;
        this.guessesMade = guessesMade;
    }
}
