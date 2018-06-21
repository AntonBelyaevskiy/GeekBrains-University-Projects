import java.util.Random;

class Ai extends Players {

    private static Random rnd = new Random();

    public Ai(int Y, int X, int WIN) {
        SIZE_Y = Y;
        SIZE_X = X;
        SIZE_WIN = WIN;
    }

    //Ход компьютера
    public void aiMove(Field fieldWithDot) {
        int x, y;
        for (int v = 0; v < SIZE_Y; v++) {
            for (int h = 0; h < SIZE_X; h++) {
                if (h + SIZE_WIN <= SIZE_X) {
                    if (checkLineHorizontal(v, h, player_one_DOT, fieldWithDot) == SIZE_WIN - 1) {
                        if (moveAiLineHorizontal(v, h, ai_DOT, fieldWithDot)) return;
                    }

                    if (v - SIZE_WIN > -2) {
                        if (checkDiaUp(v, h, player_one_DOT, fieldWithDot) == SIZE_WIN - 1) {
                            if (moveAiDiaUp(v, h, ai_DOT, fieldWithDot)) return;
                        }
                    }
                    if (v + SIZE_WIN <= SIZE_Y) {
                        if (checkDiaDown(v, h, player_one_DOT, fieldWithDot) == SIZE_WIN - 1) {
                            if (moveAiDiaDown(v, h, ai_DOT, fieldWithDot)) return;
                        }
                    }
                }
                if (v + SIZE_WIN <= SIZE_Y) {
                    if (checkLineVertical(v, h, player_one_DOT, fieldWithDot) == SIZE_WIN - 1) {
                        if (moveAiLineVertical(v, h, ai_DOT, fieldWithDot)) return;
                    }
                }
            }
        }
        //игра на победу
        for (int v = 0; v < SIZE_Y; v++) {
            for (int h = 0; h < SIZE_X; h++) {
                if (h + SIZE_WIN <= SIZE_X) {
                    if (checkLineHorizontal(v, h, ai_DOT, fieldWithDot) == SIZE_WIN - 1) {
                        if (moveAiLineHorizontal(v, h, ai_DOT, fieldWithDot)) return;
                    }

                    if (v - SIZE_WIN > -2) {
                        if (checkDiaUp(v, h, ai_DOT, fieldWithDot) == SIZE_WIN - 1) {
                            if (moveAiDiaUp(v, h, ai_DOT, fieldWithDot)) return;
                        }
                    }
                    if (v + SIZE_WIN <= SIZE_Y) {
                        if (checkDiaDown(v, h, ai_DOT, fieldWithDot) == SIZE_WIN - 1) {
                            if (moveAiDiaDown(v, h, ai_DOT, fieldWithDot)) return;
                        }
                    }
                }

                if (v + SIZE_WIN <= SIZE_Y) {
                    if (checkLineVertical(v, h, ai_DOT, fieldWithDot) == SIZE_WIN - 1) {
                        if (moveAiLineVertical(v, h, ai_DOT, fieldWithDot)) return;
                    }
                }
            }
        }

        //случайный ход
        do {
            y = rnd.nextInt(SIZE_Y);
            x = rnd.nextInt(SIZE_X);
        } while (!checkMove(y, x, fieldWithDot));
        dotFieldPlayerOne(y, x, ai_DOT, fieldWithDot);
    }

    //ход компьютера по горизонтале
    private boolean moveAiLineHorizontal(int v, int h, char dot, Field dotField) {
        for (int j = h; j < SIZE_WIN; j++) {
            if ((dotField.getField()[v][j] == EMPTY_DOT)) {
                dotField.getField()[v][j] = dot;
                return true;
            }
        }
        return false;
    }

    //ход компьютера по вертикале
    private boolean moveAiLineVertical(int v, int h, char dot, Field dotField) {
        for (int i = v; i < SIZE_WIN; i++) {
            if ((dotField.getField()[i][h] == EMPTY_DOT)) {
                dotField.getField()[i][h] = dot;
                return true;
            }
        }
        return false;
    }

    private boolean moveAiDiaUp(int v, int h, char dot, Field dotField) {
        for (int i = 0, j = 0; j < SIZE_WIN; i--, j++) {
            if ((dotField.getField()[v + i][h + j] == EMPTY_DOT)) {
                dotField.getField()[v + i][h + j] = dot;
                return true;
            }
        }
        return false;
    }

    private boolean moveAiDiaDown(int v, int h, char dot, Field dotField) {

        for (int i = 0; i < SIZE_WIN; i++) {
            if ((dotField.getField()[i + v][i + h] == EMPTY_DOT)) {
                dotField.getField()[i + v][i + h] = dot;
                return true;
            }
        }
        return false;
    }
}
