
/* ************************************************************************* *\
*                Programmierung 1 HS 2020 - Serie 5-1                         * 
\* ************************************************************************* */
package game;

import java.util.Arrays;
import java.util.Scanner;

public class VierGewinnt {

	public static final int COLS = 7;
	public static final int ROWS = 6;

	public static final int WINNING_CONDITION = 4;

	private Token[][] board = new Token[COLS][ROWS]; // 7 columns with 6 fields each
	private IPlayer[] players = new IPlayer[2]; //  two players

	/** initialize board and players and start the game */
	public void play() {
		// initialize the board
		for (Token[] column : this.board) {
			Arrays.fill(column, Token.empty);
		}

		/* initialize players */
		players[0] = new HumanPlayer();
		System.out.print("Play against a human opponent? (y / n) ");
		String opponent = new Scanner(System.in).nextLine().toLowerCase();
		while ((1 != opponent.length()) || (-1 == ("yn".indexOf(opponent)))) {
			System.out.print("Can't understand your answer. Play against a human opponent? (y / n) ");
			opponent = new Scanner(System.in).nextLine().toLowerCase();
		}
		if (opponent.equals("y")) {
			players[1] = new HumanPlayer();
		} else {
			players[1] = new ComputerPlayer();
		}
		players[0].setToken(Token.player1);
		players[1].setToken(Token.player2);

		/* play... */
		boolean solved = false;
		int currentPlayer = (new java.util.Random()).nextInt(2); // choose randomly who begins
		System.out.println("current player: " + currentPlayer);
		int insertCol, insertRow; // starting from 0
		while (!solved && !this.isBoardFull()) {
			// get player's next "move"
			// note that we pass only a copy of the board as an argument,
			// otherwise the player would be able to manipulate the board and cheat!
			insertCol = players[currentPlayer].getNextColumn(getCopyOfBoard());
			// insert the token and get the row where it landed
			insertRow = this.insertToken(insertCol, players[currentPlayer].getToken());
			//  check if the game is over
			solved = this.checkVierGewinnt(insertCol, insertRow);
			// switch to other player
			if (!solved)
				currentPlayer = (currentPlayer + 1) % 2;
		}
		System.out.println(displayBoard(this.board));
		if (solved)
			System.out.println("Player " + players[currentPlayer].getToken() + " wins!");
		else
			System.out.println("Draw! Game over.");
	}

	/**
	 * Inserts the token at the specified column (if possible)
	 * 
	 * @param column the column to insert the token
	 * @param token  the players token
	 * @return the row where the token landed
	 */
	private int insertToken(int column, Token tok) {
		// react appropriately if the move is illegal
		if (column < 0 || column >= VierGewinnt.COLS)
			printErrAndExit();

		Token[] columnArr = this.board[column];

		for (int i = 0; i < columnArr.length; i++) {
			if (columnArr[i] == Token.empty) {
				columnArr[i] = tok;
				return i;
			}
		}
		// if no empty row was found the row must be full!
		printErrAndExit();
		return -1;
	}

	/** prints a simple error message and exits the program */
	private static void printErrAndExit() {
		System.out.println("This move is illegal!");
		System.exit(1);
	}

	/**
	 * Checks if every position is occupied
	 * 
	 * @returns true, iff the board is full.
	 * 
	 */
	private boolean isBoardFull() {
		// I could also assume that the board is in a legal state and only check the
		// topmost rows
		// But the documentation of this method implies to test every cell

		for (int i = 0; i < VierGewinnt.COLS; i++) {
			for (int j = 0; j < VierGewinnt.ROWS; j++) {
				if (this.board[i][j] == Token.empty) {
					return false;
				}
			}
		}

		// if no empty cell was found return true
		return true;
	}

	/**
	 * Checks for at least four equal tokens in a row in either direction, starting
	 * from the given position.
	 */
	private boolean checkVierGewinnt(int col, int row) {
		
		int hStreak = checkDir(col,row,-1,0)+checkDir(col,row,1,0)+1;
		int vStreak = checkDir(col,row,0,-1)+checkDir(col,row,0,1)+1;
		int d1Streak = checkDir(col,row,-1,-1)+checkDir(col,row,1,1)+1;
		int d2Streak = checkDir(col,row,1,-1)+checkDir(col,row,-1,1)+1;
		return hStreak >= WINNING_CONDITION || vStreak >= WINNING_CONDITION || d1Streak >= WINNING_CONDITION || d2Streak >= WINNING_CONDITION;
	}
	
	/** checks if the immediate neighbors to a position fulfill the winning conditions in a given direction */
	private int checkDir(int col, int row, int colDir, int rowDir){		
		Token startTok = this.board[col][row];
		
		int streak = 0;
		for(int i = 1; i<WINNING_CONDITION; i++) {
			int colIndex = col + i * colDir;
			int rowIndex = row + i * rowDir;
			
			if(isOutOfBounds(colIndex, rowIndex)) return streak;
			if(board[colIndex][rowIndex] != startTok) return streak;
			streak++;
		}
		
		return streak;
	}
	public static boolean isOutOfBounds(int col, int row) {
		if(col < 0 || col >= VierGewinnt.COLS) return true;
		if(row < 0 || row >= VierGewinnt.ROWS) return true;
		return false;
	}

	

	/** Returns a (deep) copy of the board array */
	private Token[][] getCopyOfBoard() {
		Token[][] copiedBoard = new Token[COLS][ROWS];
		for (int i = 0; i < copiedBoard.length; i++) {
			for (int j = 0; j < copiedBoard[i].length; j++) {
				copiedBoard[i][j] = this.board[i][j];
			}
		}
		return copiedBoard;
	}

	/**
	 * returns a graphical representation of the board
	 */

	public static String displayBoard(Token[][] myBoard) {
		String rowDelimiter = "+";
		String rowNumbering = " ";
		for (int col = 0; col < myBoard.length; col++) {
			rowDelimiter += "---+";
			rowNumbering += " " + (col + 1) + "  ";
		}
		rowDelimiter += "\n";

		String rowStr;
		String presentation = rowDelimiter;
		for (int row = myBoard[0].length - 1; row >= 0; row--) {
			rowStr = "| ";
			for (int col = 0; col < myBoard.length; col++) {
				rowStr += myBoard[col][row].toString() + " | ";
			}
			presentation += rowStr + "\n" + rowDelimiter;
		}
		presentation += rowNumbering;
		return presentation;
	}

	/** main method, starts the program */
	public static void main(String args[]) {
		VierGewinnt game = new VierGewinnt();
		game.play();
	}
}
