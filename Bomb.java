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
    enum Name {
        BOMB
    }
    final private int COUNTDOWN_IN_MS = 4000; // 4 seconds as per spec
    Timer timer = new Timer();

    /**
     * Item ability triggered through calling this method. Method delayed by an amount in
     * milliseconds to emulate a bomb detonating.
     * FIXME Wu need to clear timer properly.
     * @param x x-coordinate bomb was placed on
     * @param y y-coordinate bomb was placed on
     * @return true if bomb detonates successfully.
     */
    public boolean itemAction(int x, int y) {
        timer.cancel(); // Cancel any existing timer.
        this.timer = new Timer();

        TimerTask task = new TimerTask() {
            public void run() {
                Board.detonate(x, y);
            }
        };

        timer.schedule(task, COUNTDOWN_IN_MS); // Start method run() after amount of time.

        return true;
    }
    // TODO Need method to reduce inventory quantity

    /**
     * Debug
     * @param array
     */
    public static void printArray(ArrayList<int[]> array) {
        for(int[] item : array) System.out.println(Arrays.toString(item));
    }
}
