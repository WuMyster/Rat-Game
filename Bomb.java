import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class emulating a bomb item.
 * @author Andrew Wu
 * @author Jing Gu
 * TODO Wu, flash colour tiles on detonation
 * TODO Wu, maybe move bomb pic and arraylist here?
 * -Maybe
 */
public class Bomb extends Item {
    public Bomb() {
        hp = 1; // is never used, only for constructor
    }

    public static final Image[] COUNTDOWN = new Image[] {
            new Image("img/ItemBomb1.png"),
            new Image("img/ItemBomb2.png"),
            new Image("img/ItemBomb3.png"),
            new Image("img/ItemBomb4.png")
    };
    public static final int COUNTDOWN_IN_S = 4;

    Timer timer = new Timer();
    private int currentCountdown = COUNTDOWN_IN_S;

    public static Image getImage(int n) {
        return COUNTDOWN[n];
    }

    /**
     * Item ability triggered through calling this method. Method delayed by an amount in
     * milliseconds to emulate a bomb detonating.
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
		// Does nothing to rats
		return r;
	}
}
