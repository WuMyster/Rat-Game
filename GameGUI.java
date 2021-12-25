import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
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
import javafx.util.Duration;

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
	 * Position multiplier of where rat is.
	 */
	private static final int RAT_POSITION = 25;

	/**
	 * Time in miliseconds between each rat steps. 100
	 */
	private static final int TIME_BETWEEN_STEPS = 10;

	/**
	 * Time between each cycle in miliseconds. 4
	 */
	private static final int CYCLE_TIME = 500;

	/**
	 * Speed of adult rat. Baby rats are 2x.
	 */
	private static final int NORMAL_RAT_SPEED = 25;

	/**
	 * Offset needed to center the Rat along the x axis in pixels.
	 */
	private static final int TILE_X_OFFSET = 10;

	/**
	 * Offset needed to center the Rat along the y axis in pixels.
	 */
	private static final int TILE_Y_OFFSET = 10;

	/**
	 * Draggable image for stop sign.
	 */
	private static ImageView draggableStop = new ImageView();

	/**
	 * Draggable image for bomb.
	 */
	private static ImageView draggableBomb = new ImageView();

	/**
	 * Draggable image for sex change (Male to Female) item.
	 */
	private static ImageView draggableSexToFemale = new ImageView();

	/**
	 * Draggable image for sex change (Female to Male) item.
	 */
	private static ImageView draggableSexToMale = new ImageView();

	/**
	 * Draggable image for sterilise item.
	 */
	private static ImageView draggableSterilise = new ImageView();

	/**
	 * Draggable image for death rat.
	 */
	private static ImageView draggableDeathRat = new ImageView();

	/**
	 * Draggable image for poison.
	 */
	private static ImageView draggablePoison = new ImageView();

	/**
	 * Draggable image for poison.
	 */
	private static ImageView draggableGas = new ImageView();

	/**
	 * Board of the game
	 */
	private static Board m;

	/**
	 * Width of the rat in pixels.
	 */
	private static int RAT_WIDTH = 30;

	/**
	 * Height of the rat in pixels.
	 */
	private static int RAT_HEIGHT = 45;

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
	 * Level number of current level.
	 */
	private static Label currLevel;

	/**
	 * Number of points accumulated in level so far.
	 */
	private static Label currPoints;

	/**
	 * Time left to finish the game.
	 */
	private static Label currTime;

	/**
	 * Location of all items.
	 */
	private static HashMap<ItemType, ArrayList<int[]>> itemPlace;

	/**
	 * The Rats in the game window which needs to move.
	 */
	private static HashMap<RatType, HashMap<Direction, ArrayList<int[]>>> currMovement;

	/**
	 * Iterating over moving the rat.
	 */
	private static Timeline ratMoveTimeline;

	/**
	 * The main cycle that runs the game.
	 */
	private static Timeline cycler;

	/**
	 * Time limit of the game.
	 */
	private static Timeline timeLimit;

	/**
	 * Number of steps rat has taken during current iteration of rat movement. 2x
	 * for baby rats and death rats.
	 */
	private static int step;

	/**
	 * Max time to complete game in seconds.
	 */
	private static int maxTime;

	/**
	 * Players name.
	 */
	private static String playerName;

	/**
	 * If user themselves has told the game to stop.
	 */
	private static boolean playerStopGame;

	/**
	 * Stacked barchart showing the number of male and female rats alive on the
	 * board.
	 */
	private static StackedBarChart<String, Number> sbcRatIndicator;

	/**
	 * Creates and displays the game window, the rat alive indicator and starts the
	 * game.
	 */
	public static void startGameScreen() {
		BorderPane gameRoot = createGameGUI();
		Scene scene = new Scene(gameRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		Main.setWindow(scene);

		cycler = new Timeline(new KeyFrame(Duration.millis(CYCLE_TIME), event -> runCycle()));
		cycler.setCycleCount(Animation.INDEFINITE);

		timeLimit = new Timeline(new KeyFrame(Duration.seconds(1), event -> decrementTimer()));
		timeLimit.setCycleCount(Animation.INDEFINITE); // Know this isn't infinite, but will think about this
														// Can it call method when count is over?
		playGame();
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
	 * @param pos   xy position of the rat
	 * @param dir   direction the rat is facing
	 * @param rt    type of rat
	 * @param move  movement status of rat
	 * @param steps move limit of the rat
	 */
	public static void addCurrMovement(int[] pos, Direction dir, RatType rt, int move, int steps) {
		currMovement.putIfAbsent(rt, new HashMap<Direction, ArrayList<int[]>>());
		currMovement.get(rt).putIfAbsent(dir, new ArrayList<int[]>());
		currMovement.get(rt).get(dir).add(new int[] { pos[0], pos[1], move, steps });
	}

	/**
	 * Removes items from board.
	 * 
	 * @param item the item to be removed
	 * @param pos  x y coordinates of where item is located on board.
	 */
	public static void removeItem(ItemType it, int[] pos) {
		ArrayList<int[]> arr = itemPlace.get(it);

		if (arr != null) {
			int[] a = null;
			for (int[] i : arr) {
				if (i[0] == pos[0] && i[1] == pos[1]) {
					a = i;
				}
			}
			arr.remove(a);
		} else {
			// System.err.println("Item cannot be removed\n" + it);
		}
	}

	/**
	 * Reacts to item that is dragged onto canvas.
	 * 
	 * @param event The drag event itself which contains data about the drag that
	 *              occured.
	 */
	public static void itemCanvasDragDropOccurred(DragEvent event) {
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
		} else if (event.getGestureSource() == draggableGas) {
			placeGas(event);
		} else {
			System.err.println("Dragging fail!!");
		}
	}

	/**
	 * Redraws all items.
	 */
	private static void drawItems() {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
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
	 */
	private static void placeStopSign(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);

		if (m.addStopSign((int) x, (int) y)) {
			addStopSign((int) x, (int) y, StopSign.MAX_STATES);
		}
	}

	/**
	 * Adds a StopSign to the board and draws it.
	 * 
	 * @param x      x position of the StopSign
	 * @param y      y position of the StopSign
	 * @param states the state of the StopSign
	 */
	public static void addStopSign(int x, int y, int states) {
		itemPlace.putIfAbsent(ItemType.STOPSIGN, new ArrayList<>());
		itemPlace.get(ItemType.STOPSIGN).add(new int[] { y, x, states });
		drawStopSign(x, y, states);
	}

	/**
	 * Draws a stop sign on this location on the board.
	 * 
	 * @param x     x position of the StopSign
	 * @param y     y posision of the StopSign
	 * @param state the state of the StopSign
	 */
	public static void drawStopSign(int x, int y, int state) {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.drawImage(StopSign.getImageState(state), x * TILE_SIZE, y * TILE_SIZE);
	}

	/**
	 * Update the graphical state of the StopSign.
	 * 
	 * @param pos   xy position of the StopSign
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
	private static void placeBomb(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);

		if (m.addBomb((int) x, (int) y)) {
			addBomb((int) x, (int) y, Bomb.START_COUNTDOWN);
		}
	}

	/**
	 * Adds a Bomb Object onto the board with specified x y coorindates and draws
	 * it.
	 * 
	 * @param x     x position of the Bomb
	 * @param y     y position of the Bomb
	 * @param state state of the Bomb
	 */
	public static void addBomb(int x, int y, int state) {
		itemPlace.putIfAbsent(ItemType.BOMB, new ArrayList<>());
		itemPlace.get(ItemType.BOMB).add(new int[] { y, x, state });
		drawBomb(x, y, state);
	}

	/**
	 * Draw a bomb on this location on the board.
	 * 
	 * @param x     x position of the Bomb
	 * @param y     y position of the Bomb
	 * @param state state of the Bomb
	 */
	public static void drawBomb(int x, int y, int state) {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.drawImage(Bomb.getImage(state), x * TILE_SIZE, y * TILE_SIZE);
	}

	/**
	 * Updates value of bomb responsible for the remaining time till detonation.
	 * 
	 * @param x     x-coordinate of bomb placement
	 * @param y     y-coordinate of bomb placement
	 * @param state remaining time
	 */
	public static void editBombCountdown(int x, int y, int state) {
		ArrayList<int[]> bombPlace = itemPlace.get(ItemType.BOMB);
		for (int[] i : bombPlace) {
			if (i[0] == y && i[1] == x) {
				i[2] = state;
			}
		}
		drawBomb(x, y, state);
	}

	/**
	 * React when an object is dragged onto the canvas.
	 *
	 * @param event The drag event itself which contains data about the drag that
	 *              occurred.
	 * @author Andrew Wu
	 */
	private static void placePoison(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);

		if (m.addPoison((int) x, (int) y)) {
			addPoison((int) x, (int) y);
		}
	}

	public static void addPoison(int x, int y) {
		itemPlace.putIfAbsent(ItemType.POISON, new ArrayList<>());
		itemPlace.get(ItemType.POISON).add(new int[] { y, x, -1 });
		drawPoison(x, y);
	}

	/**
	 * Draws a Poison on this location on the board.
	 * 
	 * @param x x position of the Poison
	 * @param y y posision of the Poison
	 */
	public static void drawPoison(int x, int y) {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.drawImage(Poison.IMAGE, x * TILE_SIZE, y * TILE_SIZE);
	}

	/**
	 * React when an object is dragged onto the canvas.
	 *
	 * @param event The drag event itself which contains data about the drag that
	 *              occurred.
	 * @author Andrew Wu
	 */
	private static void placeSexToFemale(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);

		if (m.addSexToFemale((int) x, (int) y)) {
			addSexToFemale((int) x, (int) y);
		}
	}

	/**
	 * Adds a SexToFemale Object onto the board from speicifed x y coordinates.
	 * 
	 * @param x x position of the SexToFemale
	 * @param y y position of the SexToFemale
	 */
	public static void addSexToFemale(int x, int y) {
		itemPlace.putIfAbsent(ItemType.SEX_TO_FEMALE, new ArrayList<>());
		itemPlace.get(ItemType.SEX_TO_FEMALE).add(new int[] { y, x, -1 });
		drawSexToFemale(x, y);
	}

	/**
	 * Draws a sexToFemaleIcon on this location on the board.
	 * 
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
	private static void placeSexToMale(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);

		if (m.addSexToMale((int) x, (int) y)) {
			addSexToMale((int) x, (int) y);
		}
	}

	/**
	 * Adds a SexToMale object using specified x y coorindates on the board.
	 * 
	 * @param x x position of the SexToMale
	 * @param y y position of the SexToMale
	 */
	public static void addSexToMale(int x, int y) {
		itemPlace.putIfAbsent(ItemType.SEX_TO_MALE, new ArrayList<>());
		itemPlace.get(ItemType.SEX_TO_MALE).add(new int[] { y, x, -1 });
		drawSexToMale(x, y);
	}

	/**
	 * Draws a sexToMaleIcon on this location on the board.
	 * 
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
	private static void placeSterilise(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);

		if (m.addSterilise((int) x, (int) y)) {
			addSterilise((int) x, (int) y);
		}
	}

	/**
	 * Adds a Sterilisation object to board with specified x y coordinates.
	 * 
	 * @param x x coordinates of the Sterilisation
	 * @param y y coordinates of the Sterilisation
	 */
	public static void addSterilise(int x, int y) {
		itemPlace.putIfAbsent(ItemType.STERILISATION, new ArrayList<>());
		itemPlace.get(ItemType.STERILISATION).add(new int[] { y, x, -1 });
		drawSterilise(x, y);
	}

	/**
	 * Draws a steraliseIcon on this location on the board.
	 * 
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
	private static void placeDeathRat(DragEvent event) {
		int x = (int) Math.floor(event.getX() / TILE_SIZE);
		int y = (int) Math.floor(event.getY() / TILE_SIZE);
		m.placeRat(new DeathRat(), Direction.NORTH, y, x);
	}

	/**
	 * Draws a Death Rat onto screen. Initial drawing of the Death rat.
	 * 
	 * @param x x position of the DeathRat
	 * @param y y position of the DeathRat
	 */
	public static void drawDeathRat(int x, int y) {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.drawImage(DeathRat.IMAGE, x * TILE_SIZE, y * TILE_SIZE);
	}

	/**
	 * React when an object is dragged onto the canvas.
	 *
	 * @param event The drag event itself which contains data about the drag that
	 *              occurred.
	 */
	public static void placeGas(DragEvent event) {
		int x = (int) Math.floor(event.getX() / TILE_SIZE);
		int y = (int) Math.floor(event.getY() / TILE_SIZE);
		if (m.addGas((int) x, (int) y)) {
			addGas((int) x, (int) y);
		}
	}

	/**
	 * Adds a Gas Object to the board from specified x y coordinates.
	 * 
	 * @param x x position of the gas
	 * @param y y position of the gas
	 */
	public static void addGas(int x, int y) {
		itemPlace.putIfAbsent(ItemType.GAS, new ArrayList<>());
		itemPlace.get(ItemType.GAS).add(new int[] { y, x, -1 });
		drawGas(x, y);
	}

	/**
	 * Draws a gas icon on this location on the board.
	 * 
	 * @param x x position of the gas icon
	 * @param y y posision of the gas icon
	 */
	public static void drawGas(int x, int y) {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.drawImage(Gas.IMAGE, x * TILE_SIZE, y * TILE_SIZE);
	}
	
	public static int getRemainingTime() {
		return maxTime;
	}

	/**
	 * Creates the top menu bar. Contains menu options and timer. Will need to
	 * reorganise.
	 * 
	 * @return the menu
	 */
	private static Pane createTopMenu() {
		HBox root = new HBox();
		root.setSpacing(10);
		root.setPadding(new Insets(10, 10, 10, 10));

		MenuBar menuBar = new MenuBar();

		Menu menuFile = new Menu("File");

		MenuItem save = new MenuItem("Save");
		save.setOnAction(e -> saveState());
		
		menuFile.getItems().add(save);

		Menu optionFile = new Menu("Option");
		MenuItem play = new MenuItem("Play");
		play.setDisable(true);
		save.setDisable(true);		

		MenuItem stop = new MenuItem("Stop");
		stop.setOnAction(e -> {
			stopGame();
			stop.setDisable(true);
			play.setDisable(false);
			save.setDisable(false);
		});

		play.setOnAction(e -> {
			playGame();
			stop.setDisable(false);
			play.setDisable(true);
			save.setDisable(true);
		});

		optionFile.getItems().addAll(play, stop);

		menuBar.getMenus().addAll(menuFile, optionFile);
		root.getChildren().addAll(menuBar);

		Button stopGame = new Button("Stop and save game");
		root.getChildren().addAll(stopGame);
		stopGame.setOnAction(e -> {
			playerStopGame = true;
		});

		currTime = new Label("Time xx");
		root.getChildren().add(currTime);

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

		currLevel = new Label("Level\n" + String.valueOf(GameMaster.getLvlNum()));
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
		 * Allows canvas to received dragged object within its bounds.
		 */
		itemCanvas.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				ImageView[] goodImages = new ImageView[] { draggableStop, draggableBomb, draggablePoison,
						draggableSexToFemale, draggableSexToMale, draggableSterilise, draggableDeathRat, draggableGas };
				// Mark the drag as acceptable if the source was the draggable image.
				// (for example, we don't want to allow the user to drag things or files into
				// our application)
				for (ImageView i : goodImages) {
					if (event.getGestureSource() == i) {
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
	private static void setUpDraggleableImages(VBox root) {
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

		draggableGas.setImage(Gas.IMAGE);
		root.getChildren().add(draggableGas);
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
		draggableGas.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = draggableGas.startDragAndDrop(TransferMode.ANY);
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

		setInitialValues();
		createRatIndicator();

		GameGUI.playerName = GameMaster.getName();

		GameGUI.maxTime = GameMaster.getMaxTime();
		RatController.setRatController(GameMaster.getMaxRats(), GameMaster.getPoints());

		BorderPane root = new BorderPane();
		root.setCenter(createCenterMap());
		root.setTop(createTopMenu());
		root.setRight(createRightMenu());
		root.setBottom(createBottomDisplay());
		root.setLeft(createLeftDisplay());

		m = new Board(GameMaster.getMap(), 17, 11);
		m.setUpRats(GameMaster.getRats());
		m.setUpItems(GameMaster.getItems());
		drawMap();
		setTimeLines();

		return root;
	}

	private static void setInitialValues() {

		// These no choice
		itemPlace = new HashMap<>();
		playerStopGame = false;
	}

	/**
	 * Creates the bottom display, used to display the message of the day.
	 * 
	 * @return HBox of the bottom display.
	 */
	private static Node createBottomDisplay() {
		HBox root = new HBox();

		Label msg = new Label(MessageOfDay.getMsgDay());
		root.getChildren().add(msg);
		root.setAlignment(Pos.TOP_CENTER);

		return root;
	}

	/**
	 * Creates the left display, used to display the number of rat indicators.
	 * 
	 * @return
	 */
	private static Node createLeftDisplay() {
		VBox root = new VBox();

		root.getChildren().add(sbcRatIndicator);
		return root;
	}

	/**
	 * Creates the window for showing the rat indicator.
	 */
	private static void createRatIndicator() {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis(0, GameMaster.getMaxRats(), 1);

		xAxis.setTickLabelsVisible(false);

		sbcRatIndicator = new StackedBarChart<>(xAxis, yAxis);

		XYChart.Series<String, Number> maleNumber = new XYChart.Series<>();
		maleNumber.getData().add((new XYChart.Data<>("Bob", 0)));

		XYChart.Series<String, Number> femaleNumber = new XYChart.Series<>();
		femaleNumber.getData().add(new XYChart.Data<>("Bob", 0));

		XYChart.Series<String, Number> rest = new XYChart.Series<>();
		rest.getData().add(new XYChart.Data<>("Bob", GameMaster.getMaxRats()));

		sbcRatIndicator.getData().add(maleNumber);
		sbcRatIndicator.getData().add(femaleNumber);
		sbcRatIndicator.getData().add(rest);

		sbcRatIndicator.setMaxWidth(20);
		sbcRatIndicator.setMinHeight(CANVAS_HEIGHT);
		sbcRatIndicator.setLegendVisible(false);
	}

	/**
	 * Sets the values for the rat indicator window.
	 */
	private static void setRatIndicator() {
		double a = RatController.getMaleCounter();
		double b = RatController.getFemaleCounter();
		double c = GameMaster.getMaxRats() - a - b;
		double[] fd = { a, b, c };

		int counter = 0;
		for (XYChart.Series<String, Number> series : sbcRatIndicator.getData()) {
			for (XYChart.Data<String, Number> data : series.getData()) {
				data.setYValue(fd[counter++]);
			}
		}
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

	/**
	 * IMPORTANT This method will run in a cycle indefinitely until stopped,
	 * currently allows rats to move around. TODO Check if time is over TODO Level
	 * from Game Master
	 */
	private static void runCycle() {
		currMovement = new HashMap<>();
		step = 0;
		m.runAllTiles();
		ratMoveTimeline.play();
		currPoints.setText(String.valueOf(RatController.getPoints()));
		drawItems();
		setRatIndicator();

		// Stop conditions
		if (RatController.stopGame()) { // Bad number of rats
			stopGame();
			if (RatController.getFemaleCounter() + RatController.getMaleCounter() == 0) {
				GameMaster.gameEndWin();
			} else {
				GameMaster.gameEndTooManyRats();
			}
		} else if (playerStopGame) { // Player stops the game
			stopGame();
			saveState();
		} // Otherwise keep going
	}

	private static void decrementTimer() {
		maxTime--;
		currTime.setText(String.valueOf(maxTime));
		if (maxTime == 0) { // Game over
			stopGame();
			GameMaster.gameEndTimeEnd();
		}
	}

	/**
	 * Stops the game.
	 */
	private static void stopGame() {
		cycler.stop();
		timeLimit.stop();
		setRatIndicator();
	}
	
	/**
	 * Starts/ continues the game.
	 */
	private static void playGame() {
		cycler.play();
		timeLimit.play();
	}

	/**
	 * Saves the state of the game
	 */
	private static void saveState() {
		m.saveState(Main.PLAYER_FILE_LOC + playerName + ".txt");
	}

	/**
	 * Criteria for Rat movements.
	 */
	private static void setTimeLines() {
		ratMoveTimeline = new Timeline(new KeyFrame(Duration.millis(TIME_BETWEEN_STEPS), event -> goThroughRat()));
		ratMoveTimeline.setCycleCount(NORMAL_RAT_SPEED);
	}

	/**
	 * Goes through each type of rats to draw them onto canvas
	 */
	private static void goThroughRat() {
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
	private static void drawRat(RatType rt) {
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
