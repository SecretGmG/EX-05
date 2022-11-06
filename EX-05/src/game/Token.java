/* ************************************************************************* *\
*                Programmierung 1 HS 2020 - Serie 5-1                         * 
\* ************************************************************************* */
package game;


/**
 * Basis enum to provide player tokens
 */
public enum Token {
       empty( " " ),
       player1( "O" ),
       player2( "X" );
       private String representation; // string representation of value
       Token( String s ) { this.representation = s; }
       public String toString() { return this.representation; }
}
