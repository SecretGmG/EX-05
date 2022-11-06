/* ************************************************************************* *\
*                Programmierung 1 HS 2020 - Serie 5-1                         * 
\* ************************************************************************* */
package game;

/** A very stupid computer player */
public class ComputerPlayer implements IPlayer
{
	private Token token;

	/**
	 * Strategy is to chose a random column and select 
	 * the next valid column to the right (the chosen included)
	 */
	public int getNextColumn( Token[][] board )
	{
		java.util.Random generator = new java.util.Random();
		int col = generator.nextInt( board.length );
		int step = 0;
		while ( isColFull( col, board ) ) {
			col = ( col + 1 ) % board.length;
		}
		return col;
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
