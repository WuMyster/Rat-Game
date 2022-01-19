import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
 * Leaderboard for high scores.
 * 
 * @author Jing
 */

public class LeaderBoard {

	/**
	 * List of all player's scores.
	 */
	private static ArrayList<PlayerScore> scores = new ArrayList<>();

	/**
	 * ObservableList of player information that is put onto table.
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
		scores = new ArrayList<>();
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
	 * Adds information from the game and put into leaderboard.
	 * @return position of current player on leaderboard
	 */
	public static int addData() {
		PlayerScore ps = new PlayerScore(
				GameMaster.getName(),
				calculateScore(),
				GameMaster.getMaxTime() - GameGUI.getRemainingTime(),
				GameMaster.getLvlNum()
				);
		scores.add(ps);
		writeLeaders();
		
		int out = 0;
		while (scores.get(out) != ps) {
			out++;
		}
		return out;
	}
	
	/**
	 * Writes the leaderboard to a text file.
	 */
	private static void writeLeaders() {
		Collections.sort(scores);
		try {
			PrintWriter out = new PrintWriter(Main.PLAYER_FILE_LOC + "Leaderboard.txt");
			for(PlayerScore ps : scores) {
				out.println(ps);
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int calculateScore() {
		int out = 0;
		out += RatController.getPoints() * 
				GameGUI.getRemainingTime();
		return out;
	}	

	/**
	 * Returns the leaderboard scene with correct information.
	 * @return the leaderboard
	 */
	public static Pane getLeaderBoard() {
		TableView<PlayerScore> table = new TableView<>();
		TableColumn<PlayerScore, String> name = new TableColumn<>("Name");
		name.setCellValueFactory(
				new PropertyValueFactory<>("name"));
		
		TableColumn<PlayerScore, Integer> score = new TableColumn<>("Score");
		score.setCellValueFactory(
				new PropertyValueFactory<>("score"));
		
		TableColumn<PlayerScore, Integer> time = new TableColumn<>("Time");
		time.setCellValueFactory(
				new PropertyValueFactory<>("time"));
		
		TableColumn<PlayerScore, Integer> level = new TableColumn<>("Level");
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
