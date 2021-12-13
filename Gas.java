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
    
	public Gas (int x, int y) {
		this.hp = 5;
		this.X = x;
		this.Y = y;
	}
	
    /**
     * The specific tile this gas is on and the spread distance.
     */
    private HashMap<Tile, Integer> locs = new HashMap<>();
    
    /**
     * Tiles the tile was supposed to spread to but failed.
     */
    private ArrayList<Tile> failedLocs = new ArrayList<>();
    
    public void spreadGas() {
    	if (currRadius != maxRadius) {
    		
	    	ArrayList<ArrayList<Tile>> tiles = Board.spreadGas(X, Y, this);
	    	
	    	for (Tile t : tiles.get(0)) { // Checking failed list
	    		if (!locs.containsKey(t)) {
	    			failedLocs.add(t);
	    		}
	    	}
	    	
	    	for (Tile t : tiles.get(1)) {
	    		locs.put(t, currRadius);
	    	}
	    	currRadius++;
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
