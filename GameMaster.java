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

public class GameMaster extends Application {

    Stage window;

    @Override
    public void start(Stage primaryStage) throws IOException {
        window = primaryStage;
        window.setTitle("Game Master");

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
        loginButton.setOnAction(e -> getPlayer(playerInput));
        

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

        window.setScene(scene);
        window.show();
    }
    
    private void loadMap(int lvlNum) {
    	System.out.println(lvlNum);
    }
    
    private void nextPage() {
    	GridPane grid = new GridPane();
    	
    	Button lvl = new Button("1");
    	lvl.setOnAction(e -> loadMap(1));
    	grid.getChildren().add(lvl);
    	GridPane.setConstraints(lvl, 0, 0);
    	
    	lvl = new Button("2");
    	lvl.setOnAction(e -> loadMap(2));
    	grid.getChildren().add(lvl);
    	GridPane.setConstraints(lvl, 1, 0);
    	
    	Scene scene = new Scene(grid, 300, 200);
    	window.setScene(scene);
    	window.show();
    }

    private void getPlayer(TextField playerInput) {
    	File file = null;
    	Scanner in = null;
    	ArrayList<String> input = new ArrayList<>();
        try {
            file = new File(playerInput.getText() + ".txt");
            in = new Scanner(file);
            while (in.hasNextLine()) {
                input.add(in.nextLine());
            }
            System.out.println("Success!");
            nextPage();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't access file...");
            e.printStackTrace();
        } finally {
        	in.close();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
