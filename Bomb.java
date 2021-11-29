import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class Bomb {
    final int COUNTDOWN = 1;
    final int COUNTDOWN_IN_MS = COUNTDOWN * 1000;
    Timer timer = new Timer();

    public boolean itemAction(int x, int y) {
        timer.cancel();
        this.timer = new Timer();

        TimerTask task = new TimerTask() {
            public void run() {
                detonate(x, y);
            }
        };

        timer.schedule(task, COUNTDOWN_IN_MS);

        return true;
    }


    public boolean detonate(int x, int y) {
        int originalY = y;
        int originalX = x;
        y *= Board.EXTRA_PADDING;
        x *= Board.EXTRA_PADDING;
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

    private void itemUsed(int[] pos) {
        int[] a = null;
        for (int[] i : Main.getBombPlace()) {
            if (Arrays.equals(i, pos)) {
                a = i;
            }
        }
        Main.getBombPlace().remove(a);
    }

    /**
     * Debug
     * @param array
     */
    public static void printArray(ArrayList<int[]> array) {
        for(int[] item : array) System.out.println(Arrays.toString(item));
    }
}
