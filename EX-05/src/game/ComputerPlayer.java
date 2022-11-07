/* ************************************************************************* *\
*                Programmierung 1 HS 2020 - Serie 5-1                         * 
\* ************************************************************************* */
package game;
import java.util.ArrayList;
import java.util.Random;

/** A very stupid computer player */
public class ComputerPlayer implements IPlayer
{
	private Token token;
	private static final double WINNING_SCORE = 1;
	//Doing something sooner than later is better (decrease score with increasing depth)
	private static final double DEPTH_FACTOR = 0.95;
	private static final int DEPTH = 5;
	
	public int getNextColumn( Token[][] board )
	{
		return getBestMove(board);
	}
	/**
	 * gets the scores of every move possible moves and then selects the one
	 * with the highest value
	 * 
	 * @param board the current position of the game
	 * @return the index of a column with a reasonably good move
	 */
	public int getBestMove(Token[][] board) {
		double[] scores = getScoresOfMoves(board, this.token, DEPTH);
		ArrayList<Integer> maxIndecies = new ArrayList<Integer>();
		maxIndecies.add(0);
		for(int i = 1; i<scores.length; i++) {
			if(scores[i] == scores[maxIndecies.get(0)]) {
				maxIndecies.add(i);
			}
			else if(scores[i]>scores[maxIndecies.get(0)]) {
				maxIndecies.clear();
				maxIndecies.add(i);
			}
		}
		
		return getRandElement(maxIndecies);
	}
	/** gets a random element of an integer arrayList */
	private static int getRandElement(ArrayList<Integer> arrayList){
		Random rand = new Random();
		return arrayList.get(rand.nextInt(arrayList.size()));
	}
	/**
	 * calculates the score of every move.
	 * illegal moves are scored NEGATIVE_INFINITY
	 * @param board the current position of the game
	 * @param player the player of the current torn
	 * @param depth how many moves deep the moves should be checked
	 * @return a double[] of the scores of every move
	 */
	private double[] getScoresOfMoves(Token[][] board, Token player, int depth) {
		double[] scores = new double[VierGewinnt.COLS];
		for(int i = 0; i<VierGewinnt.COLS; i++) {
			scores[i] = getScoreOfMove(board, i, player, depth);
		}
		return scores;
	}
	/**
	 * calculates the score of a move.
	 * illegal moves are scored NEGATIVE_INFINITY
	 * @param board the current position of the game
	 * @param col the index of a column representing the move to check
	 * @param player the player of the current turn
	 * @param depth how many moves deep the moves should be checked
	 * @return a double representing how good this move is for the current player
	 */
	private double getScoreOfMove(Token[][] board, int col, Token player, int depth) {
		if(isColFull(col, board)) {
			//Illegal moves are very bad!
			return Double.NEGATIVE_INFINITY;
		}
		
		Token[][] newBoard = getCopyOfBoard(board);
		int row = makeMoveUnchecked(newBoard, col, player);
		
		double guess = guessScoreOfMove(col, row, newBoard, player);
		
		if(depth == 0 || Math.abs(guess)>=WINNING_SCORE) {
			return guess;
		}
		
		return -getScoreOfBoard(newBoard, other(player), depth - 1)*DEPTH_FACTOR;
	}
	/**
	 * 
	 * @param board board the current position of the game
	 * @param player the player of the current turn
	 * @param depth how many moves deep the moves should be checked
	 * @return a double representing how good this position is for the current player
	 */
	private double getScoreOfBoard(Token[][] board, Token player, int depth) {
		double[] scores = getScoresOfMoves(board, player, depth);
		return max(scores);
	}
	private double guessScoreOfMove(int col, int row, Token[][] board, Token player) {
		if(checkForWin(col, row, board)) return WINNING_SCORE;
		return naiveScoreGuess(col,row,board,player);
	}
	/** returns a naive guess of how good the move was */
	private double naiveScoreGuess(int col, int row, Token[][] board, Token player) {
		return 0; //here is a lot of room for improvement!
	}
	/**
	 * Checks for at least four equal tokens in a row in either direction, starting
	 * from the given position.
	 */
	private boolean checkForWin(int col, int row, Token[][] board) {
		Token tok = board[col][row];
		int hStreak = checkDir(col,row,-1,0, tok, board)+checkDir(col,row,1,0, tok, board)+1;
		int vStreak = checkDir(col,row,0,-1, tok, board)+checkDir(col,row,0,1, tok, board)+1;
		int d1Streak = checkDir(col,row,-1,-1, tok, board)+checkDir(col,row,1,1, tok, board)+1;
		int d2Streak = checkDir(col,row,1,-1, tok, board)+checkDir(col,row,-1,1, tok, board)+1;
		return 
			hStreak >= VierGewinnt.WINNING_CONDITION || 
			vStreak >= VierGewinnt.WINNING_CONDITION || 
			d1Streak >= VierGewinnt.WINNING_CONDITION || 
			d2Streak >= VierGewinnt.WINNING_CONDITION;
	}
	
	/** checks if the immediate neighbors to a position fulfill the winning conditions in a given direction */
	private int checkDir(int col, int row, int colDir, int rowDir, Token startTok, Token[][] board){		
		int streak = 0;
		for(int i = 1; i<VierGewinnt.WINNING_CONDITION; i++) {
			int colIndex = col + i * colDir;
			int rowIndex = row + i * rowDir;
			
			if(VierGewinnt.isOutOfBounds(colIndex, rowIndex)) return streak;
			if(board[colIndex][rowIndex] != startTok) return streak;
			streak++;
		}
		
		return streak;
	}
	/**
	 * @param the current player
	 * @return the other/next player
	 */
	private Token other(Token player) {
		switch(player) {
		case player1:
			return Token.player2;
		case player2:
			return Token.player1;
		default:
			return Token.empty;
		}
	}
	/**
	 * returns the biggest double in the double[]
	 * @param arr an array of doubles
	 * @return the biggest value
	 */
	private static double max(double[] arr) {
		double max = Double.NEGATIVE_INFINITY;
		for(int i =0; i<arr.length; i++) {
			max = Math.max(arr[i], max);
		}
		return max;
	}
	/** Returns a (deep) copy of the board array */
	private Token[][] getCopyOfBoard(Token[][] board) {
		Token[][] copiedBoard = new Token[VierGewinnt.COLS][VierGewinnt.ROWS];
		for (int i = 0; i < copiedBoard.length; i++) {
			for (int j = 0; j < copiedBoard[i].length; j++) {
				copiedBoard[i][j] = board[i][j];
			}
		}
		return copiedBoard;
	}
	/**
	 * makes a move and does NOT check if it is legal / possible
	 * @param board the current position (will be mutated)
	 * @param col the move to make
	 * @param player the player making the move
	 */
	private int makeMoveUnchecked(Token[][] board, int col, Token player) {
		for(int i = 0; i<VierGewinnt.ROWS; i++) {
			if(board[col][i] == Token.empty) {
				board[col][i] = player;
				return i;
			}
		}
		System.out.println("Unreachable code was reached, something went wrong!");
		return -1;
	}
	/**
	 * @return true if the column col is already full and false otherwise. 
	 */
	private boolean isColFull( int col, Token[][] board )
	{
		int topRow = board[ col ].length - 1;
		return ( board[ col ][ topRow ] != Token.empty );
	}


	public void setToken( Token token )
	{
		this.token = token;
	}

	public Token getToken()
	{
		return this.token;
	}

	public String getPlayersName()
	{
		return "Random Player";
	}
}
