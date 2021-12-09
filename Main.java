import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.time.LocalTime;

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
import javafx.scene.paint.Color;
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
public class Main extends Application {
	/**
	 * Width of the window in pixels.
	 */
	private static final int WINDOW_WIDTH = 960;

	/**
	 * Height of the window in pixels.
	 */
	private static final int WINDOW_HEIGHT = 600;

	/**
	 * Width of the game canvas in pixels.
	 */
	private static final int CANVAS_WIDTH = 850;

	/**
	 * Height of the game canvas in pixels.
	 */
	private static final int CANVAS_HEIGHT = 550;

	/**
	 * Height and width of a Tile in pixels.
	 */
	public static final int TILE_SIZE = 50;

	/**
	 * Position multiplier of where rat is.
	 */
	public static final int RAT_POSITION = 25;

	/**
	 * Time in miliseconds between each rat steps. 100
	 */
	public static final int TIME_BETWEEN_STEPS = 10;

	/**
	 * Time between each cycle in miliseconds. 4
	 */
	public static final int CYCLE_TIME = 500;

	/**
	 * Speed of adult rat. Baby rats are 2x.
	 */
	public static final int NORMAL_RAT_SPEED = 25;

	/**
	 * Offset needed to center the Rat along the x axis in pixels.
	 */
	public static final int TILE_X_OFFSET = 10;

	/**
	 * Offset needed to center the Rat along the y axis in pixels.
	 */
	private static final int TILE_Y_OFFSET = 10;

	/**
	 * Draggable image for stop sign.
	 */
	ImageView draggableStop = new ImageView();

	/**
	 * Draggable image for bomb.
	 */
	ImageView draggableBomb = new ImageView();

    /**
     * Draggable image for sex change (Male to Female) item.
     */
    ImageView draggableSexToFemale = new ImageView();

    /**
     * Draggable image for sex change (Female to Male) item.
     */
    ImageView draggableSexToMale = new ImageView();

    /**
     * Draggable image for sterilise item.
     */
    ImageView draggableSterilise = new ImageView();

    /**
     * Draggable image for death rat.
     */
    ImageView draggableDeathRat = new ImageView();

	/**
	 * Draggable image for poison.
	 */
	ImageView draggablePoison = new ImageView();

	/**
	 * Board of the game
	 */
	private Board m;

	/**
	 * Width of the rat in pixels.
	 */
	public static int RAT_WIDTH;

	/**
	 * Height of the rat in pixels.
	 */
	public static int RAT_HEIGHT;

	/**
	 * Canvas of map tiles.
	 */
	private Canvas mapCanvas;

	/**
	 * Grey background.
	 */
	private Canvas baseCanvas;

	/**
	 * Canvas for all rat classes + death rat.
	 */
	private Canvas ratCanvas;

	/**
	 * Canvas for all items not including death rat.
	 */
	private static Canvas itemCanvas;

	/**
	 * Level number of current level.
	 */
	private Label currLevel;

	/**
	 * Number of points accumulated in level so far.
	 */
	private Label currPoints;
	
	private static HashMap<ItemType, ArrayList<int[]>> itemPlace;

	/**
	 * x y coordinates of all stop signs
	 */
	private static ArrayList<int[]> stopSignPlace;

	/**
	 * x y coordinates of all bomb placements
	 */
	private static ArrayList<int[]> bombPlace;

	/**
	 * x y coordinates of all poison placements
	 */
	private static ArrayList<int[]> poisonPlace;

    /**
     * x y coordinates of all sex change (Male to Female) placements.
     */
	private static ArrayList<int[]> sexToFemalePlace;

    /**
     * x y coordinates of all sex change (Female to Male) placements.
     */
	private static ArrayList<int[]> sexToMalePlace;

    /**
     * x y coordinates of all sterilise item placements.
     */
	private static ArrayList<int[]> sterilisePlace;

