package GameLogic;

public class board {
    private final EMatrixCellType[][] matrixBoard;
    private final int[] countFullCellsPerColumnArray;

    public board(int numOfRows, int numOfCols) {
        matrixBoard = new EMatrixCellType[numOfRows][numOfCols];
        countFullCellsPerColumnArray = new int[numOfCols];
    }

    public EMatrixCellType[][] getMatrixBoard() {
        return matrixBoard;
    }

    public int[] getCountFullCellsPerColumnArray() {
        return countFullCellsPerColumnArray;
    }

    public boolean checkIfColumnIsFull(int columnNum) {
        boolean isFullColumn = countFullCellsPerColumnArray[columnNum - 1] >= matrixBoard.length;
        return isFullColumn;
    }

    public void cleanGameBoard() {
        for (int row = 0; row < matrixBoard.length; row++) {
            for (int col = 0; col < matrixBoard[row].length; col++) {
                matrixBoard[row][col] = EMatrixCellType.BLANK;
            }
        }
    }

    public void resetCountFullCellsPerColumnArray() {
        for (int i = 0; i < countFullCellsPerColumnArray.length; i++) {
            countFullCellsPerColumnArray[i] = 0;
        }
    }
}
