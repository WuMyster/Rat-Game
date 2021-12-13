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

        //Level label
        Label levelLabel = new Label("Player Level: ");
        GridPane.setConstraints(levelLabel, 0, 1);

        //Level input
        TextField levelInput = new TextField("Enter player level...");
        GridPane.setConstraints(levelInput, 1, 1);

        Button loginButton = new Button("Load Player");
        GridPane.setConstraints(loginButton, 1, 3);
        loginButton.setOnAction(e -> isInt(levelInput, levelInput.getText()));
        loginButton.setOnAction(e -> getPlayer(playerInput,levelInput));

        Button leaderboardButton = new Button("Check Leaderboard");
        leaderboardButton.setOnAction(e -> LeaderBoardWindow.displayLeaderboard("Leaderboard"));
        GridPane.setConstraints(leaderboardButton,1,5);
        leaderboardButton.setOnAction(e -> {
        	System.out.println("Leaderboard not implemented yet");
        });
        


        //message
        String message = MessageOfDay.getMsgDay();


        grid.getChildren().addAll(playerLabel, playerInput, levelLabel, levelInput, loginButton, leaderboardButton);

        Scene scene = new Scene(grid, 300, 200);

        window.setScene(scene);
        window.show();
    }

    private boolean isInt(TextField input, String message){
        try{
            int level = Integer.parseInt(input.getText());
            System.out.println("User level is: " + level);
            return true;
        }catch(NumberFormatException e){
            System.out.println("Error: " + message + " is not a number");
            return false;
        }

    }

    private void getPlayer(TextField playerInput, TextField levelInput) {
        try {
            File myObj = new File("filename.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String name = myReader.nextLine();
                int level = myReader.nextInt();
                //forward to create game class
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't access file...");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
