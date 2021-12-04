import java.util.ArrayList;
import java.util.*;
/**
 * @author Salim
 */

public class Leaderboard {

    public int scoring() {
        if (GameMaster.getTimer() < GameMaster.getExpectedTime()) { // needs GameMaster to be implemented
            RatController.getPoints() += (GameMaster.getExpectedTime() - GameMaster.getTimer());
        }
        return RatController.getPoints();
    }

    //ArrayList<Integer> scores = new ArrayList<Integer>(); // In GameMaster

    Collections.sort(scores, Collections.reverseOrder()); //sorts arraylist from highest to lowest score

    for (int i = 0; i < 10; i++) { // only top 10 scores are kept
        System.out.println(score.get(i));
    }

}