	/**
	 * The Rats in the game window which needs to move.
	 */
	private static HashMap<RatType, HashMap<Direction, ArrayList<int[]>>> currMovement;

	/**
	 * Iterating over moving the rat.
	 */
	private Timeline ratMoveTimeline;

	/**
	 * The main cycle that runs the game.
	 */
	private Timeline cycler;

	/**
	 * Number of steps rat has taken during current iteration of rat movement. 2x
	 * for baby rats and death rats.
	 */
	private int step;

	/**
	 * Time when the game started.
	 */
	private LocalTime startTime;

	/**
	 * Max time to complete game in seconds.
	 */
	private int maxTime;
	
	/**
	 * Players name.
	 */
	private String playerName;
	
	/**
	 * If user themselves has told the game to stop.
	 */
	private boolean playerStopGame;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Scene scene = null;
		
		BorderPane root = createGameGUI(GameMasterExample.getMap(), GameMasterExample.getRats(), 
				GameMasterExample.getItems(), GameMasterExample.getMaxTime(), GameMasterExample.getMaxRats(),
				GameMasterExample.getName());
		
		cycler = new Timeline(new KeyFrame(Duration.millis(CYCLE_TIME), event -> runCycle()));
		cycler.setCycleCount(Animation.INDEFINITE);
		cycler.play();
		
		scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Adds to list of Rat movements on the game canvas.
	 * 
	 * @param pos  xy position of the rat
	 * @param dir  direction the rat is facing
	 * @param rt   type of rat
	 * @param move movement status of rat
	 */
	public static void addCurrMovement(int[] pos, Direction dir, RatType rt, int move) {
		currMovement.putIfAbsent(rt, new HashMap<Direction, ArrayList<int[]>>());
		currMovement.get(rt).putIfAbsent(dir, new ArrayList<int[]>());
		if (move == 0) {
			currMovement.get(rt).get(dir).add(new int[] { pos[0], pos[1], 0, 4 });
		} else {
			currMovement.get(rt).get(dir).add(new int[] { pos[0], pos[1], move });
		}
	}

	/**
	 * Adds to list of Rat movements on the game canvas.
	 * 
	 * @param pos  xy position of the rat
	 * @param dir  direction the rat is facing
	 * @param rt   type of rat
	 * @param move movement status of rat
	 * @param steps move limit of the rat
	 */
	public static void addCurrMovement(int[] pos, Direction dir, RatType rt, int move, int steps) {
		currMovement.putIfAbsent(rt, new HashMap<Direction, ArrayList<int[]>>());
		currMovement.get(rt).putIfAbsent(dir, new ArrayList<int[]>());
		currMovement.get(rt).get(dir).add(new int[] { pos[0], pos[1], move, steps });
	}

	/**
	 * Removes items from board.
	 * @param item the item to be removed
	 * @param pos x y coordinates of where item is located on board.
	 */
	public static void removeItem(Item item, int[] pos) {
		ArrayList<int[]> arr = itemPlace.get(ItemType.fromItem(item));
		
		if (arr != null) {
			int[] a = null;
			for (int[] i : arr) {
				if (i[0] == pos[0] && i[1] == pos[1]) {
					a = i;
				}
			}
			arr.remove(a);
		} else {
			System.err.println("Item cannot be removed\n" + item);
		}
	}
	
	/**
	 * Draws a Poison on this location on the board.
	 * @param x x position of the Poison
	 * @param y y posision of the Poison
	 */
	public static void drawPoison(int x, int y) {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.drawImage(Poison.IMAGE, x * TILE_SIZE, y * TILE_SIZE);
	}

