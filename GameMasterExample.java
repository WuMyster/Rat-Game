import java.util.ArrayList;
import java.util.Arrays;

public class GameMasterExample {

	private static String map = "GGGGGGGGGGGGGGGGGGPPPPJPPJPPJPPPPGGPGGGTGGPGGTGGGPGGPGGGTGGPGGTGGGPGGPGGGTGGPGGTGGGPGGJPPJJPPJPPJJPPJGGPGGTGGGPGGGTGGPGGPGGTGGGPGGGTGGPGGPGGTGGGPGGGTGGPGGPPPJPPPJPPPJPPPGGGGGGGGGGGGGGGGGG";
	
	private static ArrayList<String> rats = new ArrayList<> (Arrays.asList( 
			//"M;3,1,2", // Should only be on very new levels i.e starting maps
			"50,true,false,20,false,false,false;3,1,4",
			"50,false,false,20,false,false,false;1,1,5"
			//"D;3,9,1", //  Same here
			//"D,2;1,9,1"
			));
	
	private static int maxTime = 60;
	
	private static int maxRats = 10;
	
	// Return GUI stuff to output to get information
	public static void main(String[] args) {
		
	}
	
	public static String getMap() {
		return map;
	}
	
	public static ArrayList<String> getRats() {
		return rats;
	}
	
	public static ArrayList<String> getItems() {
		return null;
	}
	
	public static int getMaxTime() {
		return maxTime;
	}
	
	public static int getMaxRats() {
		return maxRats;
	}
}
