/* ************************************************************************* *\
*                Programmierung 1 HS 2020 - Serie 5-1                         * 
\* ************************************************************************* */
package game;

/** Interface for VierGewinnt players */
public interface IPlayer
{

	/**
	 * @return the name(s) of the programmer(s) or a team name.
	 */
	public String getPlayersName();

	/**
	 * @presumes the board is not full 
	 * @return the next move (number of column, starting from 0) of the player 
	 */
	public int getNextColumn( Token[][] board );

	/**
	 * Assign the player a token (e.g. 'X' or 'O') 
	 * @param token the Token the player should use
	 */
	public void setToken( Token token );

	/**
	 * @return the token assigned to the player or null, if not yet set.
	 */
	public Token getToken();
}
