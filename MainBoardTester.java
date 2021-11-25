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
//		System.out.println("Creating board!");
//		Board m = new Board(properMap1, 17, 11);
//		System.out.println(m.getBoard()[2][1].neighbourTiles.get(Direction.NORTH));
//		System.out.println(m.getBoard()[1][2].neighbourTiles.get(Direction.WEST));
//		System.out.println(m.getBoard()[2][1].directions[0]);
//		System.out.println(m.getBoard()[2][1].directions[1]);
		
		String fake = "GGGGPGGPGGGG"; //3, 4
		String fake2 = "GPP";
		String fake3 = "PP";
		Board m = new Board(properMap1, 17, 11);
		print2dArray(m.getBoard());
		System.out.println();
		m.eliminateEmpties();
		print2dArray(m.getBoard());
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
