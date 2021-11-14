/**
 * Test class for Map.java
 * @author 2010573
 *
 */
public class MainMapTester {

	static String map1 = "GGGGGPPGGGGG"; // 4 3
	public static void main(String[] args) {
		print2dArray(create2dArray(map1, 4, 3));
		
		//How to show graph??
	}
	
	public static Tile[][] create2dArray(String map, int x, int y) {
		Map m = new Map(map, x, y);
		return m.getBoard();
	}
	
	public static void print2dArray(Tile[][] tile) {
		for (Tile[] tileY : tile) {
			for (Tile tileX : tileY) {
				if (tileX instanceof PathTile) {
					System.out.print("P");
				} else if (tileX instanceof JunctionTile) {
					System.out.print("B");
				} else {
					System.out.print("G");
				}
			}
			System.out.println();
		}
	}
}
