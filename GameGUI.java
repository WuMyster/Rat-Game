
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
import javafx.scene.paint.Color;

/**
 * Class for interaction for the game. Responsible for displaying map, 
 * moving rats, number of rats in game, time left in game, ending game, 
 * items being put onto the map and drawing items onto the map.
 * Primarily responsible for output.
 * 
 * @author J
 *
 */
public class GameGUI {
	
	/**
	 * Width of the window in pixels.
	 */
	private static final int WINDOW_WIDTH = 1150;

	/**
	 * Height of the window in pixels.
	 */
	private static final int WINDOW_HEIGHT = 650;

	/**
	 * Width of the game canvas in pixels.
	 */
	private static final int CANVAS_WIDTH = 850;

	/**
	 * Height of the game canvas in pixels.
	 */
	private static final int CANVAS_HEIGHT = 600;

	/**
	 * Height and width of a Tile in pixels.
	 */
	public static final int TILE_SIZE = 50;

	/**
	 * Draggable image for stop sign.
	 */
	private final static ImageView draggableStop = new ImageView(StopSign.IMAGE);

	/**
	 * Draggable image for sterilise item.
	 */
	private final static ImageView draggableSterilise = new ImageView(Sterilisation.IMAGE);
	
	/**
	 * Board of the game
	 */
	private static Board m;

	/**
	 * Canvas of map tiles.
	 */
	private static Canvas mapCanvas;

	/**
	 * Grey background.
	 */
	private static Canvas baseCanvas;

	/**
	 * Canvas for all rat classes + death rat.
	 */
	private static Canvas ratCanvas;

	/**
	 * Canvas for all items not including death rat.
	 */
	private static Canvas itemCanvas;

