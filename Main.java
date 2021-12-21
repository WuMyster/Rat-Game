import javafx.application.Application;
import javafx.stage.Stage;

/**
 * GUI JavaFX, WIP
 * 
 * @author Jing Shiang Gu
 *
 */
public class Main extends Application {
	
	public final static String PLAYER_FILE_LOC = "./player/";
	
	public final static String MAP_FILE_LOC = "./map/";
	
	public final static String FILE_MAIN_SEPERATOR = ";";
	
	public final static String FILE_SUB_SEPERATOR = ",";
	
	public final static String IMAGE_FILE_LOC = "./img/";
	
	/**
	 * Main current window being displayed.
	 */
	public static Stage currWindow;

	@Override
	public void start(Stage primaryStage) throws Exception {
		currWindow = primaryStage;
		GameMaster.startGameMaster();
	}
	
	/**
	 * Starts the Game Screen.
	 */
	public static void startGameGUI() {
		GameGUI.startGameScreen();
	}

	/**
	 * Run this to run program.
	 * @param args cli arguements
	 */
	public static void main(String[] args) {
		System.out.println("Start");
		
		if (FILE_MAIN_SEPERATOR.equals(FILE_SUB_SEPERATOR)) {
			System.err.println("Error with file separation!");
		} else {
			try {
				launch(args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("End");
	}

}
