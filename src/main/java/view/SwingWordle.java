package view;

import model.*;
import control.WordleController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// SwingWordle.java
// Implements the graphical user interface for the Wordle game using Java Swing.
// Displays the grid, on-screen keyboard, and status bar while delegating logic to the controller.


public class SwingWordle extends JFrame {
    private WordleModel model;
    private WordleController controller;
    private int refreshCount = 0;

    private JLabel[][] boardCells = new JLabel[6][5];
    private Map<String, JButton> keyboardButtons = new HashMap<>();
    private Map<String, JButton> usedButtons = new HashMap<>();

    JPanel wordGrid = new JPanel(new GridLayout(6, 5, 6, 6)); // slightly larger spacing
    JLabel status = new JLabel("Guess the Word!", SwingConstants.CENTER);
    JPanel keyboardPanel = new JPanel();

    public SwingWordle() {
        setTitle("Wordle");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.saveGame();
                System.exit(0);
            }
        });

        setLayout(new BorderLayout());

        try {
            model = new WordleModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        controller = new WordleController(model);
        controller.loadGame();

        JPanel top = new JPanel(new BorderLayout());
        status.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        status.setFont(new Font("Arial", Font.BOLD, 22));
        top.add(status, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        add(wordGrid, BorderLayout.CENTER);
        buildGrid();
        buildKeyBoard();
        add(keyboardPanel, BorderLayout.SOUTH);
        setupKeyBindings();

        refresh();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void onKeyPress(String key) {
        controller.onKeyPress(key);
        refresh();
    }

    private void refresh() {
        if (refreshCount == 0) loadScreenGuesses();
        updateScreenGuesses();
        updateScreenBuffer();
        status.setText("Score: " + controller.getGameScore() + " | Guesses: " + controller.getGuessCount());

        if (controller.isWon()) showPopup(true);
        else if (controller.isLost()) showPopup(false);

        for (JButton btn : usedButtons.values()) {
            btn.repaint();
        }

        refreshCount++;
    }

    private void showPopup(Boolean won) {
        String msg = won ? "You won! The word was " + controller.getSecretWord()
                : "You lost! The word was " + controller.getSecretWord();
        int choice = JOptionPane.showOptionDialog(
                this, msg, "Game Over",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new String[]{"Reset"}, "Reset");
        if (choice == 0) resetGame();
    }

    private void resetGame() {
        for (int i = 0; i < controller.getGuessCount(); i++) {
            for (int j = 0; j < 5; j++) {
                boardCells[i][j].setText("");
                boardCells[i][j].setBackground(Color.WHITE);
            }
        }
        try { controller.refreshGame(); } catch (Exception e) { e.printStackTrace(); }
        for (JButton b : usedButtons.values()) b.setBackground(new Color(230, 230, 230));
        usedButtons.clear();
        refresh();
    }

    private void loadScreenGuesses() {
        if (controller.getGuessCount() > 0) {
            Guess[] guessesMade = Arrays.copyOf(controller.getGuesses(), controller.getGuessCount());
            for (int i = 0; i < guessesMade.length; i++) {
                Guess g = guessesMade[i];
                for (int j = 0; j < 5; j++) {
                    char ch = g.getGuess().charAt(j);
                    LetterFeedback eval = g.getLetterEval(j);
                    boardCells[i][j].setText(String.valueOf(ch).toUpperCase());
                    switch (eval) {
                        case CORRECT: boardCells[i][j].setBackground(new Color(0, 200, 0)); break;
                        case PRESENT: boardCells[i][j].setBackground(new Color(255, 200, 0)); break;
                        case ABSENT:  boardCells[i][j].setBackground(new Color(180, 180, 180)); break;
                    }
                }
            }
        }
    }

    private void updateScreenGuesses() {
        if (controller.getGuessState()) {
            int row = controller.getGuessCount() - 1;
            Guess g = controller.getLastGuess();
            if (g != null) {
                for (int i = 0; i < 5; i++) {
                    char ch = g.getGuess().charAt(i);
                    LetterFeedback eval = g.getLetterEval(i);

                    // Updates grid cell text
                    boardCells[row][i].setText(String.valueOf(ch).toUpperCase());
                    boardCells[row][i].setOpaque(true);

                    // Applies official Wordle-style colors
                    switch (eval) {
                        case CORRECT -> boardCells[row][i].setBackground(new Color(106, 170, 100));   // green
                        case PRESENT -> boardCells[row][i].setBackground(new Color(201, 180, 88));   // yellow
                        case ABSENT -> boardCells[row][i].setBackground(new Color(120, 124, 126));   // gray
                    }

                    // --- KEYBOARD COLOR LOGIC ---
                    JButton key = keyboardButtons.get(String.valueOf(ch).toUpperCase());
                    if (key != null) {
                        Color newColor = switch (eval) {
                            case CORRECT -> new Color(106, 170, 100);   // green
                            case PRESENT -> new Color(201, 180, 88);    // yellow
                            case ABSENT -> new Color(120, 124, 126);    // gray
                        };

                        // Does not downgrade colors (keeps green over yellow/gray)
                        Color current = key.getBackground();
                        if (!current.equals(new Color(106, 170, 100))) {
                            if (eval == LetterFeedback.CORRECT ||
                                    (eval == LetterFeedback.PRESENT && !current.equals(new Color(201, 180, 88))) ||
                                    (eval == LetterFeedback.ABSENT && current.equals(new Color(211, 214, 218)))) {

                                key.setBackground(newColor);
                                // Automatically adjust text color for contrast
                                if (eval == LetterFeedback.CORRECT || eval == LetterFeedback.PRESENT) {
                                    key.setForeground(Color.WHITE);
                                } else {
                                    key.setForeground(Color.BLACK);
                                }
                                key.setOpaque(true);
                                key.repaint();

                            }
                        }

                        usedButtons.put(String.valueOf(ch).toUpperCase(), key);
                    }
                }
            }
            controller.resetGuessState();
        }
    }



    private void updateScreenBuffer() {
        int row = controller.getGuessCount();
        if (row < 6) {
            String buf = controller.getBuffer();
            for (int i = 0; i < 5; i++) {
                boardCells[row][i].setText(i < buf.length() ? String.valueOf(buf.charAt(i)) : "");
            }
        }
    }

    private void buildKeyBoard() {
        keyboardPanel.setLayout(new BoxLayout(keyboardPanel, BoxLayout.Y_AXIS));
        addKeyboardRow(new String[]{"Q","W","E","R","T","Y","U","I","O","P"});
        addKeyboardRow(new String[]{"A","S","D","F","G","H","J","K","L"});
        addKeyboardRow(new String[]{"ENTER","Z","X","C","V","B","N","M","BACKSPACE"});
    }

    private void addKeyboardRow(String[] keys) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        for (String key : keys) {
            JButton btn = new JButton(key);
            btn.setBackground(new Color(211, 214, 218)); // official Wordle light-gray
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setFocusable(false);
            btn.addActionListener(e -> onKeyPress(key));
            keyboardButtons.put(key, btn);

            if ("ENTER".equals(key) || "BACKSPACE".equals(key)) btn.setPreferredSize(new Dimension(80, 40));
            row.add(btn);
        }
        keyboardPanel.add(row);
    }

    private void buildGrid() {
        wordGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 5; c++) {
                JLabel cell = new JLabel(" ", SwingConstants.CENTER);
                cell.setOpaque(true);
                cell.setBackground(Color.WHITE);
                cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
                cell.setFont(new Font("Arial", Font.BOLD, 26));
                boardCells[r][c] = cell;
                wordGrid.add(cell);
            }
        }
    }

    private void setupKeyBindings() {
        InputMap inputMap = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getRootPane().getActionMap();
        for (char c = 'A'; c <= 'Z'; c++) bindKey(inputMap, actionMap, c);
        for (char c = 'a'; c <= 'z'; c++) bindKey(inputMap, actionMap, c);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");
        actionMap.put("ENTER", new AbstractAction() { public void actionPerformed(ActionEvent e) { onKeyPress("ENTER"); }});
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "BACKSPACE");
        actionMap.put("BACKSPACE", new AbstractAction() { public void actionPerformed(ActionEvent e) { onKeyPress("BACKSPACE"); }});
    }

    private void bindKey(InputMap inputMap, ActionMap actionMap, char c) {
        final String letter = String.valueOf(c).toUpperCase();
        inputMap.put(KeyStroke.getKeyStroke(c), letter);
        actionMap.put(letter, new AbstractAction() { public void actionPerformed(ActionEvent e) { onKeyPress(letter); }});
    }
}