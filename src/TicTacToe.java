import java.util.Scanner;
//Tic-Tac-Toe
class TicTacToe {
    private static final int COMPUTER = 1;
    private static final int HUMAN = 2;
    private static final int SIDE = 3;
    private static final char COMPUTERMOVE = 'O';
    private static final char HUMANMOVE = 'X';
    private static final char EMPTY = '*';

    private static void showBoard(char[][] board) {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                System.out.print(" " + board[i][j] + " ");
                if (j < SIDE - 1) System.out.print("|");
            }
            System.out.println();
            if (i < SIDE - 1) System.out.println("-----------");
        }
        System.out.println();
    }

    private static void showInstructions() {
        System.out.println("\nChoose a cell numbered from 1 to 9 as below and play\n");
        int count = 1;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                System.out.print(" " + count + " ");
                if (j < SIDE - 1) System.out.print("|");
                count++;
            }
            System.out.println();
            if (i < SIDE - 1) System.out.println("-----------");
        }
        System.out.println();
    }

    private static void initialise(char[][] board) {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    private static boolean rowCrossed(char[][] board) {
        for (int i = 0; i < SIDE; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != EMPTY)
                return true;
        }
        return false;
    }

    private static boolean columnCrossed(char[][] board) {
        for (int i = 0; i < SIDE; i++) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != EMPTY)
                return true;
        }
        return false;
    }

    private static boolean diagonalCrossed(char[][] board) {
        return (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != EMPTY) ||
                (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != EMPTY);
    }

    private static boolean gameOver(char[][] board) {
        return rowCrossed(board) || columnCrossed(board) || diagonalCrossed(board);
    }

    private static int minimax(char[][] board, int depth, boolean isAI) {
        if (gameOver(board)) return isAI ? -10 : 10;
        if (depth == SIDE * SIDE) return 0;

        int bestScore = isAI ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = isAI ? COMPUTERMOVE : HUMANMOVE;
                    int score = minimax(board, depth + 1, !isAI);
                    board[i][j] = EMPTY;
                    bestScore = isAI ? Math.max(bestScore, score) : Math.min(bestScore, score);
                }
            }
        }
        return bestScore;
    }

    private static int bestMove(char[][] board) {
        int bestScore = Integer.MIN_VALUE;
        int move = -1;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = COMPUTERMOVE;
                    int score = minimax(board, 0, false);
                    board[i][j] = EMPTY;
                    if (score > bestScore) {
                        bestScore = score;
                        move = i * SIDE + j;
                    }
                }
            }
        }
        return move;
    }

    private static void playTicTacToe(int whoseTurn, Scanner sc) {
        char[][] board = new char[SIDE][SIDE];
        initialise(board);
        showInstructions();
        int moveIndex = 0;
        while (!gameOver(board) && moveIndex < SIDE * SIDE) {
            int move;
            if (whoseTurn == COMPUTER) {
                move = bestMove(board);
                System.out.println("COMPUTER placed " + COMPUTERMOVE + " at position " + (move + 1));
                board[move / SIDE][move % SIDE] = COMPUTERMOVE;
                whoseTurn = HUMAN;
            } else {
                System.out.print("Enter your move (1-9): ");
                if (sc.hasNextInt()) {
                    move = sc.nextInt() - 1;
                    if (move >= 0 && move < 9 && board[move / SIDE][move % SIDE] == EMPTY) {
                        board[move / SIDE][move % SIDE] = HUMANMOVE;
                        whoseTurn = COMPUTER;
                    } else {
                        System.out.println("Invalid move. Try again.");
                        continue;
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number between 1-9.");
                    sc.next();  // Clear invalid input
                    continue;
                }
            }
            showBoard(board);
            moveIndex++;
        }
        if (gameOver(board)) System.out.println(whoseTurn == COMPUTER ? "HUMAN WINS!" : "COMPUTER WINS!");
        else System.out.println("It's a draw!");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Tic Tac Toe");
        char choice;
        do {
            System.out.print("Do you want to start first? (y/n): ");
            choice = sc.next().charAt(0);
            if (choice == 'n') playTicTacToe(COMPUTER, sc);
            else if (choice == 'y') playTicTacToe(HUMAN, sc);
            else System.out.println("Invalid choice.");
            System.out.print("Do you want to quit? (y/n): ");
        } while (sc.next().charAt(0) == 'n');
        sc.close();
    }
}
