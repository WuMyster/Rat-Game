import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.image.Image;

public class Gas extends TimeItem {
	
	public static final Image IMAGE = new Image("./img/Gas.png");
	
	public static final String NAME = "Gas";

	private final int x;
	private final int y;
	
    /**
     * Damage gas does to a rat.
     */
    private final int DAMAGE = 100;

    /**
     * Number of tiles gas spread to in all possible directions.
     */
    private static final int RADIUS = 3;
    
    /**
     * Extra hp for each Gas cloud radious. TODO fix definition
     */
    private static final int EXTRA_HP = 5;
    
    /**
     * Initial hp of Gas.
     */
    private static final int START_HP = 5;

    /**
     * Time between each tick for each Gas item
     */
    private static final int GAS_EXPAND_TIME = 1000; // milliseconds
	
	/**
	 * Constructs a gas item from an xy position.
	 * 
	 * @param x 	x position of the gas item
	 * @param y 	y position of the gas item
	 */
	public Gas (int x, int y) {
		this.hp = START_HP + EXTRA_HP  * RADIUS;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Constructs a gas item from an xy position with given hp.
	 * 
	 * @param xyPos xy position of the gas
	 * @param hp 	hp of the gas item
	 */
	public Gas (int[] xyPos, int hp) {
		this.hp = hp;
		this.x = xyPos[0];
		this.y = xyPos[1];
	}
	
	/**
	 * Constructs a gas item from a x y position with a given hp.
	 * @param x 	x position of the gas item
	 * @param y		y position of the gas item
	 * @param hp	hp of the gas item
	 */
	public Gas (int x, int y, int hp) {
		this.hp = hp;
		this.x = x;
		this.y = y;
	}
    
	/**
	 * Method to deal with spreading and clearing of this gas item.
	 */
    public void itemAction() {
    	this.timer = new Timer();
    	
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				hp--;
				
				if (hp == 0) { // Clear gas
					timer.cancel();
					Board.clearGas(x, y);
				} else if (hp % EXTRA_HP == 0 && hp != START_HP) {
					Board.spreadGas(x, y, hp);    
				}
			}
		}, 0, GAS_EXPAND_TIME);
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
