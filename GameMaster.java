import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GameMaster {

	static Stage window;
    
    private static ArrayList<String> playerInfo = new ArrayList<>();
    
    private static ArrayList<String> rats = new ArrayList<>();
    
    private static ArrayList<String> items = new ArrayList<>();
    
    private static int maxTime = Integer.MAX_VALUE;
    
    private static int maxRats = Integer.MAX_VALUE;
    
    private static String map = null;
    
    private static String playerName = null;
    
    private static int[] mapSize = null;

    private static int maxLevel = Integer.MAX_VALUE;
    
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
        String message = MessageOfDay.getMsgDay();


        grid.getChildren().addAll(playerLabel, playerInput, loginButton, leaderboardButton);

        Scene scene = new Scene(grid, 300, 200);

        return scene;
    }
    
    public static int getMaxRats() {
    	return maxRats;
    }
    
    public static int[] getMapSize() {
    	return mapSize;
    }
    
    public static String getMap() {
    	return map;
    }
    
    public static ArrayList<String> getRats() {
    	return rats;
    }
    
    public static ArrayList<String> getItems() {
    	return items;
    }
    
    public static int getMaxTime() {
    	return maxTime;
    }
    
    public static String getName() {
    	return playerName;
    }
    
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
    }
    
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
            System.out.println("Couldn't access file...");
            e.printStackTrace();
        } finally {
        	in.close();
        }  	
        
        return out;
    }
    
    private static void lvlPage() {
    	maxLevel = Integer.valueOf(playerInfo.get(0));
    	
    	GridPane grid = new GridPane();
    	
    	Button lvl = new Button("1");
    	lvl.setOnAction(e -> loadGame(1));
    	lvl.setDisable(1 > maxLevel);
    	grid.getChildren().add(lvl);
    	GridPane.setConstraints(lvl, 0, 0);
    	
    	lvl = new Button("2");
    	lvl.setOnAction(e -> loadGame(2));
    	lvl.setDisable(2 > maxLevel);
    	grid.getChildren().add(lvl);
    	GridPane.setConstraints(lvl, 1, 0);
    	
    	Scene scene = new Scene(grid, 300, 200);
    	Main.currWindow.setScene(scene);
    	Main.currWindow.show();
    }

    private static void getPlayer(TextField playerInput) {
    	File file = null;
    	Scanner in = null;
    	playerName = playerInput.getText();
        try {
            file = new File("./player/" + playerName + ".txt");
            in = new Scanner(file);
            while (in.hasNextLine()) {
            	playerInfo.add(in.nextLine());
            }
            System.out.println("Success!");
            lvlPage();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't access file...");
            e.printStackTrace();
        } finally {
        	in.close();
        }
    }
}
