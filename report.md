# Project Report

---

## Design Decisions

### Architecture
This project was designed using the **Model-View-Controller (MVC)** architecture to ensure a clean separation of concerns and improve maintainability.

- **Model (`WordleModel`, `Guess`, `LetterFeedback`)**  
  Handles all game logic and state management.  
  The model tracks the secret word, guesses, feedback, and win/loss conditions. It does not contain any UI code.  
  The `Guess` class encapsulates a single attempt and uses the `LetterFeedback` enum to represent letter correctness.  

- **View (`SwingWordle`)**  
  Responsible for displaying the game board, keyboard, and status updates.  
  It observes the model’s state through the controller and updates the user interface accordingly.  
  Swing was chosen because it is lightweight, easy to configure, and ideal for a project with simple grid-based rendering.  

- **Controller (`WordleController`)**  
  Acts as the intermediary between user input and the model.  
  It processes keyboard actions, submits guesses, resets the game, and manages save/load operations through the model.

This structure allows each layer to function independently and makes testing the model and controller simple without any UI dependencies.

---

### Data Structures
- **Array of Guesses (`Guess[] guessesMade`)**  
  Used to store up to six attempts per game. Arrays are efficient and provide constant-time access when rendering guesses.
  
- **Enum (`LetterFeedback`)**  
  Represents the three feedback types: `CORRECT`, `PRESENT`, and `ABSENT`. Enums make the code more readable and type-safe.

- **String Variables**  
  The `secretWord` and `guess` strings were chosen for their simplicity and compatibility with string comparison and indexing.

- **List in `SaveLoad`**  
  A `List<String>` stores previously made guesses for persistence when saving to and loading from JSON.

These structures were selected to keep the model simple, efficient, and easy to serialize for saving game progress.

---

### Algorithms
The most important algorithm implemented is the **two-pass letter feedback algorithm** inside `Guess.java`.

1. **First Pass:**  
   Each letter in the guess is compared to the corresponding letter in the secret word.  
   - If the position matches, it is marked as **CORRECT**.  
   - The frequency of that letter is reduced from the letter count array.

2. **Second Pass:**  
   The remaining letters are checked:  
   - If the letter exists in the secret word and is still available in the count array, it is marked **PRESENT**.  
   - Otherwise, it is marked **ABSENT**.

This two-pass system properly handles duplicate letters (e.g., words like “APPLE”) and ensures Wordle’s standard feedback behavior.  
The algorithm runs in **O(n)** time per guess (where n = 5 letters), which is optimal for this application.

---

## Challenges Faced

1. **Challenge 1: Handling Duplicate Letters**  
   Initially, repeated letters (like in “APPLE”) were being over-counted, resulting in incorrect feedback.  
   - **Solution:** Implemented a two-pass checking algorithm using a frequency array to correctly handle duplicates and mark letters only once.

2. **Challenge 2: Maintaining Game State Between Sessions**  
   Saving and restoring the game proved tricky at first since the model had to serialize both score and past guesses.  
   - **Solution:** Created a `SaveLoad` class using the **Gson** library for clean JSON serialization. The model now calls `SaveLoad.saveState()` and `SaveLoad.loadState()` automatically, ensuring smooth persistence.

---

## What Was Learned
- Reinforced understanding of **Object-Oriented Programming principles**, especially encapsulation and abstraction.  
- Learned to design and implement a full **MVC architecture** in Java with clear separation of logic and presentation.  
- Gained experience with **persistent data storage** using JSON serialization.  
- Practiced **test-driven development (TDD)** with JUnit to validate model and controller behavior.  
- Improved skills in **UI design with Swing**, keyboard event handling, and layout management.

---

## If We Had More Time
- **Add Keyboard Feedback:**  
  Implement color updates on the on-screen keyboard to reflect letter correctness.
- **Expand Dictionary:**  
  Use a larger dataset of valid English words for validation.  
- **Add Hard Mode:**  
  Enforce revealed letters to be reused in subsequent guesses.
- **Improve Animations:**  
  Add smoother transitions and animations when letters flip or change color.
- **Refactor Save System:**  
  Enhance save files with timestamps and multiple user profiles.

