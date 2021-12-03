import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class emulating a bomb item.
 * @author Andrew
 * @author Jing
 * TODO Wu, flash colour tiles on detonation
 * TODO Wu, maybe move bomb pic and arraylist here?
 * -Maybe
 */
public class Bomb extends Item {
    final private int COUNTDOWN_IN_MS = 4000; // 4 seconds as per spec
    Timer timer = new Timer();

    /**
     * Item ability triggered through calling this method. Method delayed by an amount in
     * milliseconds to emulate a bomb detonating.
     * FIXME Wu need to clear timer properly.
     * @param x
     * @param y
     * @return true if bomb detonates successfully.
     */
    public boolean itemAction(int x, int y) {
        timer.cancel(); // Cancel any existing timer.
        this.timer = new Timer();

        TimerTask task = new TimerTask() {
            public void run() {
                detonate(x, y);
            }
        };

        timer.schedule(task, COUNTDOWN_IN_MS); // Start method run() after amount of time in ms.

        return true;
    }



    /**
     * Detonates bomb by clearing all tiles in each row one by one.
     * @param x x-coordinate bomb was placed on
     * @param y y-coordinate bomb was placed on
     * @return true if bomb detonated successfully
     */
    private boolean detonate(int x, int y) {
        int originalY = y;
        int originalX = x;
        y *= Board.getExtraPadding();
        x *= Board.getExtraPadding();
        int startY = y;
        int startX = x;
        TileType[][] board = Board.getBoard();

        TileType t = board[startY][startX];
        while (t != null) {
            t.blowUp();
            t = board[y--][x];
        }

        t = board[startY][startX];
        y = startY;
        x = startX;
        while (t != null) {
            t.blowUp();
            t = board[y++][x];
        }

        t = board[startY][startX];
        y = startY;
        x = startX;
        while (t != null) {
            t.blowUp();
            t = board[y][x--];
        }

        t = board[startY][startX];
        y = startY;
        x = startX;
        while (t != null) {
            t.blowUp();
            t = board[y][x++];
        }

        itemUsed(new int[] {originalY, originalX});
        return true;
    }

    /**
     * Once bomb is detonated, bomb is removed from arrayList storing it and subsequently,
     * graphic is removed.
     * @param pos Coordinates of bomb.
     */
    private void itemUsed(int[] pos) {
        int[] a = null;
        for (int[] i : Main.getBombPlace()) {
            if (Arrays.equals(i, pos)) {
                a = i;
            }
        }
        Main.getBombPlace().remove(a);
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
