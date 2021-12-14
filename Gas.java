import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.image.Image;

public class Gas extends Item {
	
	private final int X;
	private final int Y;
	
	public static final Image IMAGE = new Image("./img/Gas.png");

    /**
     * Damage gas does to a rat.
     */
    private final int DAMAGE = 100;

    /**
     * Max amount of tiles gas spread to in each direction.
     */
    private final int maxRadius = 3;
    
    /**
     * The distance the gas has spread to so far.
     */
    private int currRadius = 0;

    private static final int GAS_EXPAND_TIME = 700; // milliseconds
	
    /**
     * The specific tile this gas is on and the spread distance.
     */
    private HashMap<int[], Integer> locs = new HashMap<>();
    
    /**
     * Tiles the tile was supposed to spread to but failed.
     */
    private ArrayList<int[]> nextLocs = new ArrayList<>();
    

	public Gas (int x, int y) {
		this.hp = 5;
		this.X = x;
		this.Y = y;
		locs.put(new int[] {x, y}, 0);
		nextLocs.add(new int[] {x, y});
	}
	
    public void spreadGas() {
    	
    	if (currRadius != maxRadius) {
    		
    		ArrayList<int[]> nextLocsBuffer = new ArrayList<>();
    		
    		for (int[] tNext : nextLocs) {
    			ArrayList<ArrayList<int[]>> tiles = Board.spreadGas(tNext[0], tNext[1], this);
    			
    			boolean redoLoc = false;
    			for (int[] tFail : tiles.get(0)) {
    				if (!locs.containsKey(tFail)) {
    					redoLoc = true;
    				}
    			}
    			if (redoLoc) {
    				nextLocsBuffer.add(tNext);
    			}
    			
    			for (int[] tSucc : tiles.get(1)) {
    	    		locs.put(tSucc, currRadius);
    	    		nextLocsBuffer.add(tSucc);
    	    	}
    		}
    		
	    	nextLocs = nextLocsBuffer;
	    	nextLocsBuffer = new ArrayList<>();
	    	
	    	
	    	
    	}
    }

	@Override
	public ArrayList<Rat> itemAction(ArrayList<Rat> r) {
		ArrayList<Rat> out = new ArrayList<>();
		if (!r.isEmpty()) {
            for (Rat rat : r) {
            	if (!rat.damageRat(DAMAGE)) {
            		out.add(rat);
            	}
            }
        }
		
		spreadGas();
		
		return out;
	}
	
	@Override
	public String toString() {
		String out = "Gas deprecated";
		return out;
	}
}
