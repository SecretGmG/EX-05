package myIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Read {
	/**
	 * reads all the lines from a file and returns them
	 * 
	 * @throws FileNotFoundException
	 */
	public static String[] readLines(File file) throws FileNotFoundException {
		return readLines(new Scanner(file));
	}

	/** reads all the lines from a scanner and returns them */
	public static String[] readLines(Scanner sc) {
		ArrayList<String> lines = new ArrayList<String>();
		while (sc.hasNextLine()) {
			lines.add(sc.nextLine());
		}
		
		String[] arr = new String[lines.size()];
		arr = lines.toArray(arr);
		
		return arr;
	}

}
