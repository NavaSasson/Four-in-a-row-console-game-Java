package UI;
import GameLogic.game;
import GameLogic.EMatrixCellType;

import java.util.Collections;
import java.util.Scanner;

public class userInterface {

    private static final int NumOfSpaces = 3;
    private static final int NumOfCharAccurance = 1;

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello!\nPlease enter the number of rows for the board size (between 4 and 8)");
        String numOfRowsInput = getValidMatrixSizeOrTypeOfPlayer(scanner, "Rows");
        System.out.println("Please enter the number of columns for the board size (between 4 and 8)");
        String numOfColsInput = getValidMatrixSizeOrTypeOfPlayer(scanner, "Cols");
        System.out.println("If you want to play against another player press 1\nIf you want to play against the computer press 2");
        String againstPlayerInput = getValidMatrixSizeOrTypeOfPlayer(scanner, "Against player");
        int numOfRows = Integer.parseInt(numOfRowsInput);
        int numOfCols = Integer.parseInt(numOfColsInput);
        game game = new game(numOfRows, numOfCols, againstPlayerInput);
//       clearScreen();
        runGame(game, scanner);
        scanner.close();
    }

    private void runGame(game game, Scanner scanner) {
        int columnNumToInsertACoin;
        boolean isCurrentPlayerIsAWinner = false;

        System.out.println("Please enter the number of the column in which you would like to insert a coin or Q to quit the game");
        String inputFromUser = scanner.nextLine();
        while (!inputFromUser.equalsIgnoreCase("Q")) {
            if (checkIfValidColumnOrValidInputOfAnotherGame(game, inputFromUser, "column")) {
                columnNumToInsertACoin = Integer.parseInt(inputFromUser);
            } else {
                inputFromUser = getValidColumnOrAnotherGameInput(game, scanner, "column");
                if (inputFromUser == null) {
                    break;
                }
                columnNumToInsertACoin = Integer.parseInt(inputFromUser);
            }

            isCurrentPlayerIsAWinner = manageMoveAndCheckIfThereIsAWinner(game, columnNumToInsertACoin);
            if (isCurrentPlayerIsAWinner) {
                break;
            }

            game.switchPlayer();
            if (game.getCurrentPlayer().isComputerPlayer()) {
                isCurrentPlayerIsAWinner = manageMoveAndCheckIfThereIsAWinner(game, columnNumToInsertACoin);
                game.switchPlayer();
            }

            if (isCurrentPlayerIsAWinner) {
                break;
            } else if (game.checkIfBoardIsFull()) {
                System.out.println("The game ended with a tie");
                break;
            } else {
                System.out.println("Please enter the number of the column in which you would like to insert a coin or Q to quit the game");
                inputFromUser = scanner.nextLine();
            }
        }

        if (inputFromUser.equalsIgnoreCase("Q")) {
            System.out.println("You chose to quit the game");
            game.updatePoints(isCurrentPlayerIsAWinner);
        }

        printPointsStatus(game);
        if (checkIfUserWantsAnotherGame(game, scanner)) {
            game.activateAnotherGame();
            //cleanAndPrintScreen(game);
            runGame(game, scanner);
        } else {
            System.out.println("You chose to end the game, Bye Bye!");
        }
    }

    private boolean manageMoveAndCheckIfThereIsAWinner(game game, int columnNumToInsertACoin) {
        game.updateBoardAccordingToPlayerMove(columnNumToInsertACoin);
        cleanAndPrintScreen(game);
        return checkAndHandleWinnerCase(game);
    }

    private void cleanAndPrintScreen(game game) {
        // Clearing screen and printing board can be implemented accordingly
    	printBoard(game);
    }

    private void printPointsStatus(game i_Game) {
        String typeOfPlayer2 = "Player2";

        if (i_Game.getPlayer2().isComputerPlayer()) {
            typeOfPlayer2 = "Computer";
        }

        System.out.println(String.format(
                "The points status is:%nPlayer1: %d%n%s: %d",
                i_Game.getPlayer1().getCurrentPoints(), typeOfPlayer2, i_Game.getPlayer2().getCurrentPoints()));
    }


    private void printBoard(game i_Game) {
        int numOfRowsInBoardGame = i_Game.getGameBoard().getMatrixBoard().length;
        int numOfColsInBoardGame = i_Game.getGameBoard().getMatrixBoard()[0].length;
        String separatorLine = "====";

        separatorLine = String.join("", Collections.nCopies(numOfColsInBoardGame, separatorLine));
        separatorLine = separatorLine.concat("=");
        
        for (int row = numOfRowsInBoardGame - 1; row >= 0; row--) {
            if (row == numOfRowsInBoardGame - 1) {
                for (int currentColumn = 1; currentColumn <= numOfColsInBoardGame; currentColumn++) {
                    System.out.print("  " + currentColumn + " ");
                }
                System.out.println();
            } else {
                for (int col = 0; col < numOfColsInBoardGame; col++) {
                    if (numOfColsInBoardGame < row) {
                    	for (int currentColumn = 1; currentColumn <= numOfColsInBoardGame; currentColumn++) {
                            System.out.printf("  %d ", currentColumn);
                        }
                        System.out.println();
                    } else {
                        EMatrixCellType cellType = i_Game.getGameBoard().getMatrixBoard()[row][col];
                        if (cellType == EMatrixCellType.BLANK) {
                           // System.out.print("|   ");
                            System.out.print("|5   ");
                        } else if (cellType == EMatrixCellType.P1) {
                            System.out.print("| X ");
                        } else {
                            System.out.print("| O ");
                        }
                    }
                }
                System.out.println("|");
                System.out.println(separatorLine);
            }
        }
    }

    private String getValidMatrixSizeOrTypeOfPlayer(Scanner scanner, String flagStr) {
        String inputFromUser = scanner.nextLine();
        boolean isValidInput = checkIfValidMatrixSizeOrTypeOfPlayer(inputFromUser, flagStr);

        if (!isValidInput) {
            System.out.println("Invalid number, try again!");
            inputFromUser = getValidMatrixSizeOrTypeOfPlayer(scanner, flagStr);
        }

        return inputFromUser;
    }

    private boolean checkIfValidMatrixSizeOrTypeOfPlayer(String inputFromUser, String flagStr) {
        boolean isIntegerNumber = inputFromUser.matches("\\d+");
        boolean isValidInput = isIntegerNumber;

        if (isIntegerNumber) {
            int inputToInteger = Integer.parseInt(inputFromUser);
            if (flagStr.equals("Rows") || flagStr.equals("Cols")) {
                isValidInput = game.checkIfLegalBoardSize(inputToInteger);
            } else if (flagStr.equals("Against player")) {
                isValidInput = game.checkIfLegalPlayerType(inputToInteger);
            }
        }

        return isValidInput;
    }

    private String getValidColumnOrAnotherGameInput(game game, Scanner scanner, String flagStr) {
        String inputColumnFromUser = scanner.nextLine();
        boolean isValidColumnInput = checkIfValidColumnOrValidInputOfAnotherGame(game, inputColumnFromUser, flagStr);

        if (!isValidColumnInput) {
            inputColumnFromUser = getValidColumnOrAnotherGameInput(game, scanner, flagStr);
        }

        return inputColumnFromUser;
    }

    private boolean checkIfValidColumnOrValidInputOfAnotherGame(game game, String inputFromUser, String flagStr) {
        boolean isUserWantsToQuit = inputFromUser.equalsIgnoreCase("Q") && flagStr.equals("column");
        boolean isValidInput = isUserWantsToQuit;

        if (!isUserWantsToQuit) {
            boolean isIntegerNumber = inputFromUser.matches("\\d+");
            isValidInput = isIntegerNumber;
            if (isIntegerNumber) {
                int inputToInteger = Integer.parseInt(inputFromUser);
                if (flagStr.equals("column")) {
                    isValidInput = game.checkIfMoveIsLegal(inputToInteger);
                } else if (flagStr.equals("anotherGame")) {
                    isValidInput = game.checkIfUserInputOfAnotherGameIsLegal(inputToInteger);
                }
            }

            if (!isValidInput) {
                System.out.println("Invalid input, try again!");
            }
        }

        return isValidInput;
    }

    private boolean checkAndHandleWinnerCase(game game) {
        boolean isCurrentPlayerIsAWinner = game.checkIfThereIsFourInARow();

        if (isCurrentPlayerIsAWinner) {
            game.updatePoints(isCurrentPlayerIsAWinner);
            String winnerStr = game.getCurrentPlayer().isComputerPlayer() ? "Computer" : game.getCurrentPlayer().getPlayerCoin().toString();
            System.out.println("The winner is " + winnerStr);
        }

        return isCurrentPlayerIsAWinner;
    }

    private boolean checkIfUserWantsAnotherGame(game game, Scanner scanner) {
        System.out.println("If you want another game please press 1, otherwise press 2");
        String validInputFromUser = getValidColumnOrAnotherGameInput(game, scanner, "anotherGame");
        return game.checkIfUserInputForAnotherGameIsYes(validInputFromUser);
    }
    
    public static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
