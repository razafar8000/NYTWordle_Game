# Wordle MVC Project

---

## Team Members
- **Rayan Zafar**

---
## Gameplay Demo Video
https://youtu.be/C9VgAE7sedo?si=NPfu5e56vfI5lHbr

---

## How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/NYTWordle_Game.git
   ```
2. Open the project in IntelliJ IDEA (or Eclipse).
3. Ensure the Java SDK (version 17 or above) is configured.
3. Run one of the following files:
  - src/view/SwingWordle.java
  - Main.java
  - The game window will appear — start guessing 5-letter words!

## Features Implemented

### Random Secret Word Selection
A random 5-letter word is chosen from a dictionary at the start of each game.

### 6-Turn Gameplay Loop
Players have up to six attempts to guess the word correctly.

### Feedback System
- **Green:** Letter is in the correct position.  
- **Yellow:** Letter exists in the word but in the wrong spot.  
- **Gray:** Letter is not in the word.

### On-Screen Keyboard
Clickable buttons and physical keyboard input are both supported for making guesses.

### Dictionary Validation
Only valid English 5-letter words are accepted.

### Win/Loss Conditions
Displays a popup message when the game ends, showing the correct word and a reset option.

### Persistence (Save/Load)
The game automatically saves the current state (`wordle_save.json`) and restores it on startup.

### Score Tracking
Win count increases across sessions and is displayed on the status bar.

---
## Extra Credit

### JUnit Testing
Comprehensive suite of model and controller tests using **JUnit 5**.

### Accessible UI
Uses color, readable text, and large fonts for clarity and accessibility.

### Hard Mode 
Easily extensible to enforce the rule that revealed letters must be reused in later guesses.

---

## Controls
- Type using keyboard or click the on-screen keys.  
- **ENTER:** Submit a 5-letter guess.  
- **BACKSPACE:** Delete the last letter.  
- **R:** Reset the game (when popup appears).  
- **S:** Save game manually (auto-save also enabled).  
- **ESC / Close Window:** Automatically saves and exits the game.

---

## Known Issues
- Dictionary validation is basic and could be improved with a larger word list.  
- On-screen keyboard colors do not yet reflect letter feedback.

---

## External Libraries
- **Gson 2.10.1** — JSON serialization for save/load persistence.  
- **JUnit 5.9.0** — Unit testing framework.

