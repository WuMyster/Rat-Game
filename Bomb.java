import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class emulating a bomb item.
 * 
 * @author Andrew Wu
 */
public class Bomb extends TimeItem {

	/**
	 * Item name of Bomb.
	 */
	public static final String NAME = "Bomb";

	/**
	 * Second bomb will count down from.
	 */
	public static final int START_COUNTDOWN = 4;

	/**
	 * Timer the bomb will run off.
	 */
	public Timer timer = new Timer();

	/**
	 * Icon of item.
	 */
	public static final Image IMAGE = new Image(Main.IMAGE_FILE_LOC + "ItemBomb.png");

	/**
	 * Array holding the variations of countdown images for the bomb.
	 */
	public static final Image[] STATES = new Image[] { 
			new Image(Main.IMAGE_FILE_LOC + "ItemBomb1.png"), new Image(Main.IMAGE_FILE_LOC + "ItemBomb2.png"),
			new Image(Main.IMAGE_FILE_LOC + "ItemBomb3.png"), new Image(Main.IMAGE_FILE_LOC + "ItemBomb4.png")};

	/**
	 * X position of bomb
	 */
	private int x;

	/**
	 * y position of bomb.
	 */
	private int y;

	/**
	 * Sets the bomb with 1 health point.
	 * @param xyPos		xy position of the bomb
	 */
	public Bomb(int[] xyPos) {
		hp = START_COUNTDOWN; // is never used, only for constructor
		this.x = xyPos[1];
		this.y = xyPos[0];
		timer = new Timer();
		stopTimer = false;
	}

	/**
	 * Sets the bomb with the remaining health it has.
	 * 
	 * @param xyPos		xy position of the bomb
	 * @param hp 		seconds left from the bomb before it blows up
	 */
	public Bomb(int[] xyPos, int hp) {
		this.x = xyPos[1];
		this.y = xyPos[0];
		this.hp = hp;
	}

	/**
	 * Returns the image corresponding to the countdown time on the bomb.
	 * 
	 * @param n image number.
	 * @return Bomb image with countdown number.
	 */
	public static Image getImage(int n) {
		if (n >= STATES.length) {
			n = STATES.length - 1;
		}
		return STATES[n];
	}
	
	/**
	 * Returns state of the Bomb.
	 */
	public int getState() {
		return hp;
	}

	/**
	 * Item ability triggered through calling this method. Method delayed per
	 * second, updating the current time left on detonation. Once count down reaches
	 * 0, will invoke a method to detonate.
	 */
	public void itemAction() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (stopTimer) {
					timer.cancel();
				} 
				hp--;
				if (hp >= 0) {
					GameGUI.editItemState(ItemType.BOMB, x, y, hp);
				} else {
					timer.cancel();
					Board.detonate(x, y);
				}
			}
		}, 0, 1000);
	}

	@Override
	public ArrayList<Rat> itemAction(ArrayList<Rat> r) {
		// Does nothing directly to rats
		return r;
	}

	@Override
	public String toString() {
		String out = NAME + Main.FILE_SUB_SEPERATOR + hp;
		return out;
	}
}