	/**
	 * Reacts to item that is dragged onto canvas.
	 * @param event The drag event itself which contains data about the drag that
	 *              occured.
	 */
	public void itemCanvasDragDropOccurred(DragEvent event) {		
		if (event.getGestureSource() == draggableStop) {
			placeStopSign(event);
		} else if (event.getGestureSource() == draggableBomb) {
			placeBomb(event);
		} else if (event.getGestureSource() == draggablePoison) {
			placePoison(event);
		} else if (event.getGestureSource() == draggableSexToFemale) {
			placeSexToFemale(event);
		} else if (event.getGestureSource() == draggableSexToMale) {
			placeSexToMale(event);
		} else if (event.getGestureSource() == draggableSterilise) {
			placeSterilise(event);
		} else if (event.getGestureSource() == draggableDeathRat) {
			placeDeathRat(event);
		}
	}
	
	/**
	 * Redraws all items.
	 */
	private void drawItems() {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);		
		for (int[] i : bombPlace) {
			gc.drawImage(Bomb.getImage(i[2]), i[1] * TILE_SIZE, i[0] * TILE_SIZE);
		}
		for (ItemType it : ItemType.values()) {
			try {
				ArrayList<int[]> loc = itemPlace.get(it);
				if (loc != null) {
					for (int[] i : loc) {
						it.draw(i[1], i[0], i[2]);
					}
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * React when an object is dragged onto the canvas.
	 * 
	 * @param event The drag event itself which contains data about the drag that
	 *              occurred.
	 * @author Liam O'Reilly
	 * @author Jing Shiang Gu
	 * 
	 *         TODO Can check list, can check tile, I've had it check Tile - J Could
	 *         also use set, add it and check length
	 */
	private void placeStopSign(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);

		if (m.addStopSign((int) x, (int) y)) {
			addStopSign((int) x, (int) y, StopSign.MAX_STATES);
		}
	}
	
	public static void addStopSign(int x, int y, int states) {
		itemPlace.putIfAbsent(ItemType.STOPSIGN, new ArrayList<>());
		itemPlace.get(ItemType.STOPSIGN).add(new int[] {y, x, states});
		drawStopSign(x, y, states);
	}
	
	/**
	 * Draws a stop sign on this location on the board.
	 * @param x x position of the StopSign
	 * @param y y posision of the StopSign
	 * @param state the state of the StopSign
	 */
	public static void drawStopSign(int x, int y, int state) {
		stopSignPlace.add(new int[] { (int) y, (int) x, state });
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.drawImage(StopSign.getImageState(state), x * TILE_SIZE, y * TILE_SIZE);
	}

	/**
	 * Update the graphical state of the Stop Sign.
	 * 
	 * @param pos   xy position of the Stop Sign
	 * @param state the state it is in
	 */
	public static void damageStopSign(int[] pos, int state) {
		// Will need to think about this Currently StopSign calls damage
		ArrayList<int[]> stopSignPlace = itemPlace.get(ItemType.STOPSIGN);
		if (state != 0) {
			int[] xyPos = null;
			for (int[] i : stopSignPlace) {
				if (i[0] == pos[0] && i[1] == pos[1]) {
					xyPos = i;
				}
			}
			xyPos[2] = state;
		}
	}

    /**
     * React when an object is dragged onto the canvas.
     *
     * @param event The drag event itself which contains data about the drag that
     *              occurred.
     * @author Andrew Wu
     */
	private void placeBomb(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);

		if (m.addBomb((int) x, (int) y)) {
			addBomb((int) x, (int) y);
		}
	}
	
	public static void addBomb(int x, int y) {
		itemPlace.putIfAbsent(ItemType.BOMB, new ArrayList<>());
		itemPlace.get(ItemType.BOMB).add(new int[] {y, x, Bomb.START_COUNTDOWN });
		drawBomb(x, y);
	}
	
	/**
	 * Draw a bomb on this location on the board.
	 * @param x x position of the Bomb
	 * @param y y posision of the Bomb
	 */
	public static void drawBomb(int x, int y) {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.drawImage(Bomb.IMAGE, x * TILE_SIZE, y * TILE_SIZE);
	}

    /**
     * Updates value of bomb responsible for the remaining time till detonation.
     * @param n remaining time
     * @param x x-coordinate of bomb placement
     * @param y y-coordinate of bomb placement
     */
	public static void editBombCountdown(int n, int x, int y) {
		ArrayList<int[]> bombPlace = itemPlace.get(ItemType.BOMB);
		for (int[] i : bombPlace) {
			if (i[0] == y && i[1] == x) {
				i[2] = n;
			}
		}
	}

    /**
     * React when an object is dragged onto the canvas.
     *
     * @param event The drag event itself which contains data about the drag that
     *              occurred.
     * @author Andrew Wu
     */
	private void placePoison(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);

		if (m.addPoison((int) x, (int) y)) {
			addPoison((int) x, (int) y);
		}
	}
	
