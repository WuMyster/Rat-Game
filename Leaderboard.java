import java.util.ArrayList;
import java.util.*;
/**
 * @author Salim
 */

public class Leaderboard {

    public int scoring() {
    	
    	int points;
        if (GameMaster.getTimer() < GameMaster.getExpectedTime()) { // needs GameMaster to be implemented
            points = RatController.getPoints();
            points += (GameMaster.getExpectedTime() - GameMaster.getTimer());
        } else {
        	points = 0;
        }
        return points;
    }

    //ArrayList<Integer> scores = new ArrayList<Integer>(); // In GameMaster

    public void addPoints() {
        ArrayList<Integer> scores = new ArrayList<Integer>();
        scores.add(scoring());
    }

    public void sortList(ArrayList<Integer> scores) {
        Collections.sort(scores, Collections.reverseOrder()); //sorts arraylist from highest to lowest score
    }

    public void showLeaderboard(ArrayList<Integer> scores) {

        for (int i = 0; i < 10; i++) { // only top 10 scores are kept
            System.out.println(scores.get(i));
        }
    }
}
