import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
/**
 * @author Salim
 */

public class Leaderboard2 {

    private String player;
    private int points;

    public Leaderboard2() throws IOException {
    }

    public void getScores(String player, int point) {

      player = GameMaster.getPoints(i);
      point = GameMaster.getPoints(points.get(i));

   }

    private Scanner s;

    public void openLeaderboard() {

        try {

            s = new Scanner (new File("Leaderboard.txt"));
        }
        catch (Exception e) {
            System.out.println("No file found");
        }
    }

    public void readLeaderboard() {

        while (s.hasNext()) {
            String name = s.next();
            int score = s.nextInt();
        }
    }

    public void closeLeaderboard() {
        s.close();
    }

   // public void writeLeaderboard() throws IOException {

   //     FileWriter writer = new FileWriter("Leaderboard.txt");
    //    writer.write();
      //  writer.close();

  //  }


    public void comparePoints(GameMaster.points) throws IOException {  //points is a HashMap in GameMaster
        openLeaderboard();
        readLeaderboard();

        while (s.hasNext()) {

            HashMap<String, Integer> highscores = new HashMap<String, Integer>();

            String name = s.next();
            int score = s.nextInt();

            highscores.put(name, score);

            while (highscores.size() < 10) {

                highscores.put(GameMaster.getPoints(i),GameMaster.getPoints(points.get(i)));

                FileWriter writer = new FileWriter("Leaderboard.txt");
                writer.write(String.valueOf(highscores));
            }

            if (highscores.size() >= 10 && (GameMaster.points.get(i)) > score) {
                highscores.clear(); //removes everything

                highscores.put(GameMaster.getPoints(i),GameMaster.getPoints(points.get(i)));

                FileWriter writer2 = new FileWriter("Leaderboard.txt");
                writer2.write(String.valueOf(highscores));
                writer2.close();
            } else {
                //do nothing
            }

            closeLeaderboard();
        }


    }



}

