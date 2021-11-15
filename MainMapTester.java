import javafx.application.Application;

/**
 * Test class for Map.java
 * @author 2010573
 *
 */
public class MainMapTester {

	//static String map1 = "GGGGGPPGGGGG"; // 4 3
	
	//31 16
	//static String map2 = "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGPPPPPPJPPPPPPPGGGGPPPPPPPPPPPGGPGGGGGPGGGGGGPGGGGPGGGGGGGGGPGGPGGGGGPGGGGGGPGGGGPGGGGGGGGGPGGPGGGGGPGGGGGGPPPPPJGGGGGGGGGPGGJPPPGGPGGGGGGGGGGGPGGGGGGGGGPGGPGGPGGPGGGGGGGGGGGPGGGGGGGGGPGGPGGPGGPGGGGPPPPPPPJPPPPPPPPPJGGPPPPPPPGGGGPGGGGGGPGGGGGGGGGPGGPGGGGGGGGGGPGGGGGGPGGGGGGGGGPGGPGGGGGGGGGGPGGGGGGPGGGGGGGGGPGGPPPPPPPPPPPPGGGGGGPPPPPPPPPPPG";
	
	//17 11
	static String properMap1 = "GGGGGGGGGGGGGGGGGGPPPPPPPJPPPPPPPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPPPPPPPJPPPPPPPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPPPPPPPJPPPPPPPGGGGGGGGGGGGGGGGGG";
	
	public static void main(String[] args) {
		//print2dArray(create2dArray(map1, 4, 3));
		//print2dArray(create2dArray(map2, 31, 12));
		print2dArray(create2dArray(properMap1, 17, 11));
		Output.main(null);
		//Application.launch(args);
		
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
				} else if (tileX instanceof TunnelTile) {
					System.out.print("T");
				} else {
					System.out.print("G");
				}
			}
			System.out.println();
		}
	}
}
