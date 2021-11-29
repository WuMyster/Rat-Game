import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * @author Jing Shiang Gu
 *
 */
public class Board {

	/**
	 * The map in string format.
	 */
	private String mapDesign;

	/**
	 * The width of the map.
	 */
	private int xHeight;

	/**
	 * The height of the map
	 */
	private int yHeight;

	/**
	 * The 2d array of the map. Static so other classes can access this
	 */
	private TileType[][] board;

	/**
	 * List of all tiles on board.
	 */
	private static ArrayList<TileType> allTiles;

	// ? Is final in the correct place? Should this be public?
	/**
	 * Constants of Tile letters from string to Grass Tile, current implementation
	 * sets this to be {@code null}.
	 */
	private static final char GRASS_TILE = 'G';

	/**
	 * Constants of Tile letters from string to Path Tile.
	 */
	private final static char PATH_TILE = 'P';

	/**
	 * Constants of Tile letters from string to Junction Tile.
	 */
	private final static char JUNCTION_TILE = 'J';

	/**
	 * Constants of Tile letters from string to Tunnel Tile. TODO
	 */
	private final static char TUNNEL_TILE = 'T';

	/**
	 * Number of tiles in between each visible tile +1 (so 2 means 1 extra tile in between)
	 */
	public final static int EXTRA_PADDING = 2;

	/**
	 * Constructs a {@code Board} from input string.
	 * 
	 * @param mapDesign input string of board
	 * @param xHeight   max width of board
	 * @param yHeight   max height of board
	 */
	public Board(String mapDesign, int xHeight, int yHeight) {
		this.mapDesign = mapDesign;
		this.xHeight = xHeight;
		this.yHeight = yHeight;
		try {
			this.board = new TileType[yHeight * EXTRA_PADDING][xHeight * EXTRA_PADDING];
		} catch (Exception e) {
			e.printStackTrace();
		}
		allTiles = new ArrayList<>();
		createBoard();
		eliminateEmpties();
			createGraph();
	}

	// For debug only
	public TileType[][] getBoard() {
		return board;
	}

	/**
	 * Adds effect of stop sign to tile
	 * @param x x position of tile on map
	 * @param y y position of tile on map
	 * @return {@code true} if stop sign can be placed
	 */
	public boolean addStopSign(int x, int y) {
		TileType t = board[y * EXTRA_PADDING][x * EXTRA_PADDING];
		if (t == null) {
			return false;
		} // else if (board[y][x] instanceof tunnelTile) {

        //TODO Wu move this to item
		t.placeStopSign();

		return true;
	}

	/**
	 * TODO
	 * Adds effect of bomb to tile.
	 * Currently instantly blows up, will implement time thing.
	 * @param x x position of tile on map
	 * @param y y position of tile on map
	 * @return {@code true} if bomb can be placed at that location.
	 */
	public boolean addBomb(int x, int y) {
		y *= EXTRA_PADDING;
		x *= EXTRA_PADDING;
		int startY = y;
		int startX = x;

		TileType t = board[startY][startX];
		while (t != null) {
			t.blowUp();
			t = board[y--][x];
		}

		t = board[startY][startX];
		y = startY;
		x = startX;
		while (t != null) {
			t.blowUp();
			t = board[y++][x];
		}

		t = board[startY][startX];
		y = startY;
		x = startX;
		while (t != null) {
			t.blowUp();
			t = board[y][x--];
		}

		t = board[startY][startX];
		y = startY;
		x = startX;
		while (t != null) {
			t.blowUp();
			t = board[y][x++];
		}
		return true;
	}

	/**
	 * Draws board onto game window.
	 * 
	 * @param gc Canvas to draw the board on
	 */
	public void drawBoard(GraphicsContext gc) {

		Image grassImage = new Image("Grass.png");
		Image tileImage = new Image("Tile.png");
		
		int x = 0;
		int y = 0;
		
		for (int i = 0; i < yHeight * EXTRA_PADDING; i += EXTRA_PADDING) {
			for (int j = 0; j < xHeight * EXTRA_PADDING; j += EXTRA_PADDING) {
				if (board[i][j] == null) {
					gc.drawImage(grassImage, 
							x++ * Main.TILE_SIZE, 
							y * Main.TILE_SIZE, 
							Main.TILE_SIZE,
							Main.TILE_SIZE);
				} else {
					gc.drawImage(tileImage, 
							x++ * Main.TILE_SIZE, 
							y * Main.TILE_SIZE, 
							Main.TILE_SIZE,
							Main.TILE_SIZE);
				}
			}
			x = 0;
			y++;
		}
	}

