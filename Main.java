import javafx.application.Application;
import javafx.stage.Stage;

/**
 * GUI JavaFX, WIP
 * 
 * @author Jing Shiang Gu
 *
 */
public class Main extends Application {
	
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
		try {
			launch(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("End");
	}

}
