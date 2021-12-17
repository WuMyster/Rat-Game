import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GameMaster {
    
	/**
	 * Information from players file.
	 */
    private static ArrayList<String> playerInfo = new ArrayList<>();
    
    /**
     * List of rat positions.
     */
    private static ArrayList<String> rats = new ArrayList<>();
    
    /**
     * List of item positions
     */
    private static ArrayList<String> items = new ArrayList<>();
    
    /**
     * Max time available to finish game.
     */
    private static int maxTime = Integer.MAX_VALUE;
    
    /**
     * Max number of rats to lose the game.
     */
    private static int maxRats = Integer.MAX_VALUE;
    
    /**
     * String of the map.
     */
    private static String map = null;
    
    /**
     * Name of player.
     */
    private static String playerName = null;
    
    /**
     * x y size of the map.
     */
    private static int[] mapSize = null;

    /**
     * Max level the player has achieved
     */
    private static int maxLevel = Integer.MAX_VALUE;
    
    /**
     * Creates and returns a Game Master Scene
     * @return Game Master Scene
     */
    public static Scene startGameMaster() {

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        //Player label
        Label playerLabel = new Label("Player Name: ");
        GridPane.setConstraints(playerLabel, 0, 0);

        //Player input
        TextField playerInput = new TextField("Enter player name...");
        GridPane.setConstraints(playerInput, 1, 0);

        Button loginButton = new Button("Load Player");
        GridPane.setConstraints(loginButton, 1, 3);
        loginButton.setOnAction(e -> GameMaster.getPlayer(playerInput));
        

        Button leaderboardButton = new Button("Check Leaderboard");
        leaderboardButton.setOnAction(e -> LeaderBoardWindow.displayLeaderboard("Leaderboard"));
        GridPane.setConstraints(leaderboardButton,1,5);
        leaderboardButton.setOnAction(e -> {
        	System.out.println("Leaderboard not implemented yet");
        });
        
        //message
//        String message = MessageOfDay.getMsgDay();


        grid.getChildren().addAll(playerLabel, playerInput, loginButton, leaderboardButton);

        Scene scene = new Scene(grid, 300, 200);

        return scene;
    }
    
    /**
     * Returns max number of rats
     * @return
     */
    public static int getMaxRats() {
    	return maxRats;
    }
    
    /**
     * Returns x y size of the map.
     * @return x y size of the map
     */
    public static int[] getMapSize() {
    	return mapSize;
    }
    
    /**
     * Returns the map.
     * @return the map
     */
    public static String getMap() {
    	return map;
    }
    
    /**
     * Returns list of rats on the board.
     * @return list of rats on the board
     */
    public static ArrayList<String> getRats() {
    	return rats;
    }
    
    /**
     * Returns list of items already present on board.
     * @return list of items already present on board
     */
    public static ArrayList<String> getItems() {
    	return items;
    }
    
    /**
     * Returns max time to finish game.
     * @return max time to finish game
     */
    public static int getMaxTime() {
    	return maxTime;
    }
    
    /**
     * Returns name of player.
     * @return name of player
     */
    public static String getName() {
    	return playerName;
    }
    
    /**
     * Gets the information of the level and set up values before calling
     * game window.
     * @param lvlNum the level selected
     */
    private static void loadGame(int lvlNum) {
    	// For now, assume no ongoing game
    	ArrayList<String> information = getInfoFromFile("./map/lvl" + lvlNum + ".txt");
    	
    	int counter = 0;
    	
    	String[] mSize = information.get(counter++).split(" ");
		mapSize = new int[] {Integer.valueOf(mSize[0]), Integer.valueOf(mSize[1])};
		
		maxRats = Integer.valueOf(information.get(counter++));
		
		maxTime = Integer.valueOf(information.get(counter++));
		
		map = information.get(counter++);
		
    	int repeat = Integer.valueOf(information.get(counter++));
    	for (int i = 0; i < repeat; i++) {
    		rats.add(information.get(counter++));
    	}
    	
    	repeat = Integer.valueOf(information.get(counter++));
    	for (int i = 0; i < repeat; i++) {
    		items.add(information.get(counter++));
    	}
    	System.out.println("Finished reading file");
    	Main.gameScreen();
    }
    
    /**
     * Returns an list of information from a file
     * @param filename	the file to get information from
     * @return			list of new line seperarated information
     */
    private static ArrayList<String> getInfoFromFile(String filename) {
    	ArrayList<String> out = new ArrayList<>();
    	File file = null;
    	Scanner in = null;
        try {
            file = new File(filename);
            in = new Scanner(file);
            while (in.hasNextLine()) {
            	out.add(in.nextLine());
            }
            System.out.println("Success!");
            lvlPage();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't access file... Except this is not caught...");
            e.printStackTrace();
        } finally {
        	in.close();
        }  	
        
        return out;
    }
    
    private static ArrayList<String> getInfoFromFile(File file) {
    	ArrayList<String> out = new ArrayList<>();
    	Scanner in = null;
        try {
            in = new Scanner(file);
            while (in.hasNextLine()) {
            	out.add(in.nextLine());
            }
            System.out.println("Success!");
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't access file... Except this is not caught...");
            e.printStackTrace();
        } finally {
        	in.close();
        }  	
        return out;
    }
    
    /**
     * Sets up the level page to select level of the game.
     */
    private static void lvlPage() {
    	maxLevel = Integer.valueOf(playerInfo.get(0));
    	
    	GridPane grid = new GridPane();
    	
    	Label pageName = new Label("Select levels");
    	int maxX = 2;
    	grid.getChildren().add(pageName);
    	GridPane.setConstraints(pageName, 0, 0, maxX, 1);
    	
    	// Should get max number of levels from somewhere
    	int x = 0;
    	int y = 1;
    	
    	for (int i = 1; i < 4; i++) { // 4 is max number of levels
    		Button lvl = new Button(String.valueOf(i));
        	lvl.setOnAction(e ->  {
        		loadGame(Integer.valueOf(lvl.getText()));
        	});
        	lvl.setDisable(i > maxLevel);
        	grid.getChildren().add(lvl);
        	GridPane.setConstraints(lvl, x++, y);
        	if (x == maxX) {
        		x = 0;
        		y++;
        	}
    	}
    	
    	Scene scene = new Scene(grid, 300, 200);
    	Main.currWindow.setScene(scene);
    	Main.currWindow.show();
    }

    private static void getPlayer(TextField playerInput) {
    	playerName = playerInput.getText();
    	File f = new File("./player/" + playerName + ".txt");
    	if (f.exists() && !f.isDirectory()) {
    		playerInfo = getInfoFromFile(f);
    		lvlPage();
    	} else {
    		System.out.println("Player doesn't exist!");
    		// playerInfo = getInfoFromFile("./player/" + playerName + ".txt");
    		
    		GridPane newPlayer = new GridPane();
    		Label question = new Label("Player doesn't exist\n" + 
    				"Would you like to create\na new player?");
    		question.setAlignment(Pos.CENTER);
    		GridPane.setConstraints(question, 0, 0, 2, 1);
    		
    		Button yes = new Button("Yes");
    		GridPane.setConstraints(yes, 0, 1);
    		
    		Button no = new Button("No");
    		GridPane.setConstraints(no, 1, 1);
    		
    		newPlayer.getChildren().addAll(question, yes, no);
    		
    		Scene confirmNewPlayer = new Scene(newPlayer, 150, 100);
    		Stage getConfirm = new Stage();
    		getConfirm.setScene(confirmNewPlayer);
    		getConfirm.initModality(Modality.APPLICATION_MODAL);
    		getConfirm.showAndWait();
    	}
    }
}
