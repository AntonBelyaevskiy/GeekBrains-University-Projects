public class PlayerTwo extends Players {

    static final char player_two_DOT = 'O';

    PlayerTwo(int Y, int X, int WIN) {
        SIZE_Y = Y;
        SIZE_X = X;
        SIZE_WIN = WIN;
    }

    public void playerTwoMove(int cellY, int cellX, Field fieldWithDot) {
        dotFieldPlayerTwo(cellY, cellX, player_two_DOT, fieldWithDot);
    }

    public static char getPlayer_two_DOT() {
        return player_two_DOT;
    }

    //запись хода игрока Two на поле
    protected void dotFieldPlayerTwo(int y, int x, char dot, Field fieldWithDot) {
        fieldWithDot.getField()[y][x] = dot;
    }
}
