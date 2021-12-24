import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * @author Salim
 */

public class LeaderBoard {

	/**
	 * List of playerscores.
	 */
	private static ArrayList<PlayerScore> scores = new ArrayList<>();

	/**
	 * ObservableList of playerinformation.
	 */
	private static ObservableList<PlayerScore> data;
	
	/**
	 * Number of high scores shown.
	 */
	private final static int MAX = 3;

	/**
	 * Reads inputs of previous high scores into list.
	 */
	public void startLeaderBoard() {
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
	}

	/**
	 * Sets the data of being outputted to leaderboard scene.
	 */
	public void setData() {
		Collections.sort(scores);
		data = FXCollections.observableArrayList();
		for (int i = 0; i < MAX; i++) {
			data.add(scores.get(i));
		}
	}

	/**
	 * Returns the leaderboard scene with correct information.
	 * @return
	 */
	public static Pane getLeaderBoard() {
		TableView<String> table = new TableView<>();
		TableColumn<String, String> name = new TableColumn<>("Name");
		TableColumn<String, Integer> score = new TableColumn<>("Score");
		TableColumn<String, Integer> time = new TableColumn<>("Time");
		TableColumn<String, Integer> level = new TableColumn<>("Level");

		// table.setItems(scores);
		
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
