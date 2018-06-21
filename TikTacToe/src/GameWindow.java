import javax.swing.*;
import java.awt.*;

class GameWindow extends JFrame {

    private static final int WIN_HEIGHT = 500;
    private static final int WIN_WIDTH = 500;

    private static StartNewGameWindow startNewGameWindow;
    private static Map field;

    GameWindow() {
        setTitle("Tic Tac Toe");
        setSize(WIN_WIDTH, WIN_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        startNewGameWindow = new StartNewGameWindow(this);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        JButton btnNewGame = new JButton("Start new game");
        btnNewGame.addActionListener(e -> startNewGameWindow.setVisible(true));
        bottomPanel.add(btnNewGame);
        JButton btnExit = new JButton("Exit game");
        btnExit.addActionListener(e -> System.exit(0));
        bottomPanel.add(btnExit);

        field = new Map();
        add(field, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    void startNewGame(int mode, int fieldSizeX, int fieldSizeY, int winLength) {
        field.initOptionsForNewGame(mode, fieldSizeX, fieldSizeY, winLength);
    }
}