	private static void addPoison(int x, int y) {
		itemPlace.putIfAbsent(ItemType.POISON, new ArrayList<>());
		itemPlace.get(ItemType.POISON).add(new int[] {y, x, -1 });
		drawPoison(x, y);
	}

    /**
     * React when an object is dragged onto the canvas.
     *
     * @param event The drag event itself which contains data about the drag that
     *              occurred.
     * @author Andrew Wu
     */
	private void placeSexToFemale(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);

		if (m.addSexToFemale((int) x, (int) y)) {
			addSexToFemale((int) x, (int) y);
		}
	}
	
	public static void addSexToFemale(int x, int y) {
		itemPlace.putIfAbsent(ItemType.SEX_TO_FEMALE, new ArrayList<>());
		itemPlace.get(ItemType.SEX_TO_FEMALE).add(new int[] {y, x, -1});
		drawSexToFemale(x, y);
	}
	
	/**
	 * Draws a sexToFemaleIcon on this location on the board.
	 * @param x x position of the sexToFemaleIcon
	 * @param y y posision of the sexToFemaleIcon
	 */
	public static void drawSexToFemale(int x, int y) {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.drawImage(SexChangeToFemale.IMAGE, x * TILE_SIZE, y * TILE_SIZE);
	}

    /**
     * React when an object is dragged onto the canvas.
     *
     * @param event The drag event itself which contains data about the drag that
     *              occurred.
     * @author Andrew Wu
     */
	private void placeSexToMale(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);

		if (m.addSexToMale((int) x, (int) y)) {
			addSexToMale((int) x, (int) y);
		}
	}
	
	public static void addSexToMale(int x, int y) {
		itemPlace.putIfAbsent(ItemType.SEX_TO_MALE, new ArrayList<>());
		itemPlace.get(ItemType.SEX_TO_MALE).add(new int[] {y, x, -1});
		drawSexToMale(x, y);
	}
	
	/**
	 * Draws a sexToMaleIcon on this location on the board.
	 * @param x x position of the sexToMaleIcon
	 * @param y y posision of the sexToMaleIcon
	 */
	public static void drawSexToMale(int x, int y) {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.drawImage(SexChangeToMale.IMAGE, x * TILE_SIZE, y * TILE_SIZE);
	}

    /**
     * React when an object is dragged onto the canvas.
     *
     * @param event The drag event itself which contains data about the drag that
     *              occurred.
     * @author Andrew Wu
     */
	private void placeSterilise(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);

		if (m.addSterilise((int) x, (int) y)) {
			addSterilise((int) x, (int) y);
		}
	}
	
	public static void addSterilise(int x, int y) {
		itemPlace.put(ItemType.STERILISATION, new ArrayList<>());
		itemPlace.get(ItemType.STERILISATION).add(new int[] {y, x, -1});
		drawSterilise(x, y);
	}
	
	/**
	 * Draws a steraliseIcon on this location on the board.
	 * @param x x position of the steraliseIcon
	 * @param y y posision of the steraliseIcon
	 */
	public static void drawSterilise(int x, int y) {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.drawImage(Sterilisation.IMAGE, x * TILE_SIZE, y * TILE_SIZE);
	}

    /**
     * React when an object is dragged onto the canvas.
     *
     * @param event The drag event itself which contains data about the drag that
     *              occurred.
     */
	private void placeDeathRat(DragEvent event) {
		int x = (int) Math.floor(event.getX() / TILE_SIZE);
		int y = (int) Math.floor(event.getY() / TILE_SIZE);
		m.placeRat(new DeathRat(), Direction.NORTH, y, x);
	}

	/**
	 * Creates the top menu bar. Contains menu options.
	 * 
	 * @return the menu
	 */
	private HBox createTopMenu() {
		HBox root = new HBox();
		root.setSpacing(10);
		root.setPadding(new Insets(10, 10, 10, 10));

		MenuBar menuBar = new MenuBar();

		Menu menuFile = new Menu("File");

		MenuItem add = new MenuItem("Save");
		menuFile.getItems().add(add);

		Menu optionFile = new Menu("Option");

		menuBar.getMenus().addAll(menuFile, optionFile);
		root.getChildren().addAll(menuBar);
		
		Button stopGame = new Button("Stop and save game");
		root.getChildren().addAll(stopGame);
		stopGame.setOnAction(e -> {
			playerStopGame = true;
		});

		Label msg = new Label(MessageOfDay.getMsgDay());
		root.getChildren().add(msg);

		return root;
	}

	/**
	 * Creates the right menu - will contain level, points and items.
	 * 
	 * @return the right menu
	 */
	private VBox createRightMenu() {
		VBox root = new VBox();
		root.setSpacing(10);
		root.setPadding(new Insets(10, 10, 10, 10));

		currLevel = new Label("Level xx");
		currLevel.setFont(new Font(20));
		currLevel.setTextAlignment(TextAlignment.CENTER);
		root.getChildren().add(currLevel);

		currPoints = new Label("Points xx");
		currPoints.setFont(new Font(20));
		currLevel.setTextAlignment(TextAlignment.CENTER);
		root.getChildren().add(currPoints);
		
		setUpDraggleableImages(root);
		setUpHandling();
		
		/**
		 * 	Allows canvas to received dragged object within its bounds.
		 */
		itemCanvas.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				ImageView[] goodImages = new ImageView[] { draggableStop, draggableBomb,
						draggablePoison, draggableSexToFemale, draggableSexToMale,
						draggableSterilise, draggableDeathRat
				};
				// Mark the drag as acceptable if the source was the draggable image.
				// (for example, we don't want to allow the user to drag things or files into
				// our application)
				for (ImageView i : goodImages) {
					if  (event.getGestureSource() == i) {
						// Mark the drag event as acceptable by the canvas.
						event.acceptTransferModes(TransferMode.ANY);
						// Consume the event. This means we mark it as dealt with.
						event.consume();
					}
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
	private void setUpDraggleableImages(VBox root) {
		draggableStop.setImage(StopSign.IMAGE);
		root.getChildren().add(draggableStop);

		draggableBomb.setImage(Bomb.IMAGE);
		root.getChildren().add(draggableBomb);

		draggablePoison.setImage(Poison.IMAGE);
		root.getChildren().add(draggablePoison);

		draggableSexToFemale.setImage(SexChangeToFemale.IMAGE);
		root.getChildren().add(draggableSexToFemale);

		draggableSexToMale.setImage(SexChangeToMale.IMAGE);
		root.getChildren().add(draggableSexToMale);

		draggableSterilise.setImage(Sterilisation.IMAGE);
		root.getChildren().add(draggableSterilise);

		draggableDeathRat.setImage(DeathRat.IMAGE);
		root.getChildren().add(draggableDeathRat);
	}
	
	private void setUpHandling() {
		ClipboardContent content = new ClipboardContent();
		content.putString("Hello");
		
		draggableStop.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = draggableStop.startDragAndDrop(TransferMode.ANY);
				db.setContent(content);
				event.consume();
			}
		});
		draggableBomb.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = draggableBomb.startDragAndDrop(TransferMode.ANY);
				db.setContent(content);
				event.consume();
			}
		});
		draggablePoison.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = draggablePoison.startDragAndDrop(TransferMode.ANY);
				db.setContent(content);
				event.consume();
			}
		});
		draggableSexToFemale.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = draggableSexToFemale.startDragAndDrop(TransferMode.ANY);
				db.setContent(content);
				event.consume();
			}
		});
		draggableSexToMale.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = draggableSexToMale.startDragAndDrop(TransferMode.ANY);
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
		draggableDeathRat.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = draggableDeathRat.startDragAndDrop(TransferMode.ANY);
				db.setContent(content);
				event.consume();
			}
		});
	}

	/**
	 * Creates game GUI.
	 * 
	 * @param map the map design
	 * @param rats list of rats and their positions
	 * @param items list of items and their positions
	 * @param maxTime maximum amount of time to finish the game
	 * @param maxRats maximum number of rats before the game ends
	 * @param name name of the player
	 * @return the GUI
	 */
	private BorderPane createGameGUI(String map, ArrayList<String> rats,
			ArrayList<String> items, int maxTime, int maxRats, String name) {
			
		setInitialValues();
		
		this.playerName = name;
		
		startTime = LocalTime.now();
		this.maxTime = maxTime;
		RatController.setRatController(maxRats);
		
		BorderPane root = null;
		
		root = new BorderPane();
		root.setCenter(createCenterMap());
		root.setTop(createTopMenu());
		root.setRight(createRightMenu());
		try {
			
		} catch (NullPointerException n) {
			n.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		m = new Board(map, 17, 11);
		m.setUpRats(rats);
		m.setUpItems(items);
		drawMap();
		moveRat();

		return root;
	}
	
	private void setInitialValues() {
		
		// These no choice
		RAT_WIDTH = 30;
		RAT_HEIGHT = 45;
		stopSignPlace = new ArrayList<>();
		bombPlace = new ArrayList<>();
		poisonPlace = new ArrayList<>();
		sexToFemalePlace = new ArrayList<>();
		sexToMalePlace = new ArrayList<>();
		sterilisePlace = new ArrayList<>();
		
		itemPlace = new HashMap<>();
		
		playerStopGame = false;
	}

	/**
	 * Creates the game canvas in window. Will have the Board, Rats and Items.
	 * 
	 * @return Game canvas
	 */
	private Pane createCenterMap() {
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
	private void drawMap() {
		GraphicsContext gc = mapCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		m.drawBoard(gc);
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

	/**
	 * IMPORTANT This method will run in a cycle indefinitely until stopped,
	 * currently allows rats to move around. TODO Check if time is over TODO Level
	 * from Game Master
	 */
	private void runCycle() {
		currMovement = new HashMap<>();
		step = 0;
		m.runAllTiles();
		ratMoveTimeline.play();
		currPoints.setText(String.valueOf(RatController.getPoints()));
		drawItems();

		// Losing conditions
		if (!RatController.continueGame() && LocalTime.now().getSecond() - startTime.getSecond() > maxTime) {
			cycler.stop();
			System.out.println("Game has finished");
			// Pass control back to game master, game has finished
		} else if (playerStopGame) {
			cycler.stop();
			saveState();
		}
	}
	
	private void saveState() {
		m.saveState(playerName + ".txt");
	}

	/**
	 * Criteria for Rat movements.
	 */
	private void moveRat() {
		ratMoveTimeline = new Timeline(new KeyFrame(Duration.millis(TIME_BETWEEN_STEPS), event -> goThroughRat()));
		ratMoveTimeline.setCycleCount(NORMAL_RAT_SPEED);
	}

	/**
	 * Goes through each type of rats to draw them onto canvas
	 */
	private void goThroughRat() {
		GraphicsContext gc = ratCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		step += 1;
		for (RatType rt : RatType.values()) {
			drawRat(rt);
		}
	}

	/**
	 * Draws the rats onto the game canvas.
	 * 
	 * @param smallerList type of rat you're dealing with
	 * @param ratImage    image of rat
	 */
	private void drawRat(RatType rt) {
		HashMap<Direction, ArrayList<int[]>> smallerList = currMovement.get(rt);
		if (smallerList != null) {
			GraphicsContext gc = ratCanvas.getGraphicsContext2D();

			Image[] ratImage = rt.getImage();
			int size = rt.getSize();
			int width = RAT_WIDTH / size;
			int height = RAT_HEIGHT / size;
			int speed = rt.getSpeed();

			// List of rat positions and direction
			ArrayList<int[]> currDirection;
			currDirection = smallerList.get(Direction.NORTH);
			if (currDirection != null) {
				for (int[] i : currDirection) {
					if (i[2] == 0) {
						if (step <= NORMAL_RAT_SPEED / (4 / i[3])) {
							gc.drawImage(ratImage[0], i[1] * RAT_POSITION + (TILE_X_OFFSET * size),
									i[0] * RAT_POSITION + (TILE_SIZE / 4 * (size - 1)), width, height);
						}
					} else if (step <= NORMAL_RAT_SPEED / (4 / i[2])) {
						gc.drawImage(ratImage[0], i[1] * RAT_POSITION + (TILE_X_OFFSET * size),
								i[0] * RAT_POSITION - step * speed + (TILE_SIZE / 4 * (size - 1)), width, height);
					}
				}
			}

			currDirection = smallerList.get(Direction.EAST);
			if (currDirection != null) {
				for (int[] i : currDirection) {
					if (i[2] == 0) {
						if (step <= NORMAL_RAT_SPEED / (4 / i[3])) {
							gc.drawImage(ratImage[1], i[1] * RAT_POSITION,
									i[0] * RAT_POSITION + TILE_Y_OFFSET + (TILE_SIZE / 4 * (size - 1)), height, width);
						}
					} else if (step <= NORMAL_RAT_SPEED / (4.0 / i[2])) {
						gc.drawImage(ratImage[1], i[1] * RAT_POSITION + step * speed,
								i[0] * RAT_POSITION + TILE_Y_OFFSET + (TILE_SIZE / 4 * (size - 1)), height, width);
					}
				}
			}

			currDirection = smallerList.get(Direction.SOUTH);
			if (currDirection != null) {
				for (int[] i : currDirection) {
					if (i[2] == 0) {
						if (step <= NORMAL_RAT_SPEED / (4 / i[3])) {
							gc.drawImage(ratImage[2], i[1] * RAT_POSITION + (TILE_X_OFFSET * size),
									i[0] * RAT_POSITION + (TILE_SIZE / 4 * (size - 1)), width, height);
						}
					} else if (step <= NORMAL_RAT_SPEED / (4 / i[2])) {
						gc.drawImage(ratImage[2], i[1] * RAT_POSITION + (TILE_X_OFFSET * size),
								i[0] * RAT_POSITION + step * speed + (TILE_SIZE / 4 * (size - 1)), width, height);
					}
				}
			}

			currDirection = smallerList.get(Direction.WEST);
			if (currDirection != null) {
				for (int[] i : currDirection) {
					if (i[2] == 0) {
						if (step <= NORMAL_RAT_SPEED / (4 / i[3])) {
							gc.drawImage(ratImage[3], i[1] * RAT_POSITION,
									i[0] * RAT_POSITION + TILE_Y_OFFSET + (TILE_SIZE / 4 * (size - 1)), height, width);
						}
					} else if (step <= NORMAL_RAT_SPEED / (4 / i[2])) {
						gc.drawImage(ratImage[3], i[1] * RAT_POSITION - step * speed,
								i[0] * RAT_POSITION + TILE_Y_OFFSET + (TILE_SIZE / 4 * (size - 1)), height, width);
					}
				}
			}
		}
	}
}
