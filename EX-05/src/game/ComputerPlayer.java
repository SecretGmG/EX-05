//Sigrist Cedric 22-120-844
//Moritz Scholz 22-122-071
/* ************************************************************************* *\
*                Programmierung 1 HS 2020 - Serie 5-1                         * 
\* ************************************************************************* */
package game;
import java.util.ArrayList;
import java.util.Random;


/** A surprisingly good Computer player
 * 
 * 
 * This class implements a simple version of a min-max algorithm.
 * 
 * Most constants have been (empirically) chosen to 
 *  1) keep the runtime acceptable
 *  2) make the computer player play better moves
 *  3) make the computer player play more 'natural/good looking' moves 
 * 
 * The primary principle is to assign each position a score and where higher scores
 * represent better positions. (for the current player in this implementation)
 * 
 * Summed up: 
 * 		positive score <=> the computer thinks the current player is winning
 * 		negative score <=> the computer thinks the current player is loosing
 * 
 * The positions are recursively checked up to a depth of DEPTH meaning:
 * every possible move and their responses are checked 'DEPTH responses' deep.
 * 
 * When the maximal depth is reached the score is simply guessed.
 * In this implementation this counts how many tokens the current player has near the center column.
 * 
 * 
 * Room for improvement:
 * Keeping track of previously evaluated positions would immensely improve the runtime and would allow higher depth.
 * Improving the 'score guessing' would improve the computers move by a lot possible heuristics to include would be:
 * 		1)how many connected groups of tokens the players have
 * 		2)are these groups blocked of or not
 * 		3)how many tokens do need to be placed to possibly expand a group
 * 		4)...
 * A better score guessing would also make possible to prioritize which moves to check first potentially reducing the runtime too.
 * 
 * Despite all this I am quite happy about the moves the Computer player chooses
 * */
public class ComputerPlayer implements IPlayer
{
	private Token token;
	//The scored assigned to a game with a winning position
	private static final double WINNING_SCORE = 10;
	//Doing something sooner than later is better (decrease score with increasing depth)
	private static final double DEPTH_FACTOR = 0.95;
	private static final int DEPTH = 5;
	//How much of influence possible bad moves of the other player should have on the computers decision
	//If this number is smaller the computer player will go for more "easy wins"
	//If it is bigger only for forced wins
	private static final double OTHER_PLAYER_ACCURACY_SCALAR = 0.8;
	//The if this value is smaller the computer player will try to prevent more possible easy wins of the opponent
	private static final double MY_ACCURACY_SCALAR = 0.95;
	
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
		
		//getting the indices of all maximal scores
		ArrayList<Integer> maxIndices = new ArrayList<Integer>();
		maxIndices.add(0);
		for(int i = 1; i<scores.length; i++) {
			//if scores[i] has the same value, push to maxIndices
			if(scores[i] == scores[maxIndices.get(0)]) {
				maxIndices.add(i);
			}
			//if scores[i] is bigger, clear maxIndices and push scores[i]
			else if(scores[i]>scores[maxIndices.get(0)]) {
				maxIndices.clear();
				maxIndices.add(i);
			}
		}
		
		return getRandElement(maxIndices);
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
		
		//Don't look any deeper if the position is winning
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
		
		
		if(player == this.token) {
			return max(scores)*MY_ACCURACY_SCALAR + getFiniteAverage(scores)*(1-MY_ACCURACY_SCALAR);
		}else {
			return max(scores)*OTHER_PLAYER_ACCURACY_SCALAR + getFiniteAverage(scores)*(1-OTHER_PLAYER_ACCURACY_SCALAR);
		}
		
	}
	/** returns the average of all finite elements of an array */
	private static double getFiniteAverage(double[] arr) {
		double sum = 0;
		int elements = 0;
		for(int i = 0; i<arr.length; i++) {
			if(Double.isFinite(arr[i])){
				sum += arr[i];
				elements++;
			}
		}
		return sum/elements;
	}
	/** gets a rough estimate of how good a given move is*/
	private double guessScoreOfMove(int col, int row, Token[][] board, Token player) {
		//if the move is winning return the winning score
		if(checkForWin(col, row, board)) return WINNING_SCORE;
		//otherwise guess
		return naiveScoreGuess(col,row,board,player);
	}
	/** returns a naive estimate of how good the move was 
	 *  there is a lot of room for improvement in this function but it seems to work
	 * */
	private double naiveScoreGuess(int col, int row, Token[][] board, Token player) {
		double score = 0;
		
		//get a score indicator of every field and add them up
		for(int i = 0; i<VierGewinnt.COLS; i++) {
			for(int j = 0; j<VierGewinnt.ROWS; j++) {
				score += getScoreIndicator(i,j,board);
			}
		}
		
		//Scale the value to never (realistically) be bigger then the winning score
		final double scalar = VierGewinnt.COLS * VierGewinnt.ROWS/4;
		return score * getFactor(player) / scalar * WINNING_SCORE;
	}
	/** gets a very rough estimate of how good this position on the board is
	 * 	in respect to a certain index i,j
	 *  this estimate is always between 0 and 1
	 *  There is a lot of room for improvement in this function but it seems to work
	 * */
	private double getScoreIndicator(int i,int j, Token[][] board){
		
		Token startToken = board[i][j];
		if(startToken == Token.empty) return 0;
		double centreColumn = (VierGewinnt.COLS-1) / 2.0; //the 'index' of the center column
		double distToCentre = Math.abs(centreColumn-(double)i);
		
		//moves near the center are generally better
		double score = 	1-distToCentre/centreColumn;
		
		return score*getFactor(startToken);
	}
	private static double getFactor(Token token) {
		switch(token) {
		case player1:
			return 1;
		case player2:
			return -1;
		default:
			return 0;
		
		}
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
