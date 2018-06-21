public class Field {

    //поле хранящее данные о ходе игры
    private char[][] field;

    public char[][] getField() {
        return field;
    }

    Field(int SIZE_Y, int SIZE_X) {
        field = new char[SIZE_Y][SIZE_X];
        for (int i=0; i < SIZE_Y; i++) {
            for (int j=0; j < SIZE_X; j++) {
                field[i][j] = '.';
            }
        }
    }
}
