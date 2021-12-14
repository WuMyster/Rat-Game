import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.image.Image;

public class Gas extends TimeItem {
	
	public static final Image IMAGE = new Image("./img/Gas.png");
	
	public static final String NAME = "Gas";

    /**
     * Damage gas does to a rat.
     */
    private final int DAMAGE = 100;

    /**
     * Max amount of tiles gas spread to in each direction.
     */
    private static final int MAX_RADIUS = 3;
    
    private static final int START_HP = 15;

    private static final int GAS_EXPAND_TIME = 700; // milliseconds
	
    /**
     * The specific tile this gas is on and the spread distance.
     */
    private HashMap<int[], Integer> locs = new HashMap<>();
    
    /**
     * Tiles the tile was supposed to spread to but failed.
     */
    private ArrayList<int[]> nextLocs = new ArrayList<>();    
    
    private boolean tickStart = true;

	public Gas (int x, int y) {
		this.hp = START_HP + MAX_RADIUS;
		int[] tmp = new int[] {x, y};
		locs.put(tmp, 0);
		nextLocs.add(tmp);
	}
	
    public void spreadGas() {
    		
		ArrayList<int[]> nextLocsBuffer = new ArrayList<>();
		if (nextLocs.isEmpty()) {
			nextLocsBuffer.add(new ArrayList<int[]>(locs.keySet()).get(0));
		} else {
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
		    		locs.put(tSucc, locs.get(tNext) + 1);
		    		nextLocsBuffer.add(tSucc);
		    	}
			}
		}
    	nextLocs = nextLocsBuffer;
    	nextLocsBuffer = new ArrayList<>();
    	
    }
    
    public void itemAction() {
    	if (tickStart) {
    		tickStart = false;
			this.timer = new Timer();
	
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					hp--;
					if (hp > START_HP) { // Growing
						spreadGas();
					} else if (hp > 0) { //Shrinking
						ArrayList<int[]> del = new ArrayList<>();
						for (int[] t : locs.keySet()) {
							if (locs.get(t) == hp / (START_HP / (MAX_RADIUS - 1)) + 1) {
								del.add(t);
							}
						}
						Board.clearGas(del);
					} else {
						timer.cancel();
						Board.clearGas(new ArrayList<int[]>(locs.keySet()));
					}
				}
			}, 0, 1000);
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
		return out;
	}
	
	@Override
	public String toString() {
		String out = NAME + "," + hp;
		return out;
	}
}
