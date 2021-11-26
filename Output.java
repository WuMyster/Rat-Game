import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * GUI JavaFX, WIP
 * 
 * @author Jing Shiang Gu
 *
 */
public class Output extends Application {
	/**
	 * Width of the window.
	 */
	private static final int WINDOW_WIDTH = 960;
	/**
	 * Height of the window.
	 */
	private static final int WINDOW_HEIGHT = 600;

	/**
	 * Width of the game canvas.
	 */
	private static final int CANVAS_WIDTH = 850;
	/**
	 * Height of the game canvas.
	 */
	private static final int CANVAS_HEIGHT = 550;

	/**
	 * Number of tiles in the x axis.
	 */
	private static final int GRID_WIDTH_NUMBER = 17;
	/**
	 * Number of tiles in the y axis.
	 */
	private static final int GRID_HEIGHT_NUMBER = 11;

	/**
	 * Height and width of Tile.
	 */
	public static final int TILE_SIZE = 50;

	public static final int RAT_POSITION = 25;

	/**
	 * 
	 */
	public static final int NORMAL_RAT_SPEED = 25;

	/**
	 * Offset needed to center the Rat along the x axis.
	 */
	public static final int TILE_WIDTH_OFFSET = 10;

	// private Image playerImage;
	public static Image GRASS_IMAGE;
	public static Image TILE_IMAGE;
	public static Image RAT_IMAGE; // Change to ImageView
	public static Image STOP_SIGN;
	private Board m;
	ImageView draggableStop = new ImageView();
	public static int RAT_WIDTH;
	public static int RAT_HEIGHT;

	private Canvas mapCanvas;
	private Canvas ratCanvas;
	private Canvas itemCanvas;

	Label currLevel;
	Label currPoints;

	// Should eventually turn to HashMap to store all item position
	private static ArrayList<int[]> stopSignPlace;

	/**
	 * The Rats in the game window which needs to move.
	 */
	private static HashMap<Direction, ArrayList<int[]>> currMovement;
	private Timeline tickTimeline;
	int step;

	@Override
	public void start(Stage primaryStage) throws Exception {
		GRASS_IMAGE = new Image("Grass.png");
		TILE_IMAGE = new Image("Tile.png");
		RAT_IMAGE = new Image("Rat.png");
		STOP_SIGN = new Image("Stop_Sign.png");
		RAT_WIDTH = 30;
		RAT_HEIGHT = 45;

		BorderPane root = createGameGUI();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

		String properMap1 = "GGGGGGGGGGGGGGGGGGPPPPPPPJPPPPPPPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGJPPPPPPJPPPPPPJGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPPPPPPPJPPPPPPPGGGGGGGGGGGGGGGGGG";
		m = new Board(properMap1, 17, 11);
		stopSignPlace = new ArrayList<>();

		drawMap();
		moveRat();

		primaryStage.setScene(scene);
		primaryStage.show();

		m.placeRat(new Rat(true, 20), Direction.NORTH, 1, 1);
		Timeline a = new Timeline(new KeyFrame(Duration.seconds(1), event -> runCycle()));
		// a.setCycleCount(1);
		// a.setCycleCount(10);
		a.setCycleCount(Animation.INDEFINITE);
		a.play();
	}

	/**
	 * IMPORTANT This method will run in a cycle indefinitely until stopped,
	 * currently allows rats to move around.
	 */
	public void runCycle() {
		currMovement = new HashMap<>();
		step = 0;
		m.runAllTiles();

		tickTimeline.play();
		
		//Set points
		
		drawItems();
	}

	/**
	 * Criteria for Rat movements.
	 */
	public void moveRat() {
		tickTimeline = new Timeline(new KeyFrame(Duration.millis(10), event -> drawRat()));
		tickTimeline.setCycleCount(NORMAL_RAT_SPEED);
	}

