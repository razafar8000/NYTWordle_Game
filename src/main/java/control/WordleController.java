package control;

import model.*;
import control.GuessValidator;

// WordleController.java
// Serves as the controller in the MVC architecture, processing user input,
// passing guesses to the model, managing game state transitions, and handling save/load actions.

public class WordleController {
    private final WordleModel model;
    private String buffer = "";
    private boolean guessState = false;

    public WordleController(WordleModel model) {
        if (model == null) {
            throw new IllegalStateException("Model not initialized");
        }
        this.model = model;
    }

    public void onGuess(String word) {
        //when a guess is made guess count and pass word gets incremented to model
        if (buffer.length() == 5) {
            if (GuessValidator.isValid(buffer)) {
                model.makeGuess(buffer);
            } else {
                // Trigger popup for invalid English word
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        buffer.toUpperCase() + " is not a valid English word!",
                        "Invalid Guess",
                        javax.swing.JOptionPane.WARNING_MESSAGE
                );
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Please enter a 5-letter word.",
                    "Invalid Input",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
        }

    }

    public void onKeyPress(String key) {
        //processess user's input buffer and make guesses
        if (buffer.length() <= 5) {
            if (key.equals("ENTER") && buffer.length() == 5) {
                onGuess(buffer);
                guessState = true;
                buffer = "";
            } else if (key.equals("BACKSPACE") && !buffer.isEmpty()) {
                buffer = buffer.substring(0, buffer.length() - 1);
            } else if (key.length() == 1 && Character.isLetter(key.charAt(0)) && buffer.length() < 5) {
                buffer = buffer + key.toUpperCase();
            }
        }
    }

    public void refreshGame() throws Exception { // reset game state for continuous play
        if (model.isWon()) {
            model.incrementGameScore();
        }
        model.resetGame();
    }

    //these are getters and win and loss checkers for UI to handle refreshing/game flow
    public boolean isWon() { return model.isWon(); }
    public boolean isLost() { return model.isLost(); }
    public int getGameScore() { return model.getGameScore(); }
    public int getGuessCount() { return model.getGuessCount(); }
    public String getBuffer() { return buffer; }
    public boolean getGuessState() { return guessState; }
    public void resetGuessState() { guessState = false; }

    public Guess[] getGuesses() { return model.getGuesses(); }
    public Guess getLastGuess() { return model.getLastGuess(); }
    public String getSecretWord() { return model.getSecretWord(); }

    //saves and loads
    public void saveGame() { model.saveGame(); }
    public void loadGame() { model.loadGame(); }

    // For testing: bypasses validation
    public void forceGuess(String word) { model.makeGuess(word);}
}

