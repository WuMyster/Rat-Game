import java.util.ArrayList;
import java.util.Arrays;

public class GameMasterExample {

	private static String map = "GGGGGGGGGGGGGGGGGGPPPPJPPJPPJPPPPGGPGGGTGGPGGTGGGPGGPGGGTGGPGGTGGGPGGPGGGTGGPGGTGGGPGGJPPJJPPJPPJJPPJGGPGGTGGGPGGGTGGPGGPGGTGGGPGGGTGGPGGPGGTGGGPGGGTGGPGGPPPJPPPJPPPJPPPGGGGGGGGGGGGGGGGGG";
	// private static String map = "GGGGGGGGGGGGGGGGGGPPPPPPPJPPPPPPPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGJPPPPPPJPPPPPPJGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPPPPPPPJPPPPPPPGGGGGGGGGGGGGGGGGG";
	// private static String map = "GGGGGGGGGGGGGGGGGGPPPPPPPJPPPPPPPGGPGGGGGGPGGGGGGPGGJTTTTTTJTTTTTTJGGPGGGGGGPGGGGGGPGGJPPPPPPJPPPPPPJGGPGGGGGGPGGGGGGPGGJTTTTTTJTTTTTTJGGPGGGGGGPGGGGGGPGGPPPPPPPJPPPPPPPGGGGGGGGGGGGGGGGGG";
	
	private static ArrayList<String> rats = new ArrayList<> (Arrays.asList( 
			//"M;3,1,2", // Should only be on very new levels i.e starting maps
			"50,true,false,20,false,false,false;3,1,4",
			"50,false,false,20,false,false,false;1,1,5"
			//"D;3,9,1", //  Same here
			//"D,2;1,9,1"
			));
	
	private static ArrayList<String> items = new ArrayList<> (Arrays.asList(
			"StopSign,1;2,2"
			));
	
	private static int maxTime = 60;
	
	private static int maxRats = 10;
	
	private static String playerName = "Bob";
	
	// Return GUI stuff to output to get information
	public void main(String[] args) {
		
	}
	
	/**
	 * Return map of the game.
	 */
	public static String getMap() {
		return map;
	}
	
	/**
	 * Array list returning the rats.
	 */
	
	public static ArrayList<String> getRats() {
		return rats;
	}
	
	/**
	 * Array list returning the items.
	 */
	
	public static ArrayList<String> getItems() {
		return items;
	}
	
	/**
	 * Return maxTime of the game.
	 */
	
	public static int getMaxTime() {
		return maxTime;
	}
	
	/**
	 * Return maxRats of the game.
	 */
	
	public static int getMaxRats() {
		return maxRats;
	}
	
	/**
	 * Return every playerName.
	 */
	
	public static String getName() {
		return playerName;
	}
}