	/**
	 * Draws the rats onto the game canvas.
	 */
	public void drawRat() {
		GraphicsContext gc = ratCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		step += 1;

		// List of rat positions and direction
		ArrayList<int[]> currDirection;
		currDirection = currMovement.get(Direction.NORTH);
		if (currDirection != null) {
			for (int[] i : currDirection) {
				gc.drawImage(RAT_IMAGE, i[1] * RAT_POSITION + 
						TILE_WIDTH_OFFSET, i[0] * RAT_POSITION - step * i[2],
						RAT_WIDTH, RAT_HEIGHT);
			}
		}

		currDirection = currMovement.get(Direction.EAST);
		if (currDirection != null) {
			for (int[] i : currDirection) {
				gc.drawImage(RAT_IMAGE, i[1] * RAT_POSITION + 
						TILE_WIDTH_OFFSET + step * i[2], i[0] * RAT_POSITION,
						RAT_WIDTH, RAT_HEIGHT);
			}
		}

		currDirection = currMovement.get(Direction.SOUTH);
		if (currDirection != null) {
			for (int[] i : currDirection) {
				gc.drawImage(RAT_IMAGE, i[1] * RAT_POSITION + 
						TILE_WIDTH_OFFSET, i[0] * RAT_POSITION + step * i[2],
						RAT_WIDTH, RAT_HEIGHT);
			}
		}

		currDirection = currMovement.get(Direction.WEST);
		if (currDirection != null) {
			for (int[] i : currDirection) {
				gc.drawImage(RAT_IMAGE, i[1] * RAT_POSITION + 
						TILE_WIDTH_OFFSET - step * i[2], i[0] * RAT_POSITION,
						RAT_WIDTH, RAT_HEIGHT);
			}
		}

	}

	/**
	 * Adds to list of Rat movements on the game canvas.
	 * 
	 * @param pos xy position of the rat
	 * @param extraSpeed {@code true} if rat is baby
	 * @param dir direction it's facing
	 */
	public static void addCurrMovement(int[] pos, boolean extraSpeed, Direction dir) {
		currMovement.putIfAbsent(dir, new ArrayList<int[]>());
		int speed = extraSpeed ? 2 : 1;
		currMovement.get(dir).add(new int[] { pos[0], pos[1], speed});
	}

	/**
	 * Creates game GUI.
	 * 
	 * @return the GUI
	 */
	public BorderPane createGameGUI() {
		BorderPane root = new BorderPane();
		
		root.setCenter(createCenterMap());
		root.setTop(createTopMenu());
		root.setRight(createRightMenu());

		return root;
	}

	/**
	 * Creates the game canvas in window. Will have the Board, Rats and Items.
	 * 
	 * @return Game canvas
	 */
	public Pane createCenterMap() {
		Pane root = new Pane();
		//Creating canvases
		mapCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		ratCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		itemCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		
		//Adding canvas to pane
		root.getChildren().add(mapCanvas);
		root.getChildren().add(ratCanvas);
		root.getChildren().add(itemCanvas);

		return root;
	}

	/**
	 * Remove stop sign from board.
	 * TODO Hopefully can be upgraded to remove all items.
	 * @param pos position where the stop sign is
	 */
	public static void removeStopSign(int[] pos) {
		int[] a = null;
		for (int[] i : stopSignPlace) {
			if (Arrays.equals(i, pos)) {
				a = i;
			}
		}
		stopSignPlace.remove(a);
	}

	/**
	 * Redraws all stop signs onto the map.
	 * TODO Hopefully can be upgraded to draw all items.
	 */
	public void drawItems() {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		for (int[] i : stopSignPlace) {
			gc.drawImage(STOP_SIGN, i[1] * TILE_SIZE, i[0] * TILE_SIZE);
		}
	}

	/**
	 * React when an object is dragged onto the canvas.
	 * 
	 * @param event The drag event itself which contains data about the drag that
	 *              occurred.
	 * @author Liam O'Reilly
	 * @author Jing Shiang Gu
	 */
	public void placeStopSign(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);

