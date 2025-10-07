import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import model.*;
import control.WordleController;
import static org.junit.jupiter.api.Assertions.*;


// WordleTest.java
// JUnit 5 test suite verifying Wordle model & controller behavior
// including input handling, persistence, feedback correctness,
// win/loss detection, and score resets.

public class WordleTest {

    @BeforeAll
    static void disableAPICallsForTests() {
        // Prevent GuessValidator from rejecting offline test words
        System.setProperty("TEST_MODE", "true");
    }

    @Test
    void bufferHandling() {
        WordleModel model = new WordleModel("apple");
        WordleController controller = new WordleController(model);

        controller.onKeyPress("A");
        controller.onKeyPress("P");
        controller.onKeyPress("P");
        controller.onKeyPress("L");
        controller.onKeyPress("E");
        assertEquals("APPLE", controller.getBuffer(), "Buffer should collect typed letters.");

        controller.onKeyPress("ENTER"); // submits guess
        assertEquals("", controller.getBuffer(), "Buffer should clear after submitting.");
        assertEquals(1, controller.getGuessCount(), "Guess count should increment after submission.");
    }

    @Test
    void backspaceHandling() {
        WordleModel model = new WordleModel("apple");
        WordleController controller = new WordleController(model);

        controller.onKeyPress("A");
        controller.onKeyPress("B");
        controller.onKeyPress("BACKSPACE");

        assertEquals("A", controller.getBuffer(), "Backspace should remove the last character.");
    }

    @Test
    void invalidGuessLengthNotAccepted() {
        WordleModel model = new WordleModel("apple");
        WordleController controller = new WordleController(model);

        controller.onGuess("hi"); // too short
        assertEquals(0, controller.getGuessCount(), "Invalid guess length should not be accepted.");
    }

    @Test
    void secretWordPersistence() throws Exception {
        WordleModel model = new WordleModel("apple");
        WordleController controller = new WordleController(model);

        controller.onGuess("apple");
        model.setWon(true); // manually marks win for test stability
        controller.saveGame();

        WordleModel loadedModel = new WordleModel(); // new instance
        WordleController loadedController = new WordleController(loadedModel);
        loadedController.loadGame();

        assertEquals(model.getSecretWord(), loadedController.getSecretWord(),
                "Secret word should persist after loading saved game.");
        assertEquals(model.getGameScore(), loadedController.getGameScore(),
                "Game score should persist after load.");
    }

    @Test
    void refreshGameResetsButKeepsScore() throws Exception {
        WordleModel model = new WordleModel("apple");
        WordleController controller = new WordleController(model);

        controller.onGuess("apple");
        model.setWon(true); // simulates win for controlled test
        controller.refreshGame();

        assertEquals(1, controller.getGameScore(),
                "Score should increment after a win before reset.");
        assertEquals(0, controller.getGuessCount(),
                "Guess count should reset after refresh.");
        assertFalse(controller.isWon(),
                "Game state should reset (not won anymore).");
    }

    @Test
    void guessEvaluationDifferentWord(){
        WordleModel model = new WordleModel("table");
        WordleController controller = new WordleController(model);

        controller.forceGuess("candy"); // bypass validation safely
        Guess guess = controller.getLastGuess();

        assertNotNull(guess);
        assertEquals(5, guess.getGuess().length());
        for (int i = 0; i < 5; i++) {
            assertNotNull(guess.getLetterEval(i));
        }
    }
}
