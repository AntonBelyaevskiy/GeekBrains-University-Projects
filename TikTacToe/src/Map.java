import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Map extends JPanel {

    public static final int MODE_H_V_A = 0;
    public static final int MODE_H_V_H = 1;
    private static String STR_WHO_WIN;

    private int fieldSizeX;
    private int fieldSizeY;
    private int winLength;
    private int mode;

    private int cellHeight;
    private int cellWidth;
    private int cellX;
    private int cellY;

    private PlayerOne playerOne;
    private PlayerTwo playerTwo;
    private Ai playerAi;
    private Field fieldWithDot;
    private MouseListener mAdapter;

    private boolean isAiPlayed;
    private boolean isOnePlayerStep = true;
    private boolean isInitialized = false;

    Map() {
        setBackground(Color.GREEN);
    }

    //действия при нажатии кнопки мыши
    private void update(MouseEvent e) {
        cellX = e.getX() / cellWidth;
        cellY = e.getY() / cellHeight;

        //выбираем метод в зависимости от того, с кем играем с человеком или компьютером
        if (isAiPlayed) {
            gameWithAi(cellY, cellX);
        } else {
            gameWithPlayer(cellY, cellX);
        }
        repaint();
    }

    //прорисовываем сетку на поле и фигуры когда, кто-либо из игроков ходит
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
        drowXorO(g);
    }

    //инициализируем данные для игры
    void initOptionsForNewGame(int mode, int fieldSizeX, int fieldSizeY, int winLength) {

        removeMouseListener(mAdapter);
        //без изначального удаления этого листенера, возникает проблема.
        //Если после того, как сыграна игра, нажать на кнопку Yes, когда программа предлагает сыграть ещё раз,
        //и при этом изменить параметры игры (например увеличить размер поля),
        //во всех последующих играх кроме первой, первый ход игрока ставиться автоматически
        //(клеткой для этого первого хода, становиться последняя занятая клетка из предыдущей игры)
        //Поэтому при инициализации, каждой новой игры, сперва запрещаем принимать, какие-либо данные от мышки.

        this.fieldSizeX = fieldSizeX;
        this.fieldSizeY = fieldSizeY;
        this.winLength = winLength;

        fieldWithDot = new Field(fieldSizeY, fieldSizeX);
        playerOne = new PlayerOne(fieldSizeY, fieldSizeX, winLength);
        //выбираем в качестве противника Компьютер или Другого человека
        switch (mode) {
            case MODE_H_V_A:
                playerAi = new Ai(fieldSizeY, fieldSizeX, winLength);
                isAiPlayed = true;
                this.mode = mode;
                break;
            case MODE_H_V_H:
                playerTwo = new PlayerTwo(fieldSizeY, fieldSizeX, winLength);
                isAiPlayed = false;
                this.mode = mode;
                break;
        }
        for (int i=0; i < fieldSizeX; i++) {
            for (int j=0; j < fieldSizeY; j++) {
                System.out.print(fieldWithDot.getField()[i][j]);
            }
            System.out.println();
        }

        isInitialized = true;
        repaint();

        //запускаем игру
        startThisGame();
    }

    //прорисовка игрового поля
    private void render(Graphics g) {
        if (!isInitialized) return;

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        cellHeight = panelHeight / fieldSizeY;
        cellWidth = panelWidth / fieldSizeX;

        for (int i = 0; i < fieldSizeY; i++) {
            int y = i * cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }

        for (int i = 0; i < fieldSizeX; i++) {
            int x = i * cellWidth;
            g.drawLine(x, 0, x, panelHeight);
        }
    }

    //запуск игры. Вынес слушателя нажатия клавишь мышкой, чтобы он реагировал на нажатия, до начала игры
    private void startThisGame() {
        mAdapter = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                //ход игры при каждом нажатии кнопки мыши
                update(e);
            }
        };
        addMouseListener(mAdapter);
    }

    //режим игры против компьютера
    private void gameWithAi(int y, int x) {

        //проверка выбора не занятой клетки поля
        if (playerOne.checkMove(y, x, fieldWithDot))
            playerOne.playerOneMove(y, x, fieldWithDot);
        else return;

        //проверка победы
        if (playerOne.checkWin(Players.getPlayer_one_DOT(), fieldWithDot)) {
            STR_WHO_WIN = "Вы выиграли!";
            repaint();
            endThisGame();
            return;
        }
        // проверка ничьи
        else if (playerOne.fullField(fieldWithDot)) {
            repaint();
            STR_WHO_WIN = "Игра закончилась в ничью";
            repaint();
            endThisGame();
            return;
        }

        //ход компьютера
        do {
            playerAi.aiMove(fieldWithDot);
        } while (playerAi.checkMove(cellY, cellX, fieldWithDot));

        //проверка победы
        if (playerAi.checkWin(Ai.getAi_DOT(), fieldWithDot)) {
            STR_WHO_WIN = "Искусственный Интелект выиграл!";
            repaint();
            endThisGame();
        }
        // проверка ничьи
        else if (playerAi.fullField(fieldWithDot)) {
            repaint();
            STR_WHO_WIN = "Игра закончилась в ничью";
            repaint();
            endThisGame();
        }

        for (int i=0; i < fieldSizeX; i++) {
            for (int j=0; j < fieldSizeY; j++) {
                System.out.print(fieldWithDot.getField()[i][j]);
            }
            System.out.println();
        }
    }

    //игра против другого человека
    private void gameWithPlayer(int y, int x) {

        if (isOnePlayerStep) {

            if (playerOne.checkMove(y, x, fieldWithDot))
                playerOne.playerOneMove(y, x, fieldWithDot);
            else return;

            if (playerOne.checkWin(Players.getPlayer_one_DOT(), fieldWithDot)) {
                STR_WHO_WIN = "Выиграл игрок №1";
                repaint();
                endThisGame();
                return;
            } else if (playerOne.fullField(fieldWithDot)) {
                STR_WHO_WIN = "Ничья!";
                repaint();
                endThisGame();
                return;
            }
            isOnePlayerStep = false;
        } else {

            if (playerTwo.checkMove(y, x, fieldWithDot))
                playerTwo.playerTwoMove(y, x, fieldWithDot);
            else return;

            if (playerTwo.checkWin(PlayerTwo.getPlayer_two_DOT(), fieldWithDot)) {
                STR_WHO_WIN = "Выиграл игрок №2";
                repaint();
                endThisGame();
                return;
            } else if (playerTwo.fullField(fieldWithDot)) {
                STR_WHO_WIN = "Ничья!";
                repaint();
                endThisGame();
                return;
            }
            isOnePlayerStep = true;
        }
    }

    //прорисовка на поле X и O
    private void drowXorO(Graphics g) {

        //проходим по массиву нашего char поля и в соответствии с его заполненностью рисуем фигуры
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (fieldWithDot.getField()[j][i] != '.') {
                    if (fieldWithDot.getField()[j][i] == 'X') {
                        g.setColor(Color.BLUE);
                        g.drawLine((i * cellWidth), (j * cellHeight), ((i + 1) * cellWidth), ((j + 1) * cellHeight));
                        g.drawLine(((i + 1) * cellWidth), (j * cellHeight), (i * cellWidth), ((j + 1) * cellHeight));
                    }
                    if (fieldWithDot.getField()[j][i] == 'O') {
                        g.setColor(Color.RED);
                        g.drawOval((i * cellWidth), (j * cellHeight), cellWidth, cellHeight);
                    }
                }
            }
        }
    }

    //когда игра закончилась, выводим результат, обнуляем игровые данные, на тот случай, если захотим ещё раз сыграть
    private void endThisGame() {
        removeMouseListener(mAdapter);

        int dialogResult = JOptionPane.showConfirmDialog(this, STR_WHO_WIN, "играть ещё раз?", JOptionPane.YES_NO_OPTION);

        if (dialogResult == JOptionPane.YES_OPTION) {
            //Если играем ещё раз, то обнуляем все параметры
            playerOne = null;
            playerTwo = null;
            playerAi = null;
            fieldWithDot = null;

            isOnePlayerStep = true;
            isInitialized = false;

            //запускаем следующую игру
            initOptionsForNewGame(mode, fieldSizeX, fieldSizeY, winLength);


        } else {
            System.exit(0);
        }
    }
}