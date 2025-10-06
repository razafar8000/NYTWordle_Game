package model;

import io.SaveLoad;

// WordleModel.java
// Handles the core game logic and state for Wordle, including secret word generation,
// guess tracking, win/loss detection, and integration with save/load persistence.

public class WordleModel {
    private int gameScore;
    private int guessCount;
    private String secretWord;
    private Guess[] guessesMade = new Guess[6];

    //CTORS
    public WordleModel() throws Exception { //main constructor
        this.guessCount = 0;
        //generate word
        this.secretWord = RandomWordFetcher.fetchRandomWord().toLowerCase();

    }
    public WordleModel(String secretWord){ //used for debugging purposes
        this.guessCount = 0;
        this.secretWord = secretWord;
    }

    public void makeGuess(String word) {
        if (this.guessCount < 6) {
            if (!RandomWordFetcher.isValidWord(word)) {
                System.out.println("Invalid word. Please try again.");
                return;
            }
            Guess userGuess = new Guess(word.toLowerCase(), this.secretWord);
            this.guessesMade[guessCount] = userGuess;
            guessCount++;
        }
    }

    public void resetGame() throws Exception { //for ui to enable continuous play
        this.guessCount = 0;
        this.secretWord = RandomWordFetcher.fetchRandomWord();
        for (int i = 0; i < 6; i++) {
            this.guessesMade[i] = null;
        }
    }

    //responsible for saving and loading
    public void saveGame(){ //saves the current state of game to JSON
        SaveLoad.saveState(this);
    }
    public void loadGame(){ //loads the last saved state from JSON
        SaveLoad.loadState(this);
    }

    //these are the getters and setters and win/loss checkers
    public Boolean isWon(){
        if (this.guessCount==0){
            return false;
        }else {
            return (this.guessesMade[this.guessCount - 1].getGuess().equals(secretWord) && this.guessCount <= 6);
        }
    }
    public Boolean isLost(){
        if (this.guessCount==0){
            return false;
        }else {
            return (this.guessCount == 6 && !(this.secretWord.equals(this.guessesMade[guessCount - 1].getGuess())));
        }
    }
    public String getSecretWord(){
        return this.secretWord;
    }
    public Guess getLastGuess(){
        if (this.guessCount > 0) {
            return this.guessesMade[guessCount - 1];
        }else{
            return null;
        }
    }
    public Guess[] getGuesses(){
        return this.guessesMade;
    }
    public int getGameScore(){
        return this.gameScore;
    }
    public void setGameScore(int score){
        this.gameScore = score;
    }
    public void setSecretWord(String word){
        this.secretWord = word;
    }
    public void incrementGameScore(){
        this.gameScore++;
    }
    public int getGuessCount(){
        return this.guessCount;
    }
}
