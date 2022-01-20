import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * GUI JavaFX, WIP
 * 
 * @author Jing Shiang Gu
 *
 */
public class Main extends Application {
	
	/**
	 * File location of information about players.
	 */
	public final static String PLAYER_FILE_LOC = "./player/";
	
	/**
	 * File location of information about the different maps.
	 */
	public final static String MAP_FILE_LOC = "./map/";
	
	/**
	 * Main file separation between major aspects of information.
	 */
	public final static String FILE_MAIN_SEPERATOR = ";";
	
	/**
	 * Minor file separation between minor aspects of information.
	 */
	public final static String FILE_SUB_SEPERATOR = ",";
	
	/**
	 * File location of information about the different images.
	 */
	public final static String IMAGE_FILE_LOC = "./img/";
	
	/**
	 * Main current window being displayed.
	 */
	private static Stage currWindow;

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
	 * Shows the window.
	 * 
	 * @param scene	scene to display
	 */
	public static void setWindow(Scene scene) {
		currWindow.setScene(scene);
		currWindow.show();
		currWindow.centerOnScreen();
	}

	/**
	 * Run this to run program.
	 * @param args cli arguments
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
		Inventory.stopInv();
		System.out.println("End");
	}

}
