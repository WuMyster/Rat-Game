import java.util.Timer;
import java.util.TimerTask;

public class Bomb {
    final int COUNTDOWN = 3; // 3 for the time being. Number denotes seconds.
    final int COUNTDOWN_IN_MS = COUNTDOWN * 1000;
    int x;
    int y;
    Timer timer = new Timer();

    public boolean itemAction(int x, int y) {
        this.x = x;
        this.y = y;

        this.timer = new Timer();

        TimerTask task = new TimerTask() {
            public void run() {
                detonate();
            }
        };

        timer.schedule(task, COUNTDOWN_IN_MS);

        return true;
    }


    public boolean detonate() {
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
        return true;
    }
}
