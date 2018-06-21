abstract class Players {

    //общие данные для всех игроков, будь то сам игрок, компьютер или другой игрок
    //логика игры взята из урока про консольные крестики-нолики

    protected int SIZE_Y;
    protected int SIZE_X;
    protected int SIZE_WIN;

    static final char player_one_DOT = 'X';
    static final char ai_DOT = 'O';
    static final char EMPTY_DOT = '.';

    public static char getPlayer_one_DOT() {
        return player_one_DOT;
    }

    public static char getAi_DOT() {
        return ai_DOT;
    }

    //проверка на ничью (все  ячейки поля заполнены ходами)
    public boolean fullField(Field fieldWithDot) {
        for (int i = 0; i < SIZE_Y; i++) {
            for (int j = 0; j < SIZE_X; j++) {
                if (fieldWithDot.getField()[i][j] == EMPTY_DOT) return false;
            }
        }

        return true;
    }

    //проверка победы
    public boolean checkWin(char dot, Field fieldWithDot) {
        for (int v = 0; v < SIZE_Y; v++) {
            for (int h = 0; h < SIZE_X; h++) {
                //анализ наличие поля для проверки
                if (h + SIZE_WIN <= SIZE_X) {                           //по горизонтале
                    if (checkLineHorizontal(v, h, dot, fieldWithDot) >= SIZE_WIN) return true;

                    if (v - SIZE_WIN > -2) {                            //вверх по диагонале
                        if (checkDiaUp(v, h, dot, fieldWithDot) >= SIZE_WIN) return true;
                    }
                    if (v + SIZE_WIN <= SIZE_Y) {                       //вниз по диагонале
                        if (checkDiaDown(v, h, dot, fieldWithDot) >= SIZE_WIN) return true;
                    }
                }
                if (v + SIZE_WIN <= SIZE_Y) {                       //по вертикале
                    if (checkLineVertical(v, h, dot, fieldWithDot) >= SIZE_WIN) return true;
                }
            }
        }
        return false;
    }

    protected int checkLineHorizontal(int v, int h, char dot, Field fieldWithDot) {
        int count = 0;
        for (int j = h; j < SIZE_WIN + h; j++) {
            if ((fieldWithDot.getField()[v][j] == dot)) count++;
        }
        return count;
    }

    protected int checkLineVertical(int v, int h, char dot, Field fieldWithDot) {
        int count = 0;
        for (int i = v; i < SIZE_WIN + v; i++) {
            if ((fieldWithDot.getField()[i][h] == dot)) count++;
        }
        return count;
    }

    protected int checkDiaUp(int v, int h, char dot, Field fieldWithDot) {
        int count = 0;
        for (int i = 0, j = 0; j < SIZE_WIN; i--, j++) {
            if ((fieldWithDot.getField()[v + i][h + j] == dot)) count++;
        }
        return count;
    }

    protected int checkDiaDown(int v, int h, char dot, Field fieldWithDot) {
        int count = 0;
        for (int i = 0; i < SIZE_WIN; i++) {
            if ((fieldWithDot.getField()[i + v][i + h] == dot)) count++;
        }
        return count;
    }

    //запись хода игрока One на поле
    protected void dotFieldPlayerOne(int y, int x, char dot, Field fieldWithDot) {
        fieldWithDot.getField()[y][x] = dot;
    }

    //проверка заполнения выбранного для хода игроком
    protected boolean checkMove(int y, int x, Field fieldWithDot) {
        if (x < 0 || x >= SIZE_X || y < 0 || y >= SIZE_Y) return false;
        else if (!(fieldWithDot.getField()[y][x] == EMPTY_DOT)) return false;
        return true;
    }
}
