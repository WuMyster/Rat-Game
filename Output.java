import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Output extends Application {
	// The dimensions of the window
	private static final int WINDOW_WIDTH = 960;
	private static final int WINDOW_HEIGHT = 600;

	// The dimensions of the canvas
	private static final int CANVAS_WIDTH = 850;
	private static final int CANVAS_HEIGHT = 550;

	// The number of the grid in number of cells.
	private static final int GRID_WIDTH_NUMBER = 17;
	private static final int GRID_HEIGHT_NUMBER = 11;
	
	public static final int TILE_SIZE = 50;
	
	//The width and height of tiles
	//public static final int 
	

	private Canvas mapCanvas;
	private Canvas ratCanvas;

	Label currLevel = new Label("Level xx");
	Label currPoints = new Label("Points xx");

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = createGUI();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

		/*
		 * canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT); root.setCenter(canvas);
		 */

		// Display the scene on the stage
		drawMap();
		drawGame();
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public BorderPane createGUI() {
		BorderPane root = new BorderPane();

		root.setCenter(center());

		root.setTop(createTopMenu());

		root.setRight(createRightMenu());

		return root;
	}
	
	//GridPane?
	public Pane center() {
		Pane root = new Pane();
		mapCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		ratCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		root.getChildren().add(mapCanvas);
		root.getChildren().add(ratCanvas);
		return root;
	}

	public HBox createTopMenu() {
		HBox root = new HBox();
		root.setSpacing(10);
		root.setPadding(new Insets(10, 10, 10, 10));
		return root;
	}

	public VBox createRightMenu() {
		VBox root = new VBox();
		root.setSpacing(10);
		root.setPadding(new Insets(10, 10, 10, 10));

		currPoints.setFont(new Font(20));
		root.getChildren().add(currPoints);
		// toolbar.getChildren().add(resetPlayerLocationButton);

		return root;
	}
	
	/**
	 * Should draw the position of rats and item on top of map
	 */
	public void drawGame() {
		GraphicsContext gc = ratCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, 10, 10);//mapCanvas.getWidth(), mapCanvas.getHeight());
		
		Image ratImage = new Image("Rat.png");
		gc.drawImage(ratImage, 360, 350, 30, 45);
		
		gc.drawImage(ratImage, 10, 0, 30, 45);
	}

	/**
	 * Should be run once at the beginning of the game to create the map.
	 */
	public void drawMap() {
		// Get the Graphic Context of the canvas. This is what we draw on.
		GraphicsContext gc = mapCanvas.getGraphicsContext2D();

		// Clear canvas
		gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

		// Set the background to gray.
		//gc.setFill(Color.GRAY);
		//gc.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

		
		Image grassImage = new Image("Grass.png");
		for (int y = 0; y < GRID_HEIGHT_NUMBER; y++) {
			for (int x = y % 2; x < GRID_WIDTH_NUMBER; x += 2) {
				gc.drawImage(grassImage, x * 50, y * 50, 50, 50);
			}
		}
		
		
		//Map.drawGame(gc);
	}

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
