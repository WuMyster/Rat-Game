import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	public static void startLeaderBoard() {
		Scanner s = null;
		File f = new File(Main.PLAYER_FILE_LOC + "Leaderboard.txt");
		try {
			s = new Scanner(f);
			while (s.hasNext()) {
				scores.add(new PlayerScore(s.next()));
			}
			s.close();
		} catch (Exception e) {
			System.err.println("Leaderboard file not found");
			try {
				if (f.createNewFile()) {
					System.out.println("Leaderboard file created");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Sets the data of being outputted to leaderboard scene.
	 */
	public static void setData() {
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
		TableView<PlayerScore> table = new TableView<>();
		TableColumn<PlayerScore, String> name = new TableColumn<>("Name");
		name.setCellValueFactory(
				new PropertyValueFactory<>("name"));
		
		TableColumn<PlayerScore, String> score = new TableColumn<>("Score");
		score.setCellValueFactory(
				new PropertyValueFactory<>("score"));
		
		TableColumn<PlayerScore, String> time = new TableColumn<>("Time");
		time.setCellValueFactory(
				new PropertyValueFactory<>("time"));
		
		TableColumn<PlayerScore, String> level = new TableColumn<>("Level");
		level.setCellValueFactory(
				new PropertyValueFactory<>("level"));

		setData();
		table.setItems(data);
		
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
