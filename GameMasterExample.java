import java.util.ArrayList;
import java.util.Arrays;

public class GameMasterExample {

	/**
	 * Different maps currently available
	 */
	// private static String map = "GGGGGGGGGGGGGGGGGGPPPPJPPJPPJPPPPGGPGGGTGGPGGTGGGPGGPGGGTGGPGGTGGGPGGPGGGTGGPGGTGGGPGGJPPJJPPJPPJJPPJGGPGGTGGGPGGGTGGPGGPGGTGGGPGGGTGGPGGPGGTGGGPGGGTGGPGGPPPJPPPJPPPJPPPGGGGGGGGGGGGGGGGGG";
	   private static String map = "GGGGGGGGGGGGGGGGGGPPPPPPPJPPPPPPPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGJPPPPPPJPPPPPPJGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPPPPPPPJPPPPPPPGGGGGGGGGGGGGGGGGG";
	// private static String map = "GGGGGGGGGGGGGGGGGGPPPPPPPJPPPPPPPGGPGGGGGGPGGGGGGPGGJTTTTTTJTTTTTTJGGPGGGGGGPGGGGGGPGGJPPPPPPJPPPPPPJGGPGGGGGGPGGGGGGPGGJTTTTTTJTTTTTTJGGPGGGGGGPGGGGGGPGGPPPPPPPJPPPPPPPGGGGGGGGGGGGGGGGGG";
	
	/**
	 * List of position of rats
	 */
	private static ArrayList<String> rats = new ArrayList<> (Arrays.asList( 
			// Opposite gender rats going at each other 
			"M;3,1,2" // Should only be on very new levels i.e starting maps
			
			,"F;3,5,5"
			,"M;3,9,9"
			,"F;1,5,12"
			,"M;3,1,2"
			
			
//			"50,true,false,20,false,false,false;1,1,3"
//			,"50,false,false,20,false,false,false;2,1,5"
//			"D;3,1,7",
			
			//Added more rats
//			"50,true,false,20,false,false,false;3,1,2",
//			,"50,false,false,20,false,false,false;3,9,9"
//			,"50,false,false,20,false,false,false;2,8,15"
//			,"50,true,false,20,false,false,false;0,9,15"
			));
	
	/**
	 * List of positions of items
	 */
	private static ArrayList<String> items = new ArrayList<> (Arrays.asList(
//			"StopSign,5;1,1"
//			,"Gas,16;5,1"
//			,"Gas,16;5,2"
			));
	
	/**
	 * Max time to finish game before it ends.
	 */
	private static int maxTime = 60;
	
	/**
	 * Max number of rats before game ends.
	 */
	private static int maxRats = 50;
	
	/**
	 * Name of player
	 */
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