	/**
	 * Creates and displays the game window, the rat alive indicator and starts the
	 * game.
	 */
	public static void startGameScreen() {
		BorderPane gameRoot = createGameGUI();
		Scene scene = new Scene(gameRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		Main.setWindow(scene);
	}

	/**
	 * Reacts to item that is dragged onto canvas.
	 * 
	 * @param event The drag event itself which contains data about the drag that
	 *              occured.
	 */
	public static void itemCanvasDragDropOccurred(DragEvent event) {
		System.out.println("Added");
		if (event.getGestureSource() == draggableStop) {
			placeItemOnMap(StopSign.NAME, event);
		} else if (event.getGestureSource() == draggableSterilise) {
			placeItemOnMap(Sterilisation.NAME, event);
		} else {
			System.err.println("Dragging fail!!");
		}
		
	}
	
	/**
	 * From user input, have item be added to the specific tile to be added.
	 * 
	 * @param it		type of item
	 * @param event		DragEvent information
	 */
	private static void placeItemOnMap(String it, DragEvent event) {
		int x = (int) Math.floor(event.getX() / TILE_SIZE);
		int y = (int) Math.floor(event.getY() / TILE_SIZE);

		if (m.addItemToTile(it, x, y)) {
			System.out.println("Passes 1");
			int state;
			if (it.equals(StopSign.NAME)) {
				state = StopSign.MAX_STATES;
				System.out.println(" > StopSign added");
			} else {
				System.out.println(" > Sterile added");
				state = -1;
				System.out.println("Checking tiles");
				m.checkTiles(x, y);
				System.out.println("Finished checking");
			}
			addItemToMap(it, x, y, state);
		} else {
			System.out.println("Fails");
		}
	}
	
	/**
	 * Adds item of enum it onto map, using xy coorindates, based on its state.
	 * 
	 * @param it		type of item
	 * @param x			x pos on map
	 * @param y			y pos on map
	 * @param state		state of item
	 */
	public static void addItemToMap(String it, int x, int y, int state) {
		drawItemToMap(it, x, y, state);
	}
	
	/**
	 * Draws item of enum it onto map, using xy coordinates, based on its state.
	 * 
	 * @param it		type of item
	 * @param x			x pos on map
	 * @param y			y pos on map
	 * @param state		state of item
	 */
	public static void drawItemToMap(String it, int x, int y, int state) {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		Image i;
		if (it.equals(StopSign.NAME)) {
			i = StopSign.IMAGE;
		} else {
			i = Sterilisation.IMAGE;
		}
		gc.drawImage(i, x * TILE_SIZE, y * TILE_SIZE);
	}

	/**
	 * Creates the top menu bar. Contains menu options and timer. Will need to
	 * reorganise.
	 * 
	 * @return the menu
	 */
	private static Pane createTopMenu() {
		HBox root = new HBox();

		return root;
	}

	/**
	 * Creates the right menu - will contain level, points and items.
	 * 
	 * @return the right menu
	 */
	private static Node createRightMenu() {
		VBox root = new VBox();
		root.setSpacing(10);
		root.setPadding(new Insets(10, 10, 10, 10));
		
		setUpDraggleableImages(root);
		setUpHandling();

		/**
		 * Allows canvas to received dragged object within its bounds.
		 */
		itemCanvas.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				if (event.getGestureSource() == draggableSterilise) {
					// Mark the drag event as acceptable by the canvas.
					event.acceptTransferModes(TransferMode.ANY);
					// Consume the event. This means we mark it as dealt with.
					event.consume();
				}
				
				if (event.getGestureSource() == draggableStop) {
					// Mark the drag event as acceptable by the canvas.
					event.acceptTransferModes(TransferMode.ANY);
					// Consume the event. This means we mark it as dealt with.
					event.consume();
				}
			}
		});

		/**
		 * Allows canvas to react to dragged object when finally dropped.
		 */
		itemCanvas.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				itemCanvasDragDropOccurred(event);
				event.consume();
			}
		});

		return root;
	}

	/**
	 * Initiliase draggleable images. Expand VBox.
	 */
	private static void setUpDraggleableImages(VBox root) {
		root.getChildren().add(draggableSterilise);
		root.getChildren().add(draggableStop);
	}

	private static void setUpHandling() {
		ClipboardContent content = new ClipboardContent();
		content.putString("Hello");
		
		draggableStop.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = draggableStop.startDragAndDrop(TransferMode.ANY);
				db.setContent(content);
				event.consume();
			}
		});
		
		draggableSterilise.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = draggableSterilise.startDragAndDrop(TransferMode.ANY);
				db.setContent(content);
				event.consume();
			}
		});
	}

	/**
	 * Creates game GUI.
	 * 
	 * @return the GUI
	 */
	private static BorderPane createGameGUI() {

		BorderPane root = new BorderPane();
		root.setCenter(createCenterMap());
		root.setTop(createTopMenu());
		root.setRight(createRightMenu());
		root.setBottom(createBottomDisplay());
		root.setLeft(createLeftDisplay());

		String map1 = "GGGGGGGGGGGGGGGGGGPPPPPPPJPPPPPPPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGJPPPPPPJPPPPPPJGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPPPPPPPJPPPPPPPGGGGGGGGGGGGGGGGGG";
		m = new Board(map1, 17, 11);
		drawMap();

		return root;
	}

	/**
	 * Creates the bottom display, used to display the message of the day.
	 * 
	 * @return HBox of the bottom display.
	 */
	private static Node createBottomDisplay() {
		HBox root = new HBox();
		return root;
	}

	/**
	 * Creates the left display, used to display the number of rat indicators.
	 * 
	 * @return
	 */
	private static Node createLeftDisplay() {
		VBox root = new VBox();
		return root;
	}

	/**
	 * Creates the game canvas in window. Will have the Board, Rats and Items.
	 * 
	 * @return Game canvas
	 */
	private static Node createCenterMap() {
		Pane root = new Pane();
		// Creating canvases
		baseCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		mapCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		ratCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		itemCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);

		// Adding canvas to pane
		root.getChildren().add(baseCanvas);
		root.getChildren().add(ratCanvas);
		root.getChildren().add(mapCanvas);
		root.getChildren().add(itemCanvas);

		GraphicsContext gc = baseCanvas.getGraphicsContext2D();
		gc.setFill(Color.GRAY);
		gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

		return root;
	}

	/**
	 * Draws map onto screen
	 */
	private static void drawMap() {
		GraphicsContext gc = mapCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		m.drawBoard(gc);
	}
}
