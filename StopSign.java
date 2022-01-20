
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
		currState = 3;
	}
	
	/**
	 * Constructs a {@code StopSign}
	 * @param xyPos - The x and y coordinates
	 * @param hp - the stop signs hp
	 */
	public StopSign(int[] xyPos, int hp) {
		this.xyPos = xyPos;
		currState = 3;
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
	 * Translates the class to a string
	 */
	@Override
	public String toString() {
		String out = NAME + Main.FILE_SUB_SEPERATOR + 1;
		return out;
	}
}