	/**
	 * Put Rat onto game canvas.
	 * 
	 * @param rats the rat that's going to the next tile
	 * @param dir  direction the rat is facing
	 * @param x    x start position of the rat
	 * @param y    y start position of the rat
	 * 
	 */
	public void placeRat(Rat rats, Direction dir, int x, int y) {
		board[x * EXTRA_PADDING][y * EXTRA_PADDING].addRat(rats, dir);
	}

	/**
	 * Goes through each Tile and moves the rat to the next tile before getting the
	 * tiles new list.
	 */
	public void runAllTiles() {
		// Send item to Rat make sure to have boolean to know if it is dead or not

		// Movement
		for (TileType t : allTiles) {
			t.setCurrRat();
		}
		for (TileType t : allTiles) {
			t.getNextDirection();
		}
	}

	/**
	 * Creates 2d array of the map.
	 */
	private void createBoard() {
		int counter = 0;
		for (int i = 0; i < yHeight * EXTRA_PADDING; i++) {
			for (int j = 0; j < xHeight * EXTRA_PADDING; j++) {
				switch (mapDesign.charAt(counter++)) {
				case GRASS_TILE -> board[i][j] = null;
				case PATH_TILE -> board[i][j] = new PathTile(i, j);
				case JUNCTION_TILE -> board[i][j] = new JunctionTile(i, j);
				// case TUNNEL_TILE -> board[i][j] = new TunnelTile(i, j);
				default -> System.out.println("Map error!");
				}
				board[i][++j] = new LightTile(i, j);
			}
			i++;
			for (int j = 0; j < xHeight * EXTRA_PADDING; j++) {
				board[i][j] = new LightTile(i, j);
			}
		}

	}

	private void eliminateEmpties() {
		for (int i = 0; i < yHeight * EXTRA_PADDING; i++) {
			for (int j = 0; j < xHeight * EXTRA_PADDING; j++) {
				if (board[i][j] instanceof LightTile) {
					int counter = 0;
					if (i != 0) {
						counter += check(board[i - 1][j]) ? 1 : 0;
					}
					if (i != yHeight * EXTRA_PADDING - 1) {
						counter += check(board[i + 1][j]) ? 1 : 0;
					}

					if (j != 0) {
						counter += check(board[i][j - 1]) ? 1 : 0;
					}
					if (j != xHeight * EXTRA_PADDING - 1) {
						counter += check(board[i][j + 1]) ? 1 : 0;
					}

					if (counter < 2) {
						board[i][j] = null;
					}
				}
			}
		}
	}

	private boolean check(TileType t) {
		if (t == null) {
			return false;
		} else if (t instanceof LightTile) {
			return false;
		}
		return true;
	}

	// Possible to implement the gui part too!
	/**
	 * Converts the 2d array of the map into a graph.
	 */
	private void createGraph() {
		for (int i = 0; i < yHeight * EXTRA_PADDING; i++) {
			for (int j = 0; j < xHeight * EXTRA_PADDING; j++) {

				if (board[i][j] != null) {
					allTiles.add(board[i][j]);
					ArrayList<TileType> tiles = new ArrayList<>(4);
					ArrayList<Direction> direction = new ArrayList<>(4);

					// Check North
					if (i != 0) {
						if (board[i - 1][j] != null) {
							tiles.add(board[i - 1][j]);
							direction.add(Direction.NORTH);
						}
					}

					// Check East
					if (j != xHeight * EXTRA_PADDING - 1) {
						if (board[i][j + 1] != null) {
							tiles.add(board[i][j + 1]);
							direction.add(Direction.EAST);
						}
					}

					// Check South
					if (i != yHeight * EXTRA_PADDING - 1) {
						if (board[i + 1][j] != null) {
							tiles.add(board[i + 1][j]);
							direction.add(Direction.SOUTH);
						}
					}

					// Check West
					if (j != 0) {
						if (board[i][j - 1] != null) {
							tiles.add(board[i][j - 1]);
							direction.add(Direction.WEST);
						}
					}
					board[i][j].setNeighbourTiles(tiles.toArray(
							new TileType[2]), direction.toArray(new Direction[2]));
				}
			}
		}

	}

}
