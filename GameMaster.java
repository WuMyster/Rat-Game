import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Deals with all interactions outside of the game.
 * 
 * @author 2010573
 *
 */
public class GameMaster {
	
	/**
	 * Width of the window.
	 */
	private static int WINDOW_WIDTH = 255;
	
	/**
	 * Height of the window.
	 */
	private static int WINDOW_HEIGHT = 205;
    
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
     * Max level the player has achieved.
     */
    private static int maxLevel = Integer.MAX_VALUE;
    
    /**
     * Current level number game is on.
     */
    private static int currLvl = Integer.MIN_VALUE;
    
    /**
     * Number of points achieved so far in the game.
     */
    private static int pointsAccumulated = Integer.MIN_VALUE;
    
    /**
     * Value from confirmation window.
     */
    private static boolean answer;
    
    /**
     * Maximum number of levels.
     */
    private static int maxNumOfLevels;
    
    /**
     * Start name for level files.
     */
    private final static String START_NAME = "lvl";
    
    /**
     * Creates and displays the GameMaster Login window.
     */
    public static void startGameMaster() {
    	
    	maxNumOfLevels = 1;
    	File f = new File(Main.MAP_FILE_LOC + START_NAME + maxNumOfLevels++ + ".txt");
    	while (f.exists()) {
    		f = new File(Main.MAP_FILE_LOC + START_NAME + maxNumOfLevels++ + ".txt");
    	}

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
        GridPane.setConstraints(loginButton, 1, 2);
        loginButton.setOnAction(e -> {
        	playerName = playerInput.getText();
        	GameMaster.getPlayer();
        });
        LeaderBoard.startLeaderBoard();
        Pane leaderboard = LeaderBoard.getLeaderBoard();
        GridPane.setConstraints(leaderboard, 0, 3, 2, 1);
        
        grid.getChildren().addAll(playerLabel, playerInput, loginButton, 
        		leaderboard);

        Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);

