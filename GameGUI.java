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
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

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
	 * Width of the game canvas in pixels.
	 */
	private static final int CANVAS_WIDTH = 850;

	/**
	 * Height of the game canvas in pixels.
	 */
	private static final int CANVAS_HEIGHT = 700;
	
	/**
	 * Width of the window in pixels.
	 */
	private static final int WINDOW_WIDTH = CANVAS_WIDTH + 300;

	/**
	 * Height of the window in pixels.
	 */
	private static final int WINDOW_HEIGHT = CANVAS_HEIGHT + 70;

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
	private final static ImageView draggableStop = new ImageView(StopSign.IMAGE);

	/**
	 * Draggable image for bomb.
	 */
	private final static ImageView draggableBomb = new ImageView(Bomb.IMAGE);

	/**
	 * Draggable image for sex change (to Female) item.
	 */
	private final static ImageView draggableSexToFemale = new ImageView(SexChangeToFemale.IMAGE);

	/**
	 * Draggable image for sex change (to Male) item.
	 */
	private final static ImageView draggableSexToMale = new ImageView(SexChangeToMale.IMAGE);

	/**
	 * Draggable image for sterilise item.
	 */
	private final static ImageView draggableSterilise = new ImageView(Sterilisation.IMAGE);

	/**
	 * Draggable image for death rat.
	 */
	private final static ImageView draggableDeathRat = new ImageView(DeathRat.IMAGE);

	/**
	 * Draggable image for poison.
	 */
	private final static ImageView draggablePoison = new ImageView(Poison.IMAGE);

	/**
	 * Draggable image for poison.
	 */
	private final static ImageView draggableGas = new ImageView(Gas.IMAGE);
	
	/**
	 * Draggable image for Super Death Rat.
	 */
	private final static ImageView draggableSuperDeath = new ImageView(SuperDeathRat.IMAGE);

	/**
	 * List of all draggable images. Ensure order of draggables is the same as ItemType.
	 * DeathRat treated like normal item, but ain't in ItemType so be added at the end
	 */
	private final static ImageView[] itemIconLis = new ImageView[] {draggablePoison, draggableStop, draggableSterilise,
			draggableBomb, draggableSexToMale, draggableSexToFemale, draggableGas, draggableDeathRat, draggableSuperDeath};
	
	/**
	 * List of labels of number of how many items left.
	 */
	private static Label[] itemCounter = new Label[itemIconLis.length];

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
	 * True if game is stopped.
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
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
	            if (ke.getCode() == KeyCode.ESCAPE) {
	                if (playerStopGame) {
	                	playGame();
	                } else {
	                	stopGame();
	                }
	                
	            }
			}
	    });
		
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
	 * @param it 	the item to be removed
	 * @param pos  	x y coordinates of where item is located on board.
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
			placeItemOnMap(ItemType.STOPSIGN, event);
		} else if (event.getGestureSource() == draggableBomb) {
			placeItemOnMap(ItemType.BOMB, event);
		} else if (event.getGestureSource() == draggablePoison) {
			placeItemOnMap(ItemType.POISON, event);
		} else if (event.getGestureSource() == draggableSexToFemale) {
			placeItemOnMap(ItemType.SEX_TO_FEMALE, event);
		} else if (event.getGestureSource() == draggableSexToMale) {
			placeItemOnMap(ItemType.SEX_TO_MALE, event);
		} else if (event.getGestureSource() == draggableSterilise) {
			placeItemOnMap(ItemType.STERILISATION, event);
		} else if (event.getGestureSource() == draggableDeathRat) {
			placeDeathRat(event);
			Inventory.removeDeathRatCounter(false);
		} else if (event.getGestureSource() == draggableSuperDeath) {
			placeSuperDeath(event);
			Inventory.removeDeathRatCounter(true);
		} else if (event.getGestureSource() == draggableGas) {
			placeItemOnMap(ItemType.GAS, event);
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
	private static void placeItemOnMap(ItemType it, DragEvent event) {
		int x = (int) Math.floor(event.getX() / TILE_SIZE);
		int y = (int) Math.floor(event.getY() / TILE_SIZE);

		if (Board.addItemToTile(it, x, y)) {
			int state;
			switch (it) {
			case STOPSIGN -> state = StopSign.MAX_STATES;
			case BOMB -> state = Bomb.START_COUNTDOWN;
			default -> state = -1;
			}
			Inventory.removeItem(it);
			addItemToMap(it, x, y, state);
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
	public static void addItemToMap(ItemType it, int x, int y, int state) {
		itemPlace.putIfAbsent(it, new ArrayList<>());
		itemPlace.get(it).add(new int[] { y, x, state });
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
	public static void drawItemToMap(ItemType it, int x, int y, int state) {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.drawImage(it.getImage(state), x * TILE_SIZE, y * TILE_SIZE);
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
	 * Update the graphical state of the StopSign.
	 * 
	 * @param pos   xy position of the StopSign
	 * @param state the state it is in
	 */
	public static void damageStopSign(int[] pos, int state) {
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
		drawItemToMap(ItemType.BOMB, x, y, state);
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
		Board.placeRat(new DeathRat(), Direction.NORTH, y, x);
		drawDeathRat(x, y, RatType.DEATH);
	}

	/**
	 * Draws a Death Rat onto screen. Initial drawing of the Death rat.
	 * 
	 * @param x 	x position of the DeathRat
	 * @param y 	y position of the DeathRat
	 * @param rt 	rat type (for death rat between death rat and super death rat)
	 */
	public static void drawDeathRat(int x, int y, RatType rt) {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.drawImage(rt.getImage()[2], x * TILE_SIZE + TILE_X_OFFSET, y * TILE_SIZE, 
				RAT_WIDTH, RAT_HEIGHT);
	}
	
	/**
	 * Place a SuperDeathRat on the event.
	 * 
	 * @param event	drag event
	 */
	private static void placeSuperDeath(DragEvent event) {
		int x = (int) Math.floor(event.getX() / TILE_SIZE);
		int y = (int) Math.floor(event.getY() / TILE_SIZE);
		Board.placeRat(new SuperDeathRat(), Direction.NORTH, y, x);
		drawDeathRat(x, y, RatType.SUPERDEATH);
	}
	
	/**
	 * Sets the number of items available for the item and changes image accordingly.
	 * 
	 * @param itemNum	item number to change the number available
	 * @param num		available number of item
	 */
	public static void setItemCounter(int itemNum, int num) {
		itemCounter[itemNum].setText(String.valueOf(num));
		itemIconLis[itemNum].setDisable(num == 0);

		ColorAdjust desaturate = new ColorAdjust();
		desaturate.setSaturation(num == 0 ? -0.5 : 0);
		itemIconLis[itemNum].setEffect(desaturate);
	}
	
	/**
	 * Time remaining to finish the game.
	 * @return time in seconds to finish game.
	 */
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
				// Mark the drag as acceptable if the source was the draggable image.
				// (for example, we don't want to allow the user to drag things or files into
				// our application)
				for (ImageView i : itemIconLis) {
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
		
		for (int i = 0; i < itemIconLis.length; i++) {
			StackPane sp = new StackPane();
			sp.getChildren().add(itemIconLis[i]);
			itemCounter[i] = new Label("0");
			sp.getChildren().add(itemCounter[i]);
			sp.setAlignment(Pos.BASELINE_RIGHT);
			itemIconLis[i].setDisable(true);
			
			root.getChildren().add(sp);
		}
	}

	private static void setUpHandling() {
		ClipboardContent content = new ClipboardContent();
		content.putString("Hello");

		for (ImageView iv : itemIconLis) {
			iv.setOnDragDetected(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					Dragboard db = iv.startDragAndDrop(TransferMode.ANY);
					db.setContent(content);
					event.consume();
				}
			});
		}
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
		Inventory.setInventory(GameMaster.getInventory());

		BorderPane root = new BorderPane();
		root.setCenter(createCenterMap());
		root.setTop(createTopMenu());
		root.setRight(createRightMenu());
		root.setBottom(createBottomDisplay());
		root.setLeft(createLeftDisplay());

		int[] xy = GameMaster.getMapSize();
		Board.setUpBoard(GameMaster.getMap(), xy[0], xy[1]);
		Board.setUpRats(GameMaster.getRats());
		Board.setUpItems(GameMaster.getItems());
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
		Board.drawBoard(gc);
	}

	/**
	 * IMPORTANT This method will run in a cycle indefinitely until stopped,
	 * currently allows rats to move around. TODO Check if time is over TODO Level
	 * from Game Master
	 */
	private static void runCycle() {
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
		} else {// Otherwise keep going
			// This way so all rats are visibly killed before changing screen
			currMovement = new HashMap<>();
			step = 0;
			Board.runAllTiles();
			ratMoveTimeline.play();
			currPoints.setText(String.valueOf(RatController.getPoints()));
			drawItems();
			setRatIndicator();
		}
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
		Inventory.stopInv();
	}
	
	/**
	 * Starts/ continues the game.
	 */
	private static void playGame() {
		cycler.play();
		timeLimit.play();
		Inventory.startInv();
	}

	/**
	 * Saves the state of the game
	 */
	private static void saveState() {
		String saveLoc = Main.PLAYER_FILE_LOC + playerName + ".txt";
		Board.saveState(saveLoc);
		Inventory.writeInventoryToFile(saveLoc);
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
