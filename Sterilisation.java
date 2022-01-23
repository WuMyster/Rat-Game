import java.util.ArrayList;
import java.util.TimerTask;

import javafx.scene.image.Image;

/**
 * Class modelling a Sterilisation item.
 * @author Andrew Wu
 */
public class Sterilisation extends TimeItem {

	/**
	 * Name of the item.
	 */
	public static final String NAME = "Steralise";
	
	/**
	 * Icon of this image.
	 */
	public static final Image IMAGE = new Image(Main.IMAGE_FILE_LOC + "/Sterilise.png");
	
	/**
	 * Radius where the Sterile item will affect.
	 */
	private final static int RANGE = 3;
	
	/**
	 * Time in seconds before item takes affect.
	 */
	private final static int START_HP = 5;
	
	/**
	 * x position of the item.
	 */
	private final int x;
	
	/**
	 * y position of the item.
	 */
	private final int y;
	
    /**
     * Sets x y position and health point of item.
     * @param x 	x position of the item
     * @param y		y position of the item
     */
    public Sterilisation(int x, int y) {
    	this.x = x;
    	this.y = y;
        hp = START_HP;
    }
    
    /**
	 * Constructor for Sterilisation object setting hp of item.
	 * 
	 * @param x 	x position of item
	 * @param y 	y position of item
	 * @param hp	hp of item
	 */
    public Sterilisation(int[] xyPos, int hp) {
    	this.x = xyPos[0];
    	this.y = xyPos[1];
    	this.hp = hp;
    }

    /**
     * Does not directly interact with rats. 
     * @param r 	list of rats
     * @return 		same list of rats
     */
	@Override
	public ArrayList<Rat> itemAction(ArrayList<Rat> r) {
		// Does nothing directly to rats
		return r;
	}

	@Override
	public void itemAction() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (stopTimer) {
					timer.cancel();
				} 
				hp--;
				if (hp == 0) {
					Board.steraliseRats(y, x, RANGE);
					timer.cancel();
				}
			}
		}, 0, 1000);
	}
	
	@Override
	public String toString() {
		String out = NAME + Main.FILE_SUB_SEPERATOR + hp;
		return out;
	}
}
