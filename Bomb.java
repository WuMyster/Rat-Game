import java.util.Timer;
import java.util.TimerTask;

public class Bomb {
    int countdown = 5; // 5 for the time being

    public void itemAction (PathTile tile) {
        Timer timer = new Timer();
        timer.schedule(new Detonate(), countdown * 1000);
    }

    class Detonate extends TimerTask {
        public void run () {
            // Figure out how to traverse path and kill all rats
        }
    }
}
