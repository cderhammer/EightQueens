import java.util.*;

public class EightQueens {

    final private int[][] board = new int[8][8];
    final private int[][] testBoard = new int[8][8];

    private int hVal = 0;
    private int queen = 0;
    private int restarts = 0;
    private int moves = 0;
    private int neighbors = 8;

    private boolean newBoard = true;

    // initialize chess board and loop through the array
    public EightQueens() {
        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                board[j][k] = 0;
            }
        }
    }

    // randomize chess board
    public void randomizeBoard() {
        Random randBoard = new Random();

        while (queen < 8) {
            for (int j = 0; j < 8; j++) {
                board[randBoard.nextInt(7)][j] = 1;
                queen++;
            }
        }
        hVal = hVal(board);
    }

    // find row conflicts
    public boolean findRow(int[][] val, int x) {
        boolean found = false;
        int count = 0;

        for (int j = 0; j < 8; j++) {
            if (val[j][x] == 1) {
                count++;
            }
        }
        if (count > 1) {
            found = true;
        }
        return found;
    }

    // find column conflicts
    public boolean findCol(int[][] val, int k) {
        boolean found = false;
        int count = 0;
        for (int j = 0; j < 8; j++) {
            if (val[k][j] == 1) {
                count++;
            }
        }
        if (count > 1) {
            found = true;
        }
        return found;
    }

    // find diagonal conflicts
    public boolean findDiag(int[][] val, int x, int y) {
        boolean diagFound = false;

        // if diag is not found found return false
        for (int j = 1; j < 8; j++) {
            if (diagFound) {
                break;
            }
            // right & up diag
            if ((x + j < 8) && (y + j < 8)) {
                if (val[x + j][y + j] == 1) {
                    diagFound = true;
                }
            }
            // right & down diag
            if ((x + j < 8) && (y - j >= 0)) {
                if (val[x + j][y - j] == 1) {
                    diagFound = true;
                }
            }
            // left & down diag
            if ((x - j >= 0) && (y - j >= 0)) {
                if (val[x - j][y - j] == 1) {
                    diagFound = true;
                }
            }
            // left & up diag
            if ((x - j >= 0) && (y + j < 8)) {
                if (val[x - j][y + j] == 1) {
                    diagFound = true;
                }
            }
            
        }
        return diagFound;
    }

    // heuristic value
    public int hVal(int[][] val) {
        int count = 0;
        boolean row;
        boolean col;
        boolean diag;

        // loop through the array looking for conflicts and add them up
        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                if (val[j][k] == 1) {
                    row = findRow(val, k);
                    col = findCol(val, j);
                    diag = findDiag(val, j, k);

                    if (row || col || diag) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    // creates row neighbors
    public int neighborRow(int[][] val) {
        int row = 8;
        int minVal = 8;

        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                if (val[j][k] < minVal) {
                    minVal = val[j][k];
                    row = j;
                }
            }
        }
        return row;
    }

    // creates column neighbors
    public int neighborCol(int[][] val) {
        int col = 8;
        int minVal = 8;
        int count = 0;

        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                if (val[j][k] < minVal) {
                    minVal = val[j][k];
                    col = k;
                }
                if (val[j][k] < hVal) {
                    count++;
                }
            }
        }
        neighbors = count;
        return col;
    }

    // queen (1) manipulation
    public void moveQueen() {
        int[][] array = new int[8][8];
        int numCols;
        int col;
        int row;
        int prevQueen = 0;
        newBoard = !newBoard;

        while (true) {
            numCols = 0;

            for (int j = 0; j < 8; j++) {
                System.arraycopy(board[j], 0, testBoard[j], 0, 8);
            }
            while (numCols < 8) {
                for (int j = 0; j < 8; j++) {
                    testBoard[j][numCols] = 0;
                }
                for (int j = 0; j < 8; j++) {
                    if (board[j][numCols] == 1) {
                        prevQueen = j;
                    }
                    testBoard[j][numCols] = 1;

                    array[j][numCols] = hVal(testBoard);

                    testBoard[j][numCols] = 0;
                }
                testBoard[prevQueen][numCols] = 1;
                numCols++;
            }

            if (restart(array)) {
                queen = 0;
                for (int j = 0; j < 8; j++) {
                    for (int k = 0; k < 8; k++) {
                        board[j][k] = 0;
                    }
                }
                randomizeBoard();
                System.out.println("\nRestarting...");
                restarts++;
            }

            col = neighborCol(array);
            row = neighborRow(array);

            for (int j = 0; j < 8; j++) {
                board[j][col] = 0;
            }

            board[row][col] = 1;
            moves++;
            hVal = hVal(board);

            // if a heuristic of 0 is found, end the program, solution was found
            if (hVal(board) == 0) {
                System.out.println("\nCurrent board");
                for (int j = 0; j < 8; j++) {
                    for (int k = 0; k < 8; k++) {
                        System.out.print(board[j][k] + " ");
                    }
                    System.out.print("\n");
                }
                System.out.println("Found a solution");
                System.out.println("Number of state changes: " + moves);
                System.out.println("Number of restarts: " + restarts);
                break;
            }

            // keep printing until a solution is found
            System.out.println("\n");
            System.out.println("Heuristic value: " + hVal);
            System.out.println("Current State");

            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    System.out.print(board[j][k] + " ");
                }
                System.out.print("\n");
            }
            System.out.println("Neighbors found with lower heuristic value: " + neighbors);
            System.out.println("Setting a new board state");
        }
    }

    // restarts the chess board
    public boolean restart(int[][] val) {
        int minVal = 8;
        boolean restart = false;

        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                if (val[j][k] < minVal) {
                    minVal = val[j][k];
                }
            }
        }
        if (neighbors == 0) {
            restart = true;
        }
        return restart;
    }
}