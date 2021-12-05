import java.sql.Array;
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
     * Image of Bomb
     */
    private static Image BOMB;

    /**
     * Draggable image for bomb.
     */
    ImageView draggableBomb = new ImageView();

    private static Image SEX_TO_FEMALE;
    ImageView draggableSexToFemale = new ImageView();

    private static Image SEX_TO_MALE;
    ImageView draggableSexToMale = new ImageView();

    private static Image STERILISE;
    ImageView draggableSterilise = new ImageView();

    private static Image GAS;
    ImageView draggableGas = new ImageView();

    /**
     * Image of Poison
     */
    private static Image POISON;

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
	private Canvas itemCanvas;

	/**
	 * Level number of current level.
	 */
	private Label currLevel;
	
	/**
	 * Number of points accumulated in level so far.
	 */
	private Label currPoints;

	/**
	 * x y coordinates of all stop signs
	 */
	private static ArrayList<int[]> stopSignPlace;
	
	/**
	 * x y coordinates of all bomb placements
	 */
    private static ArrayList<int[]> bombPlace;

    public static ArrayList<int[]> getBombPlace () {
        return bombPlace;
    }

    /**
     * x y coordinates of all poison placements
     */
    private static ArrayList<int[]> poisonPlace;

    public static ArrayList<int[]> getPoisonPlace() {
        return poisonPlace;
    }

    // TODO Wu I won't need these if I don't let the items stay on a tile
    private static ArrayList<int[]> sexToFemalePlace;
    public static ArrayList<int[]> getSexToFemalePlace() {
        return sexToFemalePlace;
    }
    private static ArrayList<int[]> sexToMalePlace;
    public static ArrayList<int[]> getSexToMalePlace() {
        return sexToMalePlace;
    }
    private static ArrayList<int[]> sterilisePlace;
    public static ArrayList<int[]> getSterilisePlace() {
        return sterilisePlace;
    }
    private static ArrayList<int[]> gasPlace;
    public static ArrayList<int[]> getGasPlace() {
        return gasPlace;
    }
    public static void addGasPlace(int x, int y) {
        gasPlace.add(new int[] {(int) y, (int) x});
    }

	/**
	 * The Rats in the game window which needs to move.
	 */
	private static HashMap<RatType, 
		HashMap<Direction, ArrayList<int[]>>> currMovement;
	
	/**
	 * Iterating over moving the rat.
	 */
	private Timeline ratMoveTimeline;
	
	/**
	 * Number of steps rat has taken during current iteration of rat movement.
	 * 2x for baby rats and death rats.
	 */
	private int step;

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = createGameGUI();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

		primaryStage.setScene(scene);
		primaryStage.show();

		test101();
		
		Timeline cycler = new Timeline(new KeyFrame(Duration.millis(CYCLE_TIME), event -> runCycle()));
		// a.setCycleCount(1);
		// a.setCycleCount(10);x
		cycler.setCycleCount(Animation.INDEFINITE);
		// cycler.play();
	}
	
	private void test100() {
		m.placeRat(new Rat(100, true, false, 10, false, false, false), Direction.EAST, 1, 3);
		m.placeRat(new Rat(100, false, false, 10, false, false, false), Direction.WEST, 1, 2);
	}
	
	private void test101() {
		m.placeRat(new DeathRat(), Direction.EAST, 1, 3);
		m.placeRat(new Rat(100, true, false, 10, false, false, false), Direction.EAST, 1, 3);
		m.placeRat(new Rat(100, false, false, 10, false, false, false), Direction.WEST, 1, 2);
	}
	
	private void test59() {
		m.placeRat(new DeathRat(), Direction.EAST, 1 , 4);
		m.placeRat(new Rat(100, true, false, 10, false, false, false), Direction.EAST, 1, 3);
		m.placeRat(new Rat(100, false, false, 10, false, false, false), Direction.WEST, 1, 3);
	
	}
	
	private void test60() {
		m.placeRat(new DeathRat(), Direction.EAST, 1 , 5);
		m.placeRat(new Rat(100, true, false, 10, false, false, false), Direction.EAST, 1, 3);
		m.placeRat(new Rat(100, false, false, 10, false, false, false), Direction.WEST, 1, 3);
	
	}
	
	private void test61() {
		m.placeRat(new DeathRat(), Direction.EAST, 1 , 5);
		m.placeRat(new Rat(100, true, false, 10, true, false, false), Direction.EAST, 1, 3);
		m.placeRat(new Rat(100, false, false, 10, true, false, false), Direction.WEST, 1, 3);
	
	}
	
	// Good PathTile
	private void test30() {
		m.placeRat(new DeathRat(), Direction.EAST, 1 , 6);
		m.placeRat(new Rat(100, true, false, 10, false, false, false), Direction.EAST, 1, 4);
		m.placeRat(new Rat(100, false, false, 10, false, false, false), Direction.WEST, 1, 2);
	}
	
	// Bad
	private void test31() {
		m.placeRat(new DeathRat(), Direction.EAST, 1 , 7);
		m.placeRat(new Rat(100, true, false, 10, false, false, false), Direction.EAST, 1, 4);
		m.placeRat(new Rat(100, false, false, 10, false, false, false), Direction.WEST, 1, 2);
	}
	
	// Good JunctionTile
	private void test40() {
		m.placeRat(new DeathRat(), Direction.EAST, 1, 11);
		m.placeRat(new Rat(100, true, false, 10, false, false, false), Direction.EAST, 1, 9);
		m.placeRat(new Rat(100, false, false, 10, false, false, false), Direction.WEST, 1, 7);
	}
	
	// Bad
	private void test41() {
		m.placeRat(new DeathRat(), Direction.EAST, 1, 12);
		m.placeRat(new Rat(100, true, false, 10, false, false, false), Direction.EAST, 1, 9);
		m.placeRat(new Rat(100, false, false, 10, false, false, false), Direction.WEST, 1, 7);
	}

	/**
	 * IMPORTANT This method will run in a cycle indefinitely until stopped,
	 * currently allows rats to move around.
	 */
	private void runCycle() {
		currMovement = new HashMap<>();
		step = 0;
		m.runAllTiles();
		// currPoints.setText(String.valueOf(RatController.getPoints()));
		ratMoveTimeline.play();
		currPoints.setText(String.valueOf(RatController.getPoints()));
		
		drawItems();
		
		//Game end?
	}

	/**
	 * Criteria for Rat movements.
	 */
	private void moveRat() {
		ratMoveTimeline = new Timeline(
				new KeyFrame(Duration.millis(TIME_BETWEEN_STEPS), 
						event -> goThroughRat()));
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
	 * @param smallerList type of rat you're dealing with
	 * @param ratImage image of rat
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
							gc.drawImage(ratImage[0],
									i[1] * RAT_POSITION + (TILE_X_OFFSET * size), 
									i[0] * RAT_POSITION + (TILE_SIZE / 4 * (size - 1)),
									width, height);
						}
					} else if (step <= NORMAL_RAT_SPEED / (4 / i[2])) {
						gc.drawImage(ratImage[0],
								i[1] * RAT_POSITION + (TILE_X_OFFSET * size), 
								i[0] * RAT_POSITION - step * speed + (TILE_SIZE / 4 * (size - 1)),
								width, height);
					}
				}
			}
	
			currDirection = smallerList.get(Direction.EAST);
			if (currDirection != null) {
				for (int[] i : currDirection) {
					if (i[2] == 0) {
						if (step <= NORMAL_RAT_SPEED / (4 / i[3])) {
							gc.drawImage(ratImage[1], 
									i[1] * RAT_POSITION, 
									i[0] * RAT_POSITION + TILE_Y_OFFSET + (TILE_SIZE / 4 * (size - 1)),
									height, width);
						}
					} else if (step <= NORMAL_RAT_SPEED / (4.0 / i[2])) {
						gc.drawImage(ratImage[1], 
								i[1] * RAT_POSITION + step * speed, 
								i[0] * RAT_POSITION + TILE_Y_OFFSET + (TILE_SIZE / 4 * (size - 1)),
								height, width);
					}
				}
			}
	
			currDirection = smallerList.get(Direction.SOUTH);
			if (currDirection != null) {
				for (int[] i : currDirection) {
					if (i[2] == 0) {
						if (step <= NORMAL_RAT_SPEED / (4 / i[3])) {
							gc.drawImage(ratImage[2], 
									i[1] * RAT_POSITION + (TILE_X_OFFSET * size), 
									i[0] * RAT_POSITION + (TILE_SIZE / 4 * (size - 1)),
									width, height);
						}
					} else if (step <= NORMAL_RAT_SPEED / (4 / i[2])) {
						gc.drawImage(ratImage[2], 
								i[1] * RAT_POSITION + (TILE_X_OFFSET * size), 
								i[0] * RAT_POSITION + step * speed + (TILE_SIZE / 4 * (size - 1)),
								width, height);
					}
				}
			}
	
			currDirection = smallerList.get(Direction.WEST);
			if (currDirection != null) {
				for (int[] i : currDirection) {
					if (i[2] == 0) {
						if (step <= NORMAL_RAT_SPEED / (4 / i[3])) {
							gc.drawImage(ratImage[3], 
									i[1] * RAT_POSITION, 
									i[0] * RAT_POSITION + TILE_Y_OFFSET + (TILE_SIZE / 4 * (size - 1)),
									height, width);
						}
					} else if (step <= NORMAL_RAT_SPEED / (4 / i[2])) {
						gc.drawImage(ratImage[3], 
								i[1] * RAT_POSITION - step * speed, 
								i[0] * RAT_POSITION + TILE_Y_OFFSET + (TILE_SIZE / 4), // * (size - 1)
								height, width);
					} 
				}
			}
		}
	}

	/**
	 * Adds to list of Rat movements on the game canvas.
	 * 
	 * @param pos xy position of the rat
	 * @param dir direction the rat is facing
	 * @param rt type of rat
	 * @param move movement status of rat
	 */
	public static void addCurrMovement(int[] pos, Direction dir, RatType rt, int move) {
		currMovement.putIfAbsent(rt, new HashMap<Direction, ArrayList<int[]>>());
		currMovement.get(rt).putIfAbsent(dir, new ArrayList<int[]>());
		
		if (move == 0) {		
			currMovement.get(rt).get(dir).add(new int[] {pos[0], pos[1], 0, 4});
		} else {
			currMovement.get(rt).get(dir).add(new int[] {pos[0], pos[1], move});
		}
	}
	
	/**
	 * Adds to list of Rat movements on the game canvas.
	 * 
	 * @param pos xy position of the rat
	 * @param dir direction the rat is facing
	 * @param rt type of rat
	 * @param move movement status of rat
	 */
	public static void addCurrMovement(int[] pos, Direction dir, RatType rt, int move, int steps) {
		currMovement.putIfAbsent(rt, new HashMap<Direction, ArrayList<int[]>>());
		currMovement.get(rt).putIfAbsent(dir, new ArrayList<int[]>());
		currMovement.get(rt).get(dir).add(new int[] {pos[0], pos[1], move, steps});
	}
	/**
	 * Creates game GUI.
	 * 
	 * @return the GUI
	 */
	private BorderPane createGameGUI() {
		
        BOMB = new Image("Bomb.png");
        POISON = new Image("Poison.png");
        SEX_TO_FEMALE = new Image("SexChangeToFemale.png");
        SEX_TO_MALE = new Image("SexChangeToMale.png");
        STERILISE = new Image("img/Sterilise.png");
        GAS = new Image("img/icon-gas.png");
		RAT_WIDTH = 30;
		RAT_HEIGHT = 45;
		stopSignPlace = new ArrayList<>();
        bombPlace = new ArrayList<>();
        poisonPlace = new ArrayList<>();
        sexToFemalePlace = new ArrayList<>();
        sexToMalePlace = new ArrayList<>();
        sterilisePlace = new ArrayList<>();
        gasPlace = new ArrayList<>();
		BorderPane root = null;
		try {
		root = new BorderPane();
		root.setCenter(createCenterMap());
		root.setTop(createTopMenu());
		root.setRight(createRightMenu());
		} catch (NullPointerException n) {
			n.printStackTrace();
		}catch (Exception e ) {
			e.printStackTrace();
		}
		String properMap1;
		int tunnel = 3;
		if (tunnel == 0) {
			properMap1 = "GGGGGGGGGGGGGGGGGGPPPPJPPJPPJPPPPGGPGGGTGGPGGTGGGPGGPGGGTGGPGGTGGGPGGPGGGTGGPGGTGGGPGGJPPJJPPJPPJJPPJGGPGGTGGGPGGGTGGPGGPGGTGGGPGGGTGGPGGPGGTGGGPGGGTGGPGGPPPJPPPJPPPJPPPGGGGGGGGGGGGGGGGGG";
		} else if (tunnel == 1){
			properMap1 = "GGGGGGGGGGGGGGGGGGPPPPPPPJPPPPPPPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGJPPPPPPJPPPPPPJGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPPPPPPPJPPPPPPPGGGGGGGGGGGGGGGGGG";
		} else {// if (tunnel == 2) {
			properMap1 = "GGGGGGGGGGGGGGGGGGPPPPPPPJPPPPPPPGGPGGGGGGPGGGGGGPGGJTTTTTTJTTTTTTJGGPGGGGGGPGGGGGGPGGJPPPPPPJPPPPPPJGGPGGGGGGPGGGGGGPGGJTTTTTTJTTTTTTJGGPGGGGGGPGGGGGGPGGPPPPPPPJPPPPPPPGGGGGGGGGGGGGGGGGG";
		}
		m = new Board(properMap1, 17, 11);
		drawMap();
		moveRat();

		return root;
	}

	/**
	 * Creates the game canvas in window. Will have the Board, Rats and Items.
	 * 
	 * @return Game canvas
	 */
	private Pane createCenterMap() {
		Pane root = new Pane();
		//Creating canvases
		baseCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		mapCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		ratCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		itemCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		
		//Adding canvas to pane
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
	 * Remove stop sign from board.
	 * TODO Hopefully can be upgraded to remove all items.
	 * @param pos position where the stop sign is
	 */
	public static void removeItem(Item item, int[] pos) {
        ArrayList<int[]> arr = null;

        if (item instanceof Poison) {
            arr = poisonPlace;
        }
        if (item instanceof SexChangeToFemale) {
            arr = sexToFemalePlace;
        }
        if (item instanceof SexChangeToMale) {
            arr = sexToMalePlace;
        }
        if (item instanceof Sterilisation) {
            arr = sterilisePlace;
        }

        /*
        ArrayList<int[]> itemPlace = null;
        Item.Name itemName = Item.Name.POISON;

        switch (itemName) {
            case POISON:
                itemPlace = poisonPlace;
                break;
            case BOMB:
                itemPlace = bombPlace;
                break;
            case SEX_CHANGE_TO_FEMALE:
                itemPlace = sexToFemalePlace;
                break;
        }
         */
	}

	/**
	 * Redraws all stop signs onto the map.
	 * TODO Hopefully can be upgraded to draw all items.
	 */
	private void drawItems() {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		for (int[] i : stopSignPlace) {
			gc.drawImage(StopSign.getState(i[2]), i[1] * TILE_SIZE, i[0] * TILE_SIZE);
		}
        for (int[] i : bombPlace) {
            gc.drawImage(BOMB, i[1] * TILE_SIZE, i[0] * TILE_SIZE);
        }
        for (int[] i : poisonPlace) {
            gc.drawImage(POISON, i[1] * TILE_SIZE, i[0] * TILE_SIZE);
        }
        for (int[] i : sexToFemalePlace) {
            gc.drawImage(SEX_TO_FEMALE, i[1] * TILE_SIZE, i[0] * TILE_SIZE);
        }
        for (int[] i : sexToMalePlace) {
            gc.drawImage(SEX_TO_MALE, i[1] * TILE_SIZE, i[0] * TILE_SIZE);
        }
        for (int[] i : sterilisePlace) {
            gc.drawImage(STERILISE, i[1] * TILE_SIZE, i[0] * TILE_SIZE);
        }
        for (int[] i : gasPlace) {
            gc.drawImage(GAS, i[1] * TILE_SIZE, i[0] * TILE_SIZE);
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
	 * TODO Can check list, can check tile, I've had it check Tile - J
	 * Could also use set, add it and check length
	 */
	private void placeStopSign(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);
		
		if (m.addStopSign((int) x, (int) y)) {
			stopSignPlace.add(new int[] { (int) y, (int) x, StopSign.MAX_STATES });
			// Draw an icon at the dropped location.
			GraphicsContext gc = itemCanvas.getGraphicsContext2D();
			gc.drawImage(StopSign.getState(StopSign.MAX_STATES), x * TILE_SIZE, y * TILE_SIZE);
		}
	}

	/**
	 * React when an object is dragged onto the canvas.
	 * 
	 * @param event The drag event itself which contains data about the drag that
	 *              occurred.
	 * @author Liam O'Reilly
	 * @author Jing Shiang Gu
	 * TODO: Check if coordinate is already in list and say no
     * TODO Wu, look into a way to turn these all into one method if time allows
	 */
	private void placeStopSignAndrew(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);

        if (Board.isItemPlaceable((int) x, (int) y)) {
            stopSignPlace.add(new int[] { (int) y, (int) x });
            m.addStopSign((int) x, (int) y); //Will return boolean if sign can be placed
            // Draw an icon at the dropped location.
            GraphicsContext gc = itemCanvas.getGraphicsContext2D();
            // gc.drawImage(STOP_SIGN, x * TILE_SIZE, y * TILE_SIZE);
        }
	}
	
	/**
	 * Update the graphical state of the Stop Sign.
	 * @param pos xy position of the Stop Sign
	 * @param state the state it is in
	 */
	public static void damageStopSign(int[] pos, int state) {
		// Will need to think about this Currently StopSign calls damage
		if (state != 0) {
			int[] xyPos = null;
			for (int[] i : stopSignPlace) {
				if (i[0] == pos[0] &&
						i[1] == pos[1]) {
					xyPos = i;
				}
			}
			xyPos[2] = state;
		}
	}
	
	/**
	 * Remove stop sign from board.
	 * TODO Hopefully can be upgraded to remove all items.
	 * @param pos position where the stop sign is
	 */
	public static void removeStopSign(int[] pos) {
		int[] a = null;
		for (int[] i : stopSignPlace) {
			if (i[0] == pos[0] &&
					i[1] == pos[1]) {
				a = i;
			}
		}
		stopSignPlace.remove(a);
	}

	// the y is first due to the nature of 2d arrays, can't be helped unfortunately...
	// In future, have a method inside m.addBomb() to call a method here to add so people won't know
	// this is 2d array
    private void placeBomb(DragEvent event) {
        double x = Math.floor(event.getX() / TILE_SIZE);
        double y = Math.floor(event.getY() / TILE_SIZE);

        if (Board.isItemPlaceable((int) x, (int) y)) {
            bombPlace.add(new int[] { (int) y, (int) x });
            m.addBomb((int) x, (int) y); //Will return boolean if sign can be placed
            // Draw an icon at the dropped location.
            GraphicsContext gc = itemCanvas.getGraphicsContext2D();
            gc.drawImage(BOMB, x * TILE_SIZE, y * TILE_SIZE);
        }
    }

    private void placePoison(DragEvent event) {
        double x = Math.floor(event.getX() / TILE_SIZE);
        double y = Math.floor(event.getY() / TILE_SIZE);

        if (Board.isItemPlaceable((int) x, (int) y)) {
            poisonPlace.add(new int[] { (int) y, (int) x });
            m.addPoison((int) x, (int) y); //Will return boolean if sign can be placed
            // Draw an icon at the dropped location.
            GraphicsContext gc = itemCanvas.getGraphicsContext2D();
            gc.drawImage(POISON, x * TILE_SIZE, y * TILE_SIZE);
        }
    }

    private void placeSexToFemale(DragEvent event) {
        double x = Math.floor(event.getX() / TILE_SIZE);
        double y = Math.floor(event.getY() / TILE_SIZE);

        if (Board.isItemPlaceable((int) x, (int) y)) {
            sexToFemalePlace.add(new int[] { (int) y, (int) x });
            m.addSexToFemale((int) x, (int) y); //Will return boolean if sign can be placed
            // Draw an icon at the dropped location.
            GraphicsContext gc = itemCanvas.getGraphicsContext2D();
            gc.drawImage(SEX_TO_FEMALE, x * TILE_SIZE, y * TILE_SIZE);
        }
    }

    private void placeSexToMale(DragEvent event) {
        double x = Math.floor(event.getX() / TILE_SIZE);
        double y = Math.floor(event.getY() / TILE_SIZE);

        if (Board.isItemPlaceable((int) x, (int) y)) {
            sexToMalePlace.add(new int[] { (int) y, (int) x });
            m.addSexToMale((int) x, (int) y); //Will return boolean if sign can be placed
            // Draw an icon at the dropped location.
            GraphicsContext gc = itemCanvas.getGraphicsContext2D();
            gc.drawImage(SEX_TO_MALE, x * TILE_SIZE, y * TILE_SIZE);
        }
    }

    private void placeSterilise(DragEvent event) {
        double x = Math.floor(event.getX() / TILE_SIZE);
        double y = Math.floor(event.getY() / TILE_SIZE);

        if (Board.isItemPlaceable((int) x, (int) y)) {
            sterilisePlace.add(new int[] { (int) y, (int) x });
            m.addSterilise((int) x, (int) y); //Will return boolean if sign can be placed
            // Draw an icon at the dropped location.
            GraphicsContext gc = itemCanvas.getGraphicsContext2D();
            gc.drawImage(STERILISE, x * TILE_SIZE, y * TILE_SIZE);
        }
    }

    private void placeGas(DragEvent event) {
        double x = Math.floor(event.getX() / TILE_SIZE);
        double y = Math.floor(event.getY() / TILE_SIZE);

        if (Board.isItemPlaceable((int) x, (int) y)) {
            gasPlace.add(new int[] { (int) y, (int) x });
            m.addGas((int) x, (int) y); //Will return boolean if sign can be placed
            // Draw an icon at the dropped location.
            GraphicsContext gc = itemCanvas.getGraphicsContext2D();
            gc.drawImage(GAS, x * TILE_SIZE, y * TILE_SIZE);
        }
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

		MenuItem add = new MenuItem("Shuffle");
		menuFile.getItems().add(add);

		Menu optionFile = new Menu("Option");

		menuBar.getMenus().addAll(menuFile, optionFile);
		root.getChildren().addAll(menuBar);
		
		// Tick Timeline buttons
		Button startTickTimelineButton = new Button("Move rat");
		// We add both buttons at the same time to the timeline (we could have done this in two steps).
		root.getChildren().addAll(startTickTimelineButton);

		// Setup the behaviour of the buttons.
		startTickTimelineButton.setOnAction(e -> {
			runCycle();
		});

		return root;
	}

	/**
	 * Creates the right menu - will contain level, points
	 * and number of items.
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
		root.getChildren().add(currPoints);

		// Setup a draggable image. //TODO
		draggableStop.setImage(StopSign.getState(StopSign.MAX_STATES));
		root.getChildren().add(draggableStop);

        draggableBomb.setImage(BOMB);
        root.getChildren().add(draggableBomb);

        draggablePoison.setImage(POISON);
        root.getChildren().add(draggablePoison);

        draggableSexToFemale.setImage(SEX_TO_FEMALE);
        root.getChildren().add(draggableSexToFemale);

        draggableSexToMale.setImage(SEX_TO_MALE);
        root.getChildren().add(draggableSexToMale);

        draggableSterilise.setImage(STERILISE);
        root.getChildren().add(draggableSterilise);

        draggableGas.setImage(GAS);
        root.getChildren().add(draggableGas);

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

        /**
         * TODO Wu find a way to clean this up, reduce repetition
         * @author Liam O'Reilly
         */
        draggableBomb.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // Mark the drag as started.
                // We do not use the transfer mode (this can be used to indicate different forms
                // of drags operations, for example, moving files or copying files).
                Dragboard db = draggableBomb.startDragAndDrop(TransferMode.ANY);

                // We have to put some content in the clipboard of the drag event.
                // We do not use this, but we could use it to store extra data if we wished.
                ClipboardContent content = new ClipboardContent();
                content.putString("Hello");
                db.setContent(content);

                // Consume the event. This means we mark it as dealt with.
                event.consume();
            }
        });

        draggablePoison.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // Mark the drag as started.
                // We do not use the transfer mode (this can be used to indicate different forms
                // of drags operations, for example, moving files or copying files).
                Dragboard db = draggablePoison.startDragAndDrop(TransferMode.ANY);

                // We have to put some content in the clipboard of the drag event.
                // We do not use this, but we could use it to store extra data if we wished.
                ClipboardContent content = new ClipboardContent();
                content.putString("Hello");
                db.setContent(content);

                // Consume the event. This means we mark it as dealt with.
                event.consume();
            }
        });

        draggableSexToFemale.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // Mark the drag as started.
                // We do not use the transfer mode (this can be used to indicate different forms
                // of drags operations, for example, moving files or copying files).
                Dragboard db = draggableSexToFemale.startDragAndDrop(TransferMode.ANY);

                // We have to put some content in the clipboard of the drag event.
                // We do not use this, but we could use it to store extra data if we wished.
                ClipboardContent content = new ClipboardContent();
                content.putString("Hello");
                db.setContent(content);

                // Consume the event. This means we mark it as dealt with.
                event.consume();
            }
        });

        draggableSexToMale.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // Mark the drag as started.
                // We do not use the transfer mode (this can be used to indicate different forms
                // of drags operations, for example, moving files or copying files).
                Dragboard db = draggableSexToMale.startDragAndDrop(TransferMode.ANY);

                // We have to put some content in the clipboard of the drag event.
                // We do not use this, but we could use it to store extra data if we wished.
                ClipboardContent content = new ClipboardContent();
                content.putString("Hello");
                db.setContent(content);

                // Consume the event. This means we mark it as dealt with.
                event.consume();
            }
        });

        draggableSterilise.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // Mark the drag as started.
                // We do not use the transfer mode (this can be used to indicate different forms
                // of drags operations, for example, moving files or copying files).
                Dragboard db = draggableSterilise.startDragAndDrop(TransferMode.ANY);

                // We have to put some content in the clipboard of the drag event.
                // We do not use this, but we could use it to store extra data if we wished.
                ClipboardContent content = new ClipboardContent();
                content.putString("Hello");
                db.setContent(content);

                // Consume the event. This means we mark it as dealt with.
                event.consume();
            }
        });

        draggableGas.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // Mark the drag as started.
                // We do not use the transfer mode (this can be used to indicate different forms
                // of drags operations, for example, moving files or copying files).
                Dragboard db = draggableGas.startDragAndDrop(TransferMode.ANY);

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
         * TODO Wu probably change to switch
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
                if (event.getGestureSource() == draggableBomb) {
                    // Mark the drag event as acceptable by the canvas.
                    event.acceptTransferModes(TransferMode.ANY);
                    // Consume the event. This means we mark it as dealt with.
                    event.consume();
                }
                if (event.getGestureSource() == draggablePoison) {
                    // Mark the drag event as acceptable by the canvas.
                    event.acceptTransferModes(TransferMode.ANY);
                    // Consume the event. This means we mark it as dealt with.
                    event.consume();
                }

                if (event.getGestureSource() == draggableSexToFemale) {
                    // Mark the drag event as acceptable by the canvas.
                    event.acceptTransferModes(TransferMode.ANY);
                    // Consume the event. This means we mark it as dealt with.
                    event.consume();
                }
                if (event.getGestureSource() == draggableSexToMale) {
                    // Mark the drag event as acceptable by the canvas.
                    event.acceptTransferModes(TransferMode.ANY);
                    // Consume the event. This means we mark it as dealt with.
                    event.consume();
                }
                if (event.getGestureSource() == draggableSterilise) {
                    // Mark the drag event as acceptable by the canvas.
                    event.acceptTransferModes(TransferMode.ANY);
                    // Consume the event. This means we mark it as dealt with.
                    event.consume();
                }
                if (event.getGestureSource() == draggableGas) {
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
                itemCanvasDragDropOccurred(event);

				// Consume the event. This means we mark it as dealt with.
				event.consume();
			}
		});
		return root;
	}

    /**
     * TODO Again probably change to switch
     * Reacts to item that is dragged onto canvas.
     * @param event The drag event itself which contains data about the drag that occured.
     */
    public void itemCanvasDragDropOccurred(DragEvent event) {
        if (event.getGestureSource() == draggableStop) {
            placeStopSign(event);
        }
        if (event.getGestureSource() == draggableBomb) {
            placeBomb(event);
        }
        if (event.getGestureSource() == draggablePoison) {
            placePoison(event);
        }
        if (event.getGestureSource() == draggableSexToFemale) {
            placeSexToFemale(event);
        }
        if (event.getGestureSource() == draggableSexToMale) {
            placeSexToMale(event);
        }
        if (event.getGestureSource() == draggableSterilise) {
            placeSterilise(event);
        }
        if (event.getGestureSource() == draggableGas) {
            placeGas(event);
        }
    }

	/**
	 * Draws map onto screen
	 */
	private void drawMap() {
		// Get the Graphic Context of the canvas. This is what we draw on.
		GraphicsContext gc = mapCanvas.getGraphicsContext2D();

		// Clear canvas
		gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

		m.drawBoard(gc);
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
