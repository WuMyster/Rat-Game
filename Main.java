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
	 * Speed of adult rat. Baby rats are 2x.
	 */
	public static final int NORMAL_RAT_SPEED = 25;

	/**
	 * Offset needed to center the Rat along the x axis in pixels.
	 */
	public static final int TILE_X_OFFSET = 10;
	
	/**
	 * Image of Stop sign.
	 */
	private static Image STOP_SIGN;

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
	 * Canvas of map.
	 */
	private Canvas mapCanvas;
	
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
	 * Number of points accumlated in level so far.
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

	/**
	 * The Rats in the game window which needs to move.
	 */
	private static HashMap<RatType, HashMap<Direction, ArrayList<int[]>>> currMovement;
	
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

		m.placeRat(new Rat(true, 20), Direction.SOUTH, 1, 1); //Baby rat
		m.placeRat(new Rat(50, true, true, 20, true, true, true), Direction.SOUTH, 2, 1); //Death Rat
		m.placeRat(new Rat(50, false, true, 20, true, true, false), Direction.WEST, 5, 5); //Female rat
		m.placeRat(new Rat(50, true, false, 20, true, true, false), Direction.SOUTH, 9, 15); //Male rat
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
	private void runCycle() {
		currMovement = new HashMap<>();
		step = 0;
		m.runAllTiles();

		ratMoveTimeline.play();
		
		//Set points
		
		drawItems();
		
		//Game end?
	}

	/**
	 * Criteria for Rat movements.
	 */
	private void moveRat() {
		ratMoveTimeline = new Timeline(new KeyFrame(Duration.millis(10), event -> goThroughRat()));
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
			
			Image ratImage = rt.getImage();
			int size = rt.getSize();
			int width = RAT_WIDTH / size;
			int height = RAT_HEIGHT / size;
			int speed = rt.getSpeed();
	
			// List of rat positions and direction
			ArrayList<int[]> currDirection;
			currDirection = smallerList.get(Direction.NORTH);
			if (currDirection != null) {
				for (int[] i : currDirection) {
					gc.drawImage(ratImage,
							i[1] * RAT_POSITION + (TILE_X_OFFSET * size), 
							i[0] * RAT_POSITION - step * speed + (TILE_SIZE / 4 * (size - 1)),
							width, height);
				}
			}
	
			currDirection = smallerList.get(Direction.EAST);
			if (currDirection != null) {
				for (int[] i : currDirection) {
					gc.drawImage(ratImage, i[1] * RAT_POSITION + 
							(TILE_X_OFFSET * size) + step * speed, 
							i[0] * RAT_POSITION + (TILE_SIZE / 4 * (size - 1)),
							width, height);
				}
			}
	
			currDirection = smallerList.get(Direction.SOUTH);
			if (currDirection != null) {
				for (int[] i : currDirection) {
					gc.drawImage(ratImage, 
							i[1] * RAT_POSITION + (TILE_X_OFFSET * size), 
							i[0] * RAT_POSITION + step * speed + (TILE_SIZE / 4 * (size - 1)),
							width, height);
				}
			}
	
			currDirection = smallerList.get(Direction.WEST);
			if (currDirection != null) {
				for (int[] i : currDirection) {
					gc.drawImage(ratImage, 
							i[1] * RAT_POSITION + (TILE_X_OFFSET * size) - step * speed, 
							i[0] * RAT_POSITION + (TILE_SIZE / 4 * (size - 1)),
							width, height);
				}
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
	public static void addCurrMovement(int[] pos, Direction dir, RatType rt) {
		currMovement.putIfAbsent(rt, new HashMap<Direction, ArrayList<int[]>>());
		currMovement.get(rt).putIfAbsent(dir, new ArrayList<int[]>());
		currMovement.get(rt).get(dir).add(pos);
	}

	/**
	 * Creates game GUI.
	 * 
	 * @return the GUI
	 */
	private BorderPane createGameGUI() {
		
		STOP_SIGN = new Image("Stop_Sign.png");
        BOMB = new Image("Bomb.png");
		RAT_WIDTH = 30;
		RAT_HEIGHT = 45;
		stopSignPlace = new ArrayList<>();
        bombPlace = new ArrayList<>();
		
		BorderPane root = new BorderPane();
		root.setCenter(createCenterMap());
		root.setTop(createTopMenu());
		root.setRight(createRightMenu());
		
		String properMap1 = "GGGGGGGGGGGGGGGGGGPPPPPPPJPPPPPPPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGJPPPPPPJPPPPPPJGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPGGGGGGPGGGGGGPGGPPPPPPPJPPPPPPPGGGGGGGGGGGGGGGGGG";
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
	private void drawItems() {
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		for (int[] i : stopSignPlace) {
			gc.drawImage(STOP_SIGN, i[1] * TILE_SIZE, i[0] * TILE_SIZE);
		}
        for (int[] i : bombPlace) {
            gc.drawImage(BOMB, i[1] * TILE_SIZE, i[0] * TILE_SIZE);
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
	 */
	private void placeStopSign(DragEvent event) {
		double x = Math.floor(event.getX() / TILE_SIZE);
		double y = Math.floor(event.getY() / TILE_SIZE);

		stopSignPlace.add(new int[] { (int) y, (int) x });
		m.addStopSign((int) x, (int) y); //Will return boolean if sign can be placed

		// Draw an icon at the dropped location.
		GraphicsContext gc = itemCanvas.getGraphicsContext2D();
		gc.drawImage(STOP_SIGN, x * TILE_SIZE, y * TILE_SIZE);
	}

    private void placeBomb(DragEvent event) {
        double x = Math.floor(event.getX() / TILE_SIZE);
        double y = Math.floor(event.getY() / TILE_SIZE);

        bombPlace.add(new int[] { (int) y, (int) x });
        m.addBomb((int) x, (int) y); //Will return boolean if bomb can be placed

        // Draw an icon at the dropped location.
        GraphicsContext gc = itemCanvas.getGraphicsContext2D();
        gc.drawImage(BOMB, x * TILE_SIZE, y * TILE_SIZE);
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
		Button startTickTimelineButton = new Button("Blow up");
		// We add both buttons at the same time to the timeline (we could have done this in two steps).
		root.getChildren().addAll(startTickTimelineButton);

		// Setup the behaviour of the buttons.
		startTickTimelineButton.setOnAction(e -> {
			m.addBomb(8, 5);
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

		// Setup a draggable image.
		draggableStop.setImage(STOP_SIGN);
		root.getChildren().add(draggableStop);

        draggableBomb.setImage(BOMB);
        root.getChildren().add(draggableBomb);

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
                if (event.getGestureSource() == draggableBomb) {
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