        Main.setWindow(scene);
    }
    
    /**
     * Bad end. Too many rats caused game to end.
     */
    public static void gameEndTooManyRats() {
    	String msg = "There are too many rats!";
    	gameEnd(msg, false);
    }
    
    /**
     * Bad end. Time has run out.
     */
    public static void gameEndTimeEnd() {
    	String msg = "You ran out of time!";
    	gameEnd(msg, false);
    }

    /**
     * Good end. Killed all rats within time.
     */
	public static void gameEndWin() {
    	if (currLvl == maxLevel) {
    		maxLevel++;
    		
    		PrintWriter out = null;
        	try {
        		out = new PrintWriter(Main.PLAYER_FILE_LOC + playerName + ".txt");
        		out.print(String.valueOf(maxLevel));
        	} catch (FileNotFoundException e) {
        		e.printStackTrace();
        	} finally {
        		out.close();
        	}
    	}
    	
    	int pos = LeaderBoard.addData() + 1;
    	String msg = "You are in position " + pos+ "\n";
    	gameEnd(msg, true);
    	msg += "Congratulations!\\nYou've finished the game";
    }
	
    /**
     * End of game screen, allows user to choose menu level or
     * redo level/ next level depending on if they've finished the level.
     */
    private static void gameEnd(String msg, boolean good) {
    	
    	// Clears the user profile
    	playerInfo = new ArrayList<>();
    	playerInfo.add(String.valueOf(maxLevel));
    	
    	GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label mes = new Label(msg);
        GridPane.setConstraints(mes, 0, 0, 2, 1);
        
        Button lvlPage = new Button("Level menu");
        GridPane.setConstraints(lvlPage, 0, 1);
        lvlPage.setOnAction(e -> lvlPage());
        
        Button nextLevel;
        if (good) {
        	if (currLvl < maxLevel) {
        		nextLevel = new Button("Next level");
    	        nextLevel.setOnAction(e -> {
    	        	currLvl++;
    	        	createNewGame();
    	        });
        	} else {
        		nextLevel = new Button("Log out");
        		nextLevel.setOnAction(e -> {
        			GameMaster.startGameMaster();
        		});
        	}
        } else {
        	nextLevel = new Button("Redo level");
	        nextLevel.setOnAction(e -> createNewGame());
        }
        GridPane.setConstraints(nextLevel, 1, 1);
        grid.getChildren().addAll(mes, lvlPage, nextLevel);

        Scene scene = new Scene(grid, 300, 200);

        Main.setWindow(scene);
    }
    
    /**
     * Returns the current level number.
     * @return current level number
     */
    public static int getLvlNum() {
    	return currLvl;
    }
    
    /**
     * Returns the max level achieved by the player.
     * @return max level achieved by player
     */
    public static int getMaxLevel() {
    	return maxLevel;
    }
    
    /**
     * Returns max number of rats.
     * @return max number if rats
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
     * Returns points earned in game so far.
     * @return number of points earned in game so far
     */
    public static int getPoints() {
    	return pointsAccumulated;
    }
    
    /**
     * Gets the information of the level and set up values before calling
     * game window.
     * @param currLvl 	the level selected
     */
    private static void createNewGame() {
    	ArrayList<String> information = getInfoFromFile(Main.MAP_FILE_LOC + START_NAME + currLvl + ".txt");
    	
    	if (playerInfo.size() != 1) {
    		boolean overwritePreviousGame = confirmWindow(
    				"Starting a new game will\noverwrite your\nprevious game\n" + 
    				"Do you want to overwrite?");
    		if (!overwritePreviousGame) {
    			return;
    		}
    	}
    	String tmp = playerInfo.get(0);
    	playerInfo = new ArrayList<>();
    	playerInfo.add(tmp);
    	
		loadMapInfo();
		
		pointsAccumulated = 0;
		
    	int counter = 4;
		
    	int repeat = Integer.valueOf(information.get(counter++));
    	for (int i = 0; i < repeat; i++) {
    		rats.add(information.get(counter++));
    	}
    	// For now assume no items on board of new game
    	System.out.println("Finished reading file");
    	Main.startGameGUI();
    }
    
    /**
     * Loads game already saved in players file.
     */
    private static void loadPrevGame() {
    	// 0 is taken up by max level achieved by player
    	currLvl = Integer.valueOf(playerInfo.get(1));
    	loadMapInfo();
    	
    	pointsAccumulated = Integer.valueOf(playerInfo.get(2));
    	maxTime = Integer.valueOf(playerInfo.get(3));
    	
    	rats = new ArrayList<>();
    	
    	int counter = 4;
    	int repeat = Integer.valueOf(playerInfo.get(counter++));
    	for (int i = 0; i < repeat; i++) {
    		rats.add(playerInfo.get(counter++));
    	}
    	
    	repeat = Integer.valueOf(playerInfo.get(counter++));
    	for (int i = 0; i < repeat; i++) {
    		items.add(playerInfo.get(counter++));
    	}
    	Main.startGameGUI();
    }
    
    /**
     * Loads common information about the map.
     */
    private static void loadMapInfo() {
    	ArrayList<String> information = getInfoFromFile(Main.MAP_FILE_LOC + START_NAME + currLvl + ".txt");
    	
    	String[] mSize = information.get(0).split(" ");
		mapSize = new int[] {Integer.valueOf(mSize[0]), Integer.valueOf(mSize[1])};
		
		maxRats = Integer.valueOf(information.get(1));
		
		maxTime = Integer.valueOf(information.get(2));
		
		map = information.get(3);
    }
    
    /**
     * Returns an list of information from a file.
     * @param filename	the filename to get information from
     * @return			list of new line separated information
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
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't access file... Except this is not caught...");
            e.printStackTrace();
        } finally {
        	in.close();
        }  	
        return out;
    }
    
    /**
     * Returns a list of information from a file.
     * @param file	file to get information from
     * @return		lost of new line separated information
     */
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
    	for (int i = 1; i < maxNumOfLevels - 1; i++) { // 4 is max number of levels
    		Button lvl = new Button(String.valueOf(i));
        	lvl.setOnAction(e ->  {
        		currLvl = Integer.valueOf(lvl.getText());
        		createNewGame();
        	});
        	lvl.setDisable(i > maxLevel);
        	grid.getChildren().add(lvl);
        	GridPane.setConstraints(lvl, x++, y);
        	if (x == maxX) {
        		x = 0;
        		y++;
        	}
    	}
    	if (playerInfo.size() > 1) {
    		Button continuePrevGame = new Button("Load previous game");
    		continuePrevGame.setOnAction(e -> {
    			loadPrevGame();
    			});
    		GridPane.setConstraints(continuePrevGame, x, y);
    		grid.getChildren().add(continuePrevGame);
    	}
    	
    	Scene scene = new Scene(grid, 300, 200);

    	Main.setWindow(scene);
    }

    /**
     * Attempts to get the information about the player.
     * @param playerInput 	name of the player
     */
    private static void getPlayer() {
    	File f = new File(Main.PLAYER_FILE_LOC + playerName + ".txt");
    	if (f.isFile()) {
    		playerInfo = getInfoFromFile(f);
    		lvlPage();
    	} else {
    		boolean createNewPlayer = confirmWindow("Player doesn't exist\n" + 
    				"Would you like to create\na new player?");
    		if (createNewPlayer) {
    			writePlayerInfo("1");
    			lvlPage();
    		}
    	}
    }
    
    /**
     * Returns the confirmation of what the user wants.
     * @return {@code true} if user had selected yes in confirmWindow()
     */
    private static boolean getAnswer() {
    	return answer;
    }
    
    /**
     * Handles a confirmation window and returns {@code true} if
     * the user selects yes, {@code false} otherwise.
     * @param msg 	message to display to user
     * @return		{@code true} if user selects yes
     */
    private static boolean confirmWindow(String msg) {
    	
    	Stage getConfirm = new Stage();
		
		GridPane newPlayer = new GridPane();
		Label question = new Label(msg);
		question.setAlignment(Pos.CENTER);
		GridPane.setConstraints(question, 0, 0, 2, 1);
		
		Button yes = new Button("Yes");
		yes.setOnAction(e -> {
			GameMaster.answer = true;
			getConfirm.close();
		});
		GridPane.setConstraints(yes, 0, 1);
		
		Button no = new Button("No");
		no.setOnAction(e -> {
			GameMaster.answer = false;
			getConfirm.close();
		});
		GridPane.setConstraints(no, 1, 1);
		
		newPlayer.getChildren().addAll(question, yes, no);
		
		Scene confirmNewPlayer = new Scene(newPlayer, 150, 100);
		
		getConfirm.setScene(confirmNewPlayer);
		getConfirm.initModality(Modality.APPLICATION_MODAL);
		getConfirm.showAndWait();
		
    	return getAnswer();
    }
    
    /**
     * Creates a new player with default values.
     * @param name	name of the player
     */
    private static void writePlayerInfo(String maxLvl) {
    	File file = new File(Main.PLAYER_FILE_LOC + playerName + ".txt");
    	try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
    		writer.write(maxLvl);
    		writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	playerInfo.add(maxLvl);
    }
}
