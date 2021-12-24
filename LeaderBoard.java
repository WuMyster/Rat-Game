import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * @author Salim
 */

public class LeaderBoard {

	private static ArrayList<PlayerScore> scores = new ArrayList<>();
	
	public void createLeaderBoard() {

		Scanner s = null;
		try {
			s = new Scanner(new File("Leaderboard.txt"));
			while (s.hasNext()) {
				scores.add(new PlayerScore(s.next()));
			}
		} catch (Exception e) {
			System.out.println("No file found");
		} finally {
			s.close();
		}

		Collections.sort(scores);
		Collections.reverse(scores);
	}

	public static Pane getLeaderBoard() {		
		TableView<String> table = new TableView<>();
		TableColumn<String, String> name = new TableColumn<>("Name");
		TableColumn<String, Integer> score = new TableColumn<>("Score");
		TableColumn<String, Integer> time = new TableColumn<>("Time");
		TableColumn<String, Integer> level = new TableColumn<>("Level");
		
		table.getColumns().add(name);
		table.getColumns().add(score);
		table.getColumns().add(time);
		table.getColumns().add(level);
		
		
		VBox tableUI = new VBox();
		tableUI.setSpacing(5);
		tableUI.setPadding(new Insets(10, 0, 0, 10));
		tableUI.getChildren().add(table);
		
		return tableUI;
	}
}
