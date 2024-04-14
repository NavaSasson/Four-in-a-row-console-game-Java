package GameLogic;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;
import java.util.List;


public class game {
    private static final int MinBoardSize = 4;
    private static final int MaxBoardSize = 8;
    private static final int MinMovesNumToHaveFourInARow = 7;
    private static final int MinMovesNumToHaveThreeInARow = 5;
    private final board gameBoard;
    private final playerInfo player1;
    private final playerInfo player2;
    private playerInfo currentPlayer;
    private int movesCounter = 0;

    public game(int numOfRows, int numOfCols, String player2Type) {
        gameBoard = new board(numOfRows, numOfCols);
        player1 = new playerInfo();
        player2 = new playerInfo(checkIfInputEqualToComputerType(player2Type), ECoinType.P2);
        currentPlayer = player1;
    }

    public board getGameBoard() {
        return gameBoard;
    }

    public playerInfo getPlayer1() {
        return player1;
    }

    public playerInfo getPlayer2() {
        return player2;
    }

    public playerInfo getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(playerInfo currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    private int getMovesCounter() {
        return movesCounter;
    }

    private void setMovesCounter(int movesCounter) {
        this.movesCounter = movesCounter;
    }

    public static boolean checkIfLegalBoardSize(int userInput) {
        return userInput >= MinBoardSize && userInput <= MaxBoardSize;
    }

    public static boolean checkIfLegalPlayerType(int userInput) {
        return EnumSet.allOf(EPlayerType.class).stream().anyMatch(type -> type.ordinal() == userInput);
    }

    private static boolean checkIfInputEqualToComputerType(String inputStr) {
        EPlayerType userChoice;
        try {
            userChoice = EPlayerType.valueOf(inputStr);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return userChoice == EPlayerType.COMPUTER;
    }

    public boolean checkIfThereIsFourInARow() {
        int rows = gameBoard.getMatrixBoard().length;
        int cols = gameBoard.getMatrixBoard()[0].length;
        boolean isItPossibleToHaveFourInARow = getMovesCounter() >= MinMovesNumToHaveFourInARow;
        boolean isFourInARow = false;

        if (isItPossibleToHaveFourInARow) {
            // Check horizontally
            for (int i = 0; i < rows && !isFourInARow; i++) {
                for (int j = 0; j <= cols - 4 && !isFourInARow; j++) {
                    isFourInARow = checkIfThereIsASequanceOfThreeInHorizontal(i, j) &&
                            gameBoard.getMatrixBoard()[i][j] == gameBoard.getMatrixBoard()[i][j + 3];
                }
            }

            // Check vertically
            for (int j = 0; j < cols && !isFourInARow; j++) {
                for (int i = 0; i <= rows - 4 && !isFourInARow; i++) {
                    isFourInARow = checkIfThereIsASequanceOfThreeInVertical(i, j) &&
                            gameBoard.getMatrixBoard()[i][j] == gameBoard.getMatrixBoard()[i + 3][j];
                }
            }

            // Check diagonally (top-right to bottom-left)
            for (int i = 0; i <= rows - 4 && !isFourInARow; i++) {
                for (int j = 0; j <= cols - 4 && !isFourInARow; j++) {
                    isFourInARow = checkIfThereIsASequanceOfThreeInRightToLeftDiagonal(i, j) &&
                            gameBoard.getMatrixBoard()[i][j] == gameBoard.getMatrixBoard()[i + 3][j + 3];
                }
            }

            // Check diagonally (top-left to bottom-right)
            for (int i = 0; i <= rows - 4 && !isFourInARow; i++) {
                for (int j = cols - 1; j >= 3 && !isFourInARow; j--) {
                    isFourInARow = checkIfThereIsASequanceOfThreeInLeftToRightDiagonal(i, j) &&
                            gameBoard.getMatrixBoard()[i][j] == gameBoard.getMatrixBoard()[i + 3][j - 3];
                }
            }
        }

        return isFourInARow;
    }

    public boolean checkIfMoveIsLegal(int columnNumToInsertACoin) {
        boolean isLegalMove = true;

        if (currentPlayer.getPlayerCoin() == ECoinType.P1 ||
                (currentPlayer.getPlayerCoin() == ECoinType.P2 && !player2.isComputerPlayer())) {
            isLegalMove = checkIfInputInRangeOfBoard(columnNumToInsertACoin) &&
                    !checkIfColumnInGameBoardIsFull(columnNumToInsertACoin);
        }

        return isLegalMove;
    }

    public boolean checkIfUserInputOfAnotherGameIsLegal(int inputFromUser) {
        return EnumSet.allOf(EPlayerWantsAnotherGameType.class).stream().anyMatch(type -> type.ordinal() == inputFromUser);
    }

    public boolean checkIfUserInputForAnotherGameIsYes(String inputFromUserStr) {
        try {
            EPlayerWantsAnotherGameType userChoice = EPlayerWantsAnotherGameType.valueOf(inputFromUserStr);
            return userChoice == EPlayerWantsAnotherGameType.YES;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean checkIfBoardIsFull() {
        return getMovesCounter() == gameBoard.getMatrixBoard().length * gameBoard.getMatrixBoard()[0].length;
    }

    private boolean checkIfColumnInGameBoardIsFull(int columnNum) {
        return gameBoard.checkIfColumnIsFull(columnNum);
    }

    private boolean checkIfInputInRangeOfBoard(int columnNumToInsertACoin) {
        return columnNumToInsertACoin > 0 && columnNumToInsertACoin <= gameBoard.getMatrixBoard()[0].length;
    }

    public void updatePoints(boolean isCurrentPlayerWin) {
        if (isCurrentPlayerWin) {
            if (currentPlayer.getPlayerCoin() == ECoinType.P1) {
                player1.setCurrentPoints(player1.getCurrentPoints() + 1);
            } else {
                player2.setCurrentPoints(player2.getCurrentPoints() + 1);
            }
        } else if (!checkIfBoardIsFull()) { // User entered Q
            if (currentPlayer.getPlayerCoin() == ECoinType.P1) {
                player2.setCurrentPoints(player2.getCurrentPoints() + 1);
            } else {
                player1.setCurrentPoints(player1.getCurrentPoints() + 1);
            }
        } // else - Tie
    }

    public void updateBoardAccordingToPlayerMove(int columnNumToInsertACoin) {
        if (currentPlayer.isComputerPlayer()) {
            columnNumToInsertACoin = chooseComputerMove();
        }

        int rowToInsertCoin = gameBoard.getCountFullCellsPerColumnArray()[columnNumToInsertACoin - 1];
        if (currentPlayer.getPlayerCoin() == ECoinType.P1) {
            gameBoard.getMatrixBoard()[rowToInsertCoin][columnNumToInsertACoin - 1] = EMatrixCellType.P1;
        } else {
            gameBoard.getMatrixBoard()[rowToInsertCoin][columnNumToInsertACoin - 1] = EMatrixCellType.P2;
        }

        gameBoard.getCountFullCellsPerColumnArray()[columnNumToInsertACoin - 1]++;
        movesCounter++;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer.getPlayerCoin() == ECoinType.P1) ? player2 : player1;
    }

    private int chooseARandomMove() {
        int columnNum = gameBoard.getMatrixBoard()[0].length;
        List<Integer> arrayOfEmptyColumns = new ArrayList<>();

        while (columnNum > 0) {
            if (!checkIfColumnInGameBoardIsFull(columnNum)) {
                arrayOfEmptyColumns.add(columnNum);
            }
            columnNum--;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(arrayOfEmptyColumns.size());
        return arrayOfEmptyColumns.get(randomIndex);
    }

    public void activateAnotherGame() {
        gameBoard.cleanGameBoard();
        gameBoard.resetCountFullCellsPerColumnArray();
        currentPlayer = player1;
        movesCounter = 0;
    }

    private boolean checkIfThereIsASequanceOfThreeInHorizontal(int row, int col) {
        return gameBoard.getMatrixBoard()[row][col] != EMatrixCellType.BLANK &&
                gameBoard.getMatrixBoard()[row][col] == gameBoard.getMatrixBoard()[row][col + 1] &&
                gameBoard.getMatrixBoard()[row][col] == gameBoard.getMatrixBoard()[row][col + 2];
    }

    private boolean checkIfThereIsASequanceOfThreeInVertical(int row, int col) {
        return gameBoard.getMatrixBoard()[row][col] != EMatrixCellType.BLANK &&
                gameBoard.getMatrixBoard()[row][col] == gameBoard.getMatrixBoard()[row + 1][col] &&
                gameBoard.getMatrixBoard()[row][col] == gameBoard.getMatrixBoard()[row + 2][col];
    }

    private boolean checkIfThereIsASequanceOfThreeInRightToLeftDiagonal(int row, int col) {
        return gameBoard.getMatrixBoard()[row][col] != EMatrixCellType.BLANK &&
                gameBoard.getMatrixBoard()[row][col] == gameBoard.getMatrixBoard()[row + 1][col + 1] &&
                gameBoard.getMatrixBoard()[row][col] == gameBoard.getMatrixBoard()[row + 2][col + 2];
    }

    private boolean checkIfThereIsASequanceOfThreeInLeftToRightDiagonal(int row, int col) {
        return gameBoard.getMatrixBoard()[row][col] != EMatrixCellType.BLANK &&
                gameBoard.getMatrixBoard()[row][col] == gameBoard.getMatrixBoard()[row + 1][col - 1] &&
                gameBoard.getMatrixBoard()[row][col] == gameBoard.getMatrixBoard()[row + 2][col - 2];
    }

    private int chooseComputerMove() {
        int numOfRows = gameBoard.getMatrixBoard().length;
        int numOfCols = gameBoard.getMatrixBoard()[0].length;
        boolean isItPossibleToHaveThreeInARow = getMovesCounter() >= MinMovesNumToHaveThreeInARow;
        boolean isThreeInARow = false;
        int potentialColToInsertACoin = -1;

        if (isItPossibleToHaveThreeInARow) {
            // Check horizontally
            for (int row = 0; row < numOfRows && !isThreeInARow; row++) {
                for (int col = 0; col <= numOfCols - 4 && !isThreeInARow; col++) {
                    if (checkIfThereIsASequanceOfThreeInHorizontal(row, col)) {
                        if (gameBoard.getCountFullCellsPerColumnArray()[col + 3] == row) {
                            potentialColToInsertACoin = col + 4;
                            isThreeInARow = true;
                        } else if (col != 0 && gameBoard.getCountFullCellsPerColumnArray()[col - 1] == row) {
                            potentialColToInsertACoin = col;
                            isThreeInARow = true;
                        }
                    }
                }
            }

            // Check vertically
            for (int col = 0; col < numOfCols && !isThreeInARow; col++) {
                for (int row = 0; row <= numOfRows - 4 && !isThreeInARow; row++) {
                    isThreeInARow = checkIfThereIsASequanceOfThreeInVertical(row, col) &&
                            gameBoard.getCountFullCellsPerColumnArray()[col] == row + 3;
                    if (isThreeInARow) {
                        potentialColToInsertACoin = col + 1;
                    }
                }
            }

            // Check diagonally (top-right to bottom-left)
            for (int row = 0; row <= numOfRows - 4 && !isThreeInARow; row++) {
                for (int col = 0; col <= numOfCols - 4 && !isThreeInARow; col++) {
                    if (checkIfThereIsASequanceOfThreeInRightToLeftDiagonal(row, col)) {
                        if (gameBoard.getCountFullCellsPerColumnArray()[col + 3] == row + 3) {
                            potentialColToInsertACoin = col + 4;
                            isThreeInARow = true;
                        } else if (col != 0 && row != 0 && gameBoard.getCountFullCellsPerColumnArray()[col - 1] == row - 1) {
                            potentialColToInsertACoin = col;
                            isThreeInARow = true;
                        }
                    }
                }
            }

            // Check diagonally (top-left to bottom-right)
            for (int row = 0; row <= numOfRows - 4 && !isThreeInARow; row++) {
                for (int col = numOfCols - 1; col >= 3 && !isThreeInARow; col--) {
                    if (checkIfThereIsASequanceOfThreeInLeftToRightDiagonal(row, col)) {
                        if (gameBoard.getCountFullCellsPerColumnArray()[col - 3] == row + 3) {
                            potentialColToInsertACoin = col - 2;
                            isThreeInARow = true;
                        }
                    }
                }
            }
        }

        if (!isItPossibleToHaveThreeInARow || !isThreeInARow) {
            potentialColToInsertACoin = chooseARandomMove();
        }

        return potentialColToInsertACoin;
    }
}
