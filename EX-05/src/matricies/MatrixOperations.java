package matricies;

public class MatrixOperations {
	public static int[][] readMatrix(String Path){
		//TODO
		return null;
	}
	
	
	/**
	 * Transposes a square matrix
	 * @param A the input (square) matrix
	 * @return the transposed matrix / null if the input is not square
	 */
	public static int[][] transpose(int[][] A){
		if(A[0].length != A.length) {
			System.out.println("The input matrix was not square!");
			return null;
		}
		int n = A.length;
		
		int[][] B = new int[n][n];
		
		for(int i = 0; i<n; i++) {
			for(int j = 0; j<n; j++) {
				B[i][j] = A[j][i];
			}
		}
		
		return B;
	}
	
	
	/**
	 * multiplies two matrices
	 * 
	 * @param A a matrix of size nxm
	 * @param B a matrix of size mxl
	 * @return A*B / null if the side-length m does not match
	 */
	public static int[][] multiply(int[][] A, int[][] B){
		
		
		if(A[0].length != B.length) {
			System.out.println("the sizes of the matrices do not match! null was returned");
			return null;
		}
		int n = A.length;
		int m = A[0].length;
		int l = B[0].length;
		int[][] C = new int[n][l];
		
		for(int i = 0; i<n; i++) {
			for(int j = 0; j<l; j++) {
				for(int k = 0; k<m; k++) {
					C[i][j]+=A[i][k]*B[k][j];
				}
			}
		}
		return C;		
	}
}
