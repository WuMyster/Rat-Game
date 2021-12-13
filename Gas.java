import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.image.Image;

public class Gas extends Item {
	
	public static final Image IMAGE = new Image("./img/Gas.png");
    /*
    Set on tile
    itemAction()
    have a radius
    one step, set tiles in all directions once
    when its repeated for radius number, stop and disappear after a while
     */

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
    private HashMap<Tile, Integer> locs = new HashMap<>();
    
    /**
     * Tiles the tile was supposed to spread to but failed.
     */
    private ArrayList<Tile> failedLocs = new ArrayList<>();
    
    public void spreadGas() {
    	
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
		
		// Now attempt to spread if needed
		
		return out;
	}
	
	@Override
	public String toString() {
		String out = "Gas deprecated";
		return out;
	}
}
