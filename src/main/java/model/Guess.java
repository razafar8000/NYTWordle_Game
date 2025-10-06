package model;
import java.util.Arrays;

// Guess.java
// Represents a single user guess, comparing it to the secret word and storing
// letter-by-letter feedback (correct, present, absent) for display in the view.

public class Guess {
    private final String guess;
    private final LetterFeedback[] letterEval = new LetterFeedback[5];

    public Guess(String guess, String secret) {
        this.guess = guess;
        checkWord(secret);
    }

    //2 pass approach to deal with duplicates
    public void checkWord(String secretWord){
        int[] letterCounts = new int[26];
        for (int i = 0; i < secretWord.length(); i++) { //count instances of each letter in secret word
            letterCounts[secretWord.charAt(i) - 'a']++;
        }

        //first pass only deals with correctness
        for (int i=0; i<5; i++){
            if (guess.charAt(i) == secretWord.charAt(i)){
                letterEval[i] = LetterFeedback.CORRECT;
                letterCounts[guess.charAt(i) - 'a']--;
            }
        }
        //second pass deals with present and absent
        for(int i = 0; i < 5; i++) {
            if (letterEval[i] == LetterFeedback.CORRECT){
            }
            else if (secretWord.contains(String.valueOf(guess.charAt(i))) && letterCounts[guess.charAt(i) - 'a'] > 0) {
                letterEval[i] = LetterFeedback.PRESENT;
                letterCounts[guess.charAt(i) - 'a']--;
            } else{
                letterEval[i] = LetterFeedback.ABSENT;
            }
        }
    }

    //getters

    public String getGuess() {
        return guess;
    }
    //returns the validity of letter according to given position
    public LetterFeedback getLetterEval(int pos) {
        return letterEval[pos];
    }

    @Override
    public String toString() {
        return "Guess string: \""+this.guess + "\" Feedback: "+ Arrays.toString(letterEval);
    }
}