		stopSignPlace.add(new int[] { (int) y, (int) x });
		stopSignPlace.add(new int[] { 50, 50 });
		m.addStopSign((int) x, (int) y);

		// Draw an icon at the dropped location.
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.drawImage(STOP_SIGN, x * TILE_SIZE, y * TILE_SIZE);
	}

	/**
	 * Creates the top menu bar. Contains menu options.
	 * 
	 * @return the menu
	 */
	public HBox createTopMenu() {
		HBox root = new HBox();
		root.setSpacing(10);
		root.setPadding(new Insets(10, 10, 10, 10));

		MenuBar menuBar = new MenuBar();

		Menu menuFile = new Menu("File");

		MenuItem add = new MenuItem("Shuffle");
		menuFile.getItems().add(add);

		Menu optionFile = new Menu("Option");

		menuBar.getMenus().addAll(menuFile, optionFile);
		root.getChildren().addAll(menuBar);

		return root;
	}

	/**
	 * Creates the right menu - will contain level, points
	 * and number of items.
	 * 
	 * @return the right menu
	 */
	public VBox createRightMenu() {
		VBox root = new VBox();
		root.setSpacing(10);
		root.setPadding(new Insets(10, 10, 10, 10));

		currLevel = new Label("Level xx");
		currLevel.setFont(new Font(20));
		currLevel.setTextAlignment(TextAlignment.CENTER);
		root.getChildren().add(currLevel);

		currPoints = new Label("Points xx");
		currPoints.setFont(new Font(20));
		root.getChildren().add(currPoints);

		// Setup a draggable image.
		draggableStop.setImage(STOP_SIGN);
		root.getChildren().add(draggableStop);

		// This code setup what happens when the dragging starts on the image.
		// You probably don't need to change this (unless you wish to do more advanced
		// things).
		/**
		 * @author Liam O'Reilly
		 */
		draggableStop.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				// Mark the drag as started.
				// We do not use the transfer mode (this can be used to indicate different forms
				// of drags operations, for example, moving files or copying files).
				Dragboard db = draggableStop.startDragAndDrop(TransferMode.ANY);

				// We have to put some content in the clipboard of the drag event.
				// We do not use this, but we could use it to store extra data if we wished.
				ClipboardContent content = new ClipboardContent();
				content.putString("Hello");
				db.setContent(content);

				// Consume the event. This means we mark it as dealt with.
				event.consume();
			}
		});

		// This code allows the canvas to receive a dragged object within its bounds.
		// You probably don't need to change this (unless you wish to do more advanced
		// things).
		/**
		 * @author Liam O'Reilly
		 */
		itemCanvas.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				// Mark the drag as acceptable if the source was the draggable image.
				// (for example, we don't want to allow the user to drag things or files into
				// our application)
				if (event.getGestureSource() == draggableStop) {
					// Mark the drag event as acceptable by the canvas.
					event.acceptTransferModes(TransferMode.ANY);
					// Consume the event. This means we mark it as dealt with.
					event.consume();
				}
			}
		});

		// This code allows the canvas to react to a dragged object when it is finally
		// dropped.
		// You probably don't need to change this (unless you wish to do more advanced
		// things).
		/**
		 * @author Liam O'Reilly
		 */
		itemCanvas.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				// We call this method which is where the bulk of the behaviour takes place.
				placeStopSign(event);
				// Consume the event. This means we mark it as dealt with.
				event.consume();
			}
		});
		return root;
	}

	/**
	 * Draws map onto screen
	 */
	public void drawMap() {
		// Get the Graphic Context of the canvas. This is what we draw on.
		GraphicsContext gc = mapCanvas.getGraphicsContext2D();

		// Clear canvas
		gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

		m.drawBoard(gc);
	}

	public static void main(String[] args) {
		System.out.println("Start");
		launch(args);
		System.out.println("End");
	}
}
