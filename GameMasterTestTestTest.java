import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GameMasterTestTestTest {

	/*
	 * For now only read:
	 * x, y [where x is width of map, y is height of map]
	 * [String of Map]
	 * [Number of rats to add in]
	 * [x start position, y start position, z direction it is facing???]
	 */
	
	public GameMasterTestTestTest(String filename) {

		File inputFile = new File(filename);
		Scanner in = null;
		try {
			in = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println(in.next());
		in.close();

	}
}
