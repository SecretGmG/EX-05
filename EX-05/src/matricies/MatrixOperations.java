package matricies;

import java.io.File;
import java.io.FileNotFoundException;

/** implements the matrix operations on int[][] */
public class MatrixOperations {
	
	
	/** reads a matrix from a file */
	static int[][] readMatrix(String path){
		try {
			return Matrix.fromFile(new File(path)).getData();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
			System.out.println("Unreachable code was reached, Something went wrong");
			return null;
		}
	}
	
	/** calculates the product of two matrices */
	static int[][] product(int[][] arrA, int[][] arrB) {
		
		Matrix product = Matrix.product(new Matrix(arrA), new Matrix(arrB));
		if(product == null) return null;
		return product.getData();
	}
	
	/** calculates the Transposition of a matrix
	 *  gives an error message and returns null if the matrix is not square
	 */
	static int[][] transpose(int[][] arrA){
		Matrix A = new Matrix(arrA);
		if(!A.isSquare()) {
			System.out.println("This matrix is not square, null was returned");
			return null;
		}
		return A.transposed().getData();
	}
	
	/** prints a matrix */
	static void printMatrix(int[][] arrA) {
		if(arrA == null) System.out.println("null");
		else System.out.println(new Matrix(arrA));
	}
	
}
