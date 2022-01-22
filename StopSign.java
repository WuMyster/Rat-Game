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
	 * Icon of this image.
	 */
	public static final Image IMAGE = new Image(Main.IMAGE_FILE_LOC + "StopSign2.png");
	
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
			new Image(Main.IMAGE_FILE_LOC + "StopSign0.png"),
			new Image(Main.IMAGE_FILE_LOC + "StopSign1.png"),
			IMAGE
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
	 * @param xyPos 	xyPos of the item.
	 */
	public StopSign(int[] xyPos) {
		this.xyPos = xyPos;
		this.hp = START_HP;
		currState = getState();
	}
	
	/**
	 * Constructs a {@code StopSign}
	 * @param xyPos - The x and y coordinates
	 * @param hp - the stop signs hp
	 */
	public StopSign(int[] xyPos, int hp) {
		this.xyPos = xyPos;
		this.hp = hp;
		currState = getState();
	}
	
	/**
	 * Number of rats trying to access this tile against the stop sign.
	 * @param n number of rats trying to get to this tile
	 * @return number of rats that are able to get into this tile
	 */
	public int numsRatsCanEnter(int n) {
		hp -= n;
		int nextState = getState();
		if (nextState == 0) {
			// Still want tile to remove StopSign from itself
		} else if (currState != nextState) {
			currState = nextState;
			GameGUI.editItemState(ItemType.STOPSIGN, xyPos[1], xyPos[0], currState);
		}
		if (isAlive()) {
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
		return hp / DIVIDER;
	}

	/**
	 * Doesn't touch the rat.
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
		String out = NAME + Main.FILE_SUB_SEPERATOR + hp;
		return out;
	}
}
