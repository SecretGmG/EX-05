//Sigrist Cedric 22-120-844
//Moritz Scholz 22-122-071
package matricies;

import java.io.File;
import java.io.FileNotFoundException;

public class MatrixTest {

	public static final String TEST_PATH_1 = "src/testFiles/testMatrix1.txt";
	public static final String TEST_PATH_2 = "src/testFiles/testMatrix2.txt";
	public static final String TEST_PATH_3 = "src/testFiles/testMatrix3.txt";
	public static final String TEST_PATH_4 = "src/testFiles/testMatrix4.txt";

	public static void main(String[] args) {
		//testMatrix();
		testMatrixOperations();
	}

	/** runs tests with the MatrixOperations class */
	public static void testMatrixOperations() {
		int[][] A = MatrixOperations.readMatrix(TEST_PATH_1);
		int[][] B = MatrixOperations.readMatrix(TEST_PATH_2);
		int[][] C = MatrixOperations.readMatrix(TEST_PATH_3);
		int[][] D = MatrixOperations.readMatrix(TEST_PATH_4);
		
		System.out.println("A:");
		MatrixOperations.printMatrix(A);
		System.out.println("B:");
		MatrixOperations.printMatrix(B);
		System.out.println("C:");
		MatrixOperations.printMatrix(C);
		System.out.println("D:");
		MatrixOperations.printMatrix(D);

		
		System.out.println("A*B:");
		MatrixOperations.printMatrix(MatrixOperations.product(A, B));
		System.out.println("B*A:");
		MatrixOperations.printMatrix(MatrixOperations.product(B, A));
		System.out.println("B*C:");
		MatrixOperations.printMatrix(MatrixOperations.product(B, C));
		System.out.println("C*C:");
		MatrixOperations.printMatrix(MatrixOperations.product(C, C));

		System.out.println("C^T:");
		MatrixOperations.printMatrix(MatrixOperations.transpose(C));
		System.out.println("D^T:");
		MatrixOperations.printMatrix(MatrixOperations.transpose(D));
	}

	/** runs tests with the Matrix class */
	public static void testMatrix() {
		Matrix A = null, B = null, C = null, D = null;

		try {
			A = Matrix.fromFile(new File(TEST_PATH_1));
			B = Matrix.fromFile(new File(TEST_PATH_2));
			C = Matrix.fromFile(new File(TEST_PATH_3));
			D = Matrix.fromFile(new File(TEST_PATH_4));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("A: \n" + A);
		System.out.println("B: \n" + B);
		System.out.println("C: \n" + C);
		System.out.println("D: \n" + D);

		System.out.println("A*B:");
		System.out.println(Matrix.product(A, B));
		System.out.println("B*A:");
		System.out.println(Matrix.product(B, A));
		System.out.println("B*C:");
		System.out.println(Matrix.product(B, C));
		System.out.println("C*C:");
		System.out.println(Matrix.product(C, C));

		System.out.println("A^T:");
		System.out.println(C.transposed());
		System.out.println("D^T:");
		System.out.println(D.transposed());

	}

}
