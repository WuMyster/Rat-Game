import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;

/**
 * Test class for Board.java. Use for test only.
 * 
 * @author Jing Shiang Gu deprecated
 */
public class MainBoardTester {

	// static String map1 = "GGGGGPPGGGGG"; // 4 3

	// 31 16
	// static String map2 =
	// "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGPPPPPPJPPPPPPPGGGGPPPPPPPPPPPGGPGGGGGPGGGGGGPGGGGPGGGGGGGGGPGGPGGGGGPGGGGGGPGGGGPGGGGGGGGGPGGPGGGGGPGGGGGGPPPPPJGGGGGGGGGPGGJPPPGGPGGGGGGGGGGGPGGGGGGGGGPGGPGGPGGPGGGGGGGGGGGPGGGGGGGGGPGGPGGPGGPGGGGPPPPPPPJPPPPPPPPPJGGPPPPPPPGGGGPGGGGGGPGGGGGGGGGPGGPGGGGGGGGGGPGGGGGGPGGGGGGGGGPGGPGGGGGGGGGGPGGGGGGPGGGGGGGGGPGGPPPPPPPPPPPPGGGGGGPPPPPPPPPPPG";

	// 17 11
	static String properMap1 = "GGGGGGGGGGGGGGGGGGPPPPPPPJPPPPPPPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGJPPPPPPJPPPPPPJGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPPPPPPPJPPPPPPPGGGGGGGGGGGGGGGGGG";
	public static Output BBOAD;

	public static void main(String[] args) {
		Board m = new Board(properMap1, 17, 11);
		//print2dArray(m.getBoard());
		//System.out.println();
		//m.eliminateEmpties();
		//print2dArray(m.getBoard());
		System.out.println("Done!");
	}

	public static TileType[][] create2dArray(String map, int x, int y) {
		Board m = new Board(map, x, y);
		//m.eliminateBadInvisTiles();
		return m.getBoard();
	}

	public static void print2dArray(TileType[][] tile) {
		for (TileType[] tileY : tile) {
			for (TileType tileX : tileY) {
				if (tileX instanceof PathTile) {
					System.out.print("P");
				} else if (tileX instanceof JunctionTile) {
					System.out.print("J");
				} else if (tileX instanceof LightTile) {
					System.out.print("L");
				} else {
					System.out.print("G");
				}
			}	
			//else if (tileX instanceof LightTile) {
			//System.out.print("L");					
		//} 
			System.out.println();
		}
	}
}
