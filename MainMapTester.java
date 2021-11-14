
public class MainMapTester {

	static String map1 = "GGGGGPPGGGGG";
	public static void main(String[] args) {
		Tile[][] f = create2dArray(map1, 4, 3);
		for (Tile[] tileY : f) {
			for (Tile tileX : tileY) {
				if (tileX instanceof PathTile) {
					System.out.print("P");
				} else if (tileX instanceof BridgeTile) {
					System.out.print("B");
				} else {
					System.out.print("G");
				}
			}
			System.out.println();
		}
		
	}
	
	public static Tile[][] create2dArray(String map, int x, int y) {
		Map m = new Map(map, x, y);
		return m.getBoard();
	}
}
