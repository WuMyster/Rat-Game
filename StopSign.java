import java.util.ArrayList;

import javafx.scene.image.Image;

/**
 * An {@code Item} that prevents rats from entering the tile this item is on.
 * @author Jing
 *
 */
public class StopSign extends Item {
	/**
	 * The name of the item
	 */
	public static final String NAME = "StopSign";
	
	/**
	 * Max number of images - 1.
	 */
	public static final int MAX_STATES = 2;
	
	/**
	 * Start health of all Stop Signs.
	 */
	private static final int START_HP = 9;
	
	/**
	 * Number to show the different stages of the Stop Sign
	 * breaking down.
	 */
	private static final int DIVIDER = 3;

	/**
	 * Images of the different states of the stop sign.
	 * TODO change divider so all images are used
	 */
	private static final Image[] STATES = new Image[] {
			new Image("/img/StopSign0.png"),
			new Image("/img/StopSign1.png"),
			new Image("/img/StopSign2.png")
	};
	
	/**
	 * XY position of the stop sign.
	 */
	private final int[] xyPos;
	
	/**
	 * Last state the Stop Sign was.
	 */
	private int currState;
	
	/**
	 * Constructs a {@code StopSign} which prevents rats from entering the
	 * {@code Tile} that has this item. Has
	 * @param xyPos
	 */
	public StopSign(int[] xyPos) {
		this.xyPos = new int[] {xyPos[0] / Board.EXTRA_PADDING, 
				xyPos[1] / Board.EXTRA_PADDING};
		this.hp = START_HP;
		currState = START_HP / DIVIDER;
	}
	
	/**
	 * Constructs a {@code StopSign}
	 * @param xyPos - The x and y coordinates
	 * @param hp - the stop signs hp
	 */
	public StopSign(int[] xyPos, int hp) {
		this.xyPos = new int[] {xyPos[0] / Board.EXTRA_PADDING, 
				xyPos[1] / Board.EXTRA_PADDING};
		this.hp = hp;
		currState = START_HP / DIVIDER;
	}
	
	/**
	 * Number of rats trying to access this tile against the stop sign.
	 * @param n number of rats trying to get to this tile
	 * @return number of rats that are able to get into this tile
	 */
	public int numsRatsCanEnter(int n) {
		hp -= n;
		int nextState = hp / DIVIDER;
		if (nextState == 0) {
			// Still want tile to remove StopSign from itself
		} else if (currState != hp / DIVIDER) {
			currState = hp / DIVIDER;
			Main.damageStopSign(xyPos, currState);
		}
		
		if (hp > 0) {
			return 0;
		}
		return Math.abs(hp);
	}
	
	/**
	 * Returns an image of the Stop Sign based on its state.
	 * @param s state number, lower is more broken Stop Sign
	 * @return the image of the Stop Sign
	 */
	public static Image getImageState(int s) {
		return STATES[s];
	}
	/**
	 * Gets the current state
	 * @return the current state
	 */
	public int getState() {
		return currState - 1;
	}

	/**
	 * Doesnt touch the rat
	 * @return a list of rats
	 */
	@Override
	public ArrayList<Rat> itemAction(ArrayList<Rat> r) {
		// Does nothing
		return r;
	}

	/**
	 * Translates the class to a string
	 */
	@Override
	public String toString() {
		String out = NAME + "," + hp;
		return out;
	}
}
