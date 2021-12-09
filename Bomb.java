import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class emulating a bomb item.
 * 
 * @author Andrew Wu
 */
public class Bomb extends Item {

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
	public static final Image IMAGE = new Image("img/ItemBomb.png");

	/**
	 * Array holding the variations of countdown images for the bomb.
	 */
	public static final Image[] STATES = new Image[] { 
			new Image("img/ItemBomb1.png"), new Image("img/ItemBomb2.png"),
			new Image("img/ItemBomb3.png"), new Image("img/ItemBomb4.png")};

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
	 */
	public Bomb(int[] xyPos) {
		hp = START_COUNTDOWN; // is never used, only for constructor
		this.x = xyPos[1];
		this.y = xyPos[0];
	}

	/**
	 * Sets the bomb with the remaining health it has.
	 * 
	 * @param hp seconds left from the bomb before it blows up
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
	
	public int getState() {
		return hp;
	}

	/**
	 * Item ability triggered through calling this method. Method delayed per
	 * second, updating the current time left on detonation. Once count down reaches
	 * 0, will invoke a method to detonate.
	 * 
	 * @param x x-coordinate bomb was placed on
	 * @param y y-coordinate bomb was placed on
	 * @return true if bomb detonates successfully.
	 */
	public boolean itemAction() {
		this.timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				hp--;
				if (hp >= 0) {
					Main.editBombCountdown(x, y, hp);
				}

				if (hp < 0) {
					timer.cancel();
					Board.detonate(x, y);
				}
			}
		}, 0, 1000);
		return true;
	}

	@Override
	public ArrayList<Rat> itemAction(ArrayList<Rat> r) {
		// Does nothing directly to rats
		return r;
	}

	@Override
	public String toString() {
		String out = NAME + "," + hp;
		return out;
	}
}