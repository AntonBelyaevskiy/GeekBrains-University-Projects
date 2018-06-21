import javax.swing.*;
import java.awt.*;


class StartNewGameWindow extends JFrame{

    private final GameWindow gameWindow;

    private static final int WIN_HEIGHT = 250;
    private static final int WIN_WIDTH = 350;
    private static final int MIN_WIN_LEN = 3;
    private static final int MIN_FIELD_SIZE = 3;
    private static final int MAX_FIELD_SIZE = 10;
    private static final String STR_WIN_LEN = "Winning Length: ";
    private static final String STR_FIELD_SIZE = "Field Size: ";

    private JRadioButton jrbHumVsAi = new JRadioButton("Human vs. AI", true);
    private JRadioButton jrbHumVsHum = new JRadioButton("Human vs. Human");
    private ButtonGroup gameMode = new ButtonGroup();

    private JSlider slFieldSize;
    private JSlider slWinLength;

    StartNewGameWindow(GameWindow gameWindow) {

        this.gameWindow = gameWindow;
        setTitle("New game parameters");
        setSize(WIN_WIDTH, WIN_HEIGHT);
        setLocationRelativeTo(gameWindow);
        setLayout(new GridLayout(10, 1));

        addGameContolsMode();
        addGameControlsFieldWinLenght();

        JButton btnStartGame = new JButton("Start a game");
        btnStartGame.addActionListener(e -> btnStartGameClick());
        add(btnStartGame);
    }

    private void addGameContolsMode() {
        add(new JLabel("Choose gaming mode:"));
        gameMode.add(jrbHumVsAi);
        gameMode.add(jrbHumVsHum);
        add(jrbHumVsHum);
        add(jrbHumVsAi);
    }

    private void addGameControlsFieldWinLenght() {
        add(new JLabel("Choose field size:"));
        final JLabel lblFieldSize = new JLabel(STR_FIELD_SIZE + MIN_FIELD_SIZE);
        add(lblFieldSize);

       slFieldSize = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
        slFieldSize.addChangeListener(e -> {
            int currentFieldSize = slFieldSize.getValue();
            lblFieldSize.setText(STR_FIELD_SIZE + currentFieldSize);
            slWinLength.setMaximum(currentFieldSize);
        });
        add(slFieldSize);

        add(new JLabel("Choose winning length:"));
        final JLabel lblWinLen = new JLabel(STR_WIN_LEN + MIN_WIN_LEN);
        add(lblWinLen);

        //в качестве максимального значения для slWinLength устанавливаем MIN_FIELD_SIZE, чтобы при запуске,
        //если мы не меняем значение Field Size небыло бы возможности установить slWinLength > slFieldSize
        slWinLength = new JSlider(MIN_WIN_LEN, MIN_FIELD_SIZE, MIN_WIN_LEN);
        slWinLength.addChangeListener(e -> lblWinLen.setText(STR_WIN_LEN + slWinLength.getValue()));
        add(slWinLength);
    }

    private void btnStartGameClick() {
        int gameMode;
        int fieldSize = slFieldSize.getValue();
        int winLength = slWinLength.getValue();

        if(jrbHumVsAi.isSelected()) gameMode = Map.MODE_H_V_A;
        else gameMode = Map.MODE_H_V_H;

        gameWindow.startNewGame(gameMode, fieldSize, fieldSize, winLength);
        setVisible(false);
    }


}
