package matricies;

import java.io.File;
import java.io.FileNotFoundException;

import myIO.Read;



public class Matrix {
	private int[][] data;
	
	
	public Matrix(int m, int n) {
		this(new int[m][n]);
	}
	
	public Matrix(int[][] data) {
		this.data = data;
	}
	
	/* Getters and setters */
	
	private int rows() {
		return data.length;
	}
	private int collumns() {
		return data[0].length;
	}
	public int[][] getData(){
		return this.data;
	}
	/**
	 * Checks if a matrix is square
	 */
	public boolean isSquare() {
		return rows() == collumns();
	}
	/** sets the value at the given indices */
	public void set(int i, int j, int value) {
		this.data[i][j] = value;
	}
	/** gets tha value from the given indices */
	public int get(int i, int j) {
		return data[i][j];
	}
		
	/**
	 * Transposes a matrix
	 * @param A the input (square) matrix
	 * @return the transposed matrix
	 */
	public Matrix transposed(){
		
		Matrix transposedMatrix = new Matrix(collumns(), rows());
		
		for(int i = 0; i<collumns(); i++) {
			for(int j = 0; j<rows(); j++) {
				transposedMatrix.set(i, j, get(j,i));
			}
		}
		
		return transposedMatrix;
	}
	
	/**
	 * multiplies two matrices
	 * 
	 * @param A a matrix of size nxm
	 * @param B a matrix of size mxl
	 * @return A*B / null if the side-length m does not match
	 */
	public static Matrix product(Matrix A, Matrix B){
		
		
		if(A.collumns() != B.rows()) {
			System.out.println("the sizes of the matrices do not match! null was returned");
			return null;
		}
		int iterator = A.collumns();
		Matrix C = new Matrix(A.rows(), B.collumns());
		
		for(int i = 0; i<A.rows(); i++) {
			for(int j = 0; j<B.collumns(); j++) {
				for(int k = 0; k<iterator; k++) {
					int val = C.get(i, j);
					val += A.get(i, k)*B.get(k, j);
					C.set(i, j, val);
				}
			}
		}
		return C;
	}
	/** returns a string representation of the matrix */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i<rows(); i++) {
			for(int j = 0; j<collumns(); j++) {
				//give some padding and add a whitespace
				sb.append(String.format("%3d ",get(i,j))); 
			}
			sb.append('\n');
		}
	return sb.toString();
	}
	/**
	 * @presumes the format is valid
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Matrix fromFile(File file) throws FileNotFoundException{
		
		String[] lines = Read.readLines(file);
		
		int m = lines.length;
		int n = lines[0].split(" ").length;
		
		Matrix matrix = new Matrix(m,n);
		
		for(int i = 0; i<m; i++) {
			String[] elementsInThisLine = lines[i].split(" ");
			for(int j = 0; j<n; j++) {
			
				matrix.set(i, j, Integer.parseInt(elementsInThisLine[j]));
			}
		}
		return matrix;
	}
}
