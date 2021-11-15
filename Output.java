import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
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
	private static final int WINDOW_HEIGHT = 569; //Nice

	// The dimensions of the canvas
	private static final int CANVAS_WIDTH = 850;
	private static final int CANVAS_HEIGHT = 550;

	// The number of the grid in number of cells.
	private static final int GRID_WIDTH_NUMBER = 17;
	private static final int GRID_HEIGHT_NUMBER = 11;
	
	public static final int TILE_SIZE = 50;
	
	//The width and height of tiles
	//public static final int 
	

	// private Image playerImage;
	public static Image GRASS_IMAGE;
	public static Image TILE_IMAGE;
	// private String start = "C:\\Users\\jsgu1\\eclipse-workspace\\CS230CW1\\";

	private Canvas mapCanvas;
	private Canvas ratCanvas;

	Label currLevel = new Label("Level xx");
	Label currPoints = new Label("Points xx");

	@Override
	public void start(Stage primaryStage) throws Exception {
		GRASS_IMAGE = new Image("Grass.png");
		TILE_IMAGE = new Image("Tile.png");
		
		
		BorderPane root = createGUI();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

		// Display the scene on the stage
		drawMap();
		drawGame();
		//drawRat();
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public BorderPane createGUI() {
		BorderPane root = new BorderPane();

		root.setCenter(createCenterMap());

		root.setTop(createTopMenu());

		root.setRight(createRightMenu());
		
		Pane empty = new Pane();
		empty.setMinSize(0, 0);
		empty.prefHeight(0);
		root.setBottom(empty);
			
		return root;
	}
	
	//GridPane? Might help with "snapping" item to correct Tile place
	public Pane createCenterMap() {
		Pane root = new Pane();
		mapCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		ratCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		root.getChildren().add(mapCanvas);
		root.getChildren().add(ratCanvas);
		BorderPane.setAlignment(root, Pos.BOTTOM_LEFT);	
		return root;
	}

	public HBox createTopMenu() {
		HBox root = new HBox();
		root.setSpacing(10);
		root.setPadding(new Insets(10, 10, 10, 10));;
		
		return root;
	}

	public VBox createRightMenu() {
		VBox root = new VBox();
		root.setSpacing(10);
		root.setPadding(new Insets(10, 10, 10, 10));

		currPoints.setFont(new Font(20));
		root.getChildren().add(currPoints);
		root.maxHeight(10);
		root.prefHeight(10);
		// toolbar.getChildren().add(resetPlayerLocationButton);

		return root;
	}
	
	//int x, int y, Direction d
	public void drawGame() {
		GraphicsContext gc = ratCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, 10, 10);//mapCanvas.getWidth(), mapCanvas.getHeight());
		
		Image ratImage = new Image("Rat.png");
		
		gc.drawImage(ratImage, 10, 0, 30, 45);
		gc.drawImage(ratImage, 360, 350, 30, 45);
		gc.drawImage(ratImage, 410, 250, 30, 45);
		gc.drawImage(ratImage, 760, 450, 30, 45);
	}

	/**
	 * Should be run once at the beginning of the game to create the map.
	 */
	public void drawMap() {
		// Get the Graphic Context of the canvas. This is what we draw on.
		GraphicsContext gc = mapCanvas.getGraphicsContext2D();

		// Clear canvas
		gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

		try {
			Map.drawMap(gc);
		} catch (NullPointerException e) {
			Image grassImage = new Image("Grass.png");
			for (int y = 0; y < GRID_HEIGHT_NUMBER; y++) {
				for (int x = y % 2; x < GRID_WIDTH_NUMBER; x += 2) {
					gc.drawImage(grassImage, x * 50, y * 50, 50, 50);
				}
			}
		}
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
