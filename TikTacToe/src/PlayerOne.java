class PlayerOne extends Players {

    PlayerOne(int Y, int X, int WIN) {
        SIZE_Y = Y;
        SIZE_X = X;
        SIZE_WIN = WIN;
    }

    public void playerOneMove(int cellY, int cellX, Field fieldWithDot) {
        dotFieldPlayerOne(cellY, cellX, PlayerOne.getPlayer_one_DOT(), fieldWithDot);
    }
}
