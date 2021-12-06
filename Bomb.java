import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class emulating a bomb item.
 * @author Andrew Wu
 */
public class Bomb extends Item {
	
	/**
	 * X position of bomb
	 */
	private int x;
	
	/**
	 * y position of bomb.
	 */
	private int y;
	
	public static String name = "Bomb";
	
    /**
     * Sets the bomb with 1 health point.
     */
    public Bomb() {
        hp = 1; // is never used, only for constructor
    }
    
    /**
     * Sets the bomb with the remaining health it has.
     * @param hp seconds left from the bomb before it blows up
     */
    public Bomb(int[] xyPos, int hp) {
    	this.x = xyPos[0];
    	this.y = xyPos[1];
    	this.hp = hp;
    }

    /**
     * Array holding the variations of countdown images for the bomb.
     */
    public static final Image[] COUNTDOWN = new Image[] {
            new Image("img/ItemBomb1.png"),
            new Image("img/ItemBomb2.png"),
            new Image("img/ItemBomb3.png"),
            new Image("img/ItemBomb4.png")
    };

    /**
     * Returns the image corresponding to the countdown time on the bomb.
     * @param n image number.
     * @return Bomb image with countdown number.
     */
    public static Image getImage(int n) {
        return COUNTDOWN[n];
    }

    /**
     * Second bomb will count down from.
     */
    public static final int COUNTDOWN_IN_S = 4;

    /**
     * Current countdown of the bomb.
     */
    private int currentCountdown = COUNTDOWN_IN_S;

    Timer timer = new Timer();

    /**
     * Item ability triggered through calling this method. Method delayed per second, updating
     * the current time left on detonation. Once count down reaches 0, will invoke a method to
     * detonate.
     * @param x x-coordinate bomb was placed on
     * @param y y-coordinate bomb was placed on
     * @return true if bomb detonates successfully.
     */
    public boolean itemAction(int x, int y) {
        this.timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                currentCountdown--;
                if (currentCountdown >= 0) {
                    Main.editBombCountdown(currentCountdown, x, y);
                }

                if (currentCountdown < 0) {
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
		String out = name + hp;
		return out;
	}
}