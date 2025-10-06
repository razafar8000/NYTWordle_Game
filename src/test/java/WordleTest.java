import org.junit.jupiter.api.Test;
import model.*;
import control.WordleController;
import static org.junit.jupiter.api.Assertions.*;

// WordleTest.java
// JUnit 5 test suite verifying the functionality of the Wordle model and controller,
// including input handling, persistence, feedback correctness, and win/loss behavior.


public class WordleTest {

    // Already covered: Win/Loss/Guess count...

    @Test
    void bufferHandling(){
        WordleModel model = new WordleModel("apple");
        WordleController controller = new WordleController(model);

        controller.onKeyPress("A");
        controller.onKeyPress("P");
        controller.onKeyPress("P");
        controller.onKeyPress("L");
        controller.onKeyPress("E");
        assertEquals("APPLE", controller.getBuffer());

        // Enters and submits the guess
        controller.onKeyPress("ENTER");
        assertEquals("", controller.getBuffer());
        assertEquals(1, controller.getGuessCount());
    }

    @Test
    void backspaceHandling(){
        WordleModel model = new WordleModel("apple");
        WordleController controller = new WordleController(model);

        controller.onKeyPress("A");
        controller.onKeyPress("B");
        controller.onKeyPress("BACKSPACE");

        assertEquals("A", controller.getBuffer());
    }

    @Test
    void invalidGuessLengthNotAccepted(){
        WordleModel model = new WordleModel("apple");
        WordleController controller = new WordleController(model);

        controller.onGuess("hi"); // not 5 letters
        assertEquals(0, controller.getGuessCount());
    }

    @Test
    void secretWordPersistence() throws Exception {
        WordleModel model = new WordleModel("apple");
        WordleController controller = new WordleController(model);

        controller.onGuess("apple");
        assertTrue(controller.isWon());

        //Simulates and saves or loads
        controller.saveGame();

        WordleModel newModel = new WordleModel(); // default
        WordleController newController = new WordleController(newModel);
        newController.loadGame();

        assertEquals(model.getSecretWord(), newController.getSecretWord());
        assertEquals(model.getGameScore(), newController.getGameScore());
    }

    @Test
    void refreshGameResetsButKeepsScore() throws Exception {
        WordleModel model = new WordleModel("apple");
        WordleController controller = new WordleController(model);

        controller.onGuess("apple");
        assertTrue(controller.isWon());

        controller.refreshGame();

        // Score increments after win
        assertEquals(1, controller.getGameScore());

        // Guesses should reset
        assertEquals(0, controller.getGuessCount());
        assertFalse(controller.isWon());
    }

    @Test
    void guessEvaluationDifferentWord(){
        WordleModel model = new WordleModel("table");
        WordleController controller = new WordleController(model);

        controller.onGuess("candy");
        Guess guess = controller.getLastGuess();

        assertEquals(5, guess.getGuess().length());
        for (int i = 0; i < 5; i++) {
            assertNotNull(guess.getLetterEval(i));
        }
    }
}

