import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.canvas.GraphicsContext;

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
	 * Constants of Tile letters from string to Tunnel Tile. XX
	 */
	private final static char TUNNEL_TILE = 'T';
	
	private final static int EXTRA_PADDING = 2;

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
		createGraph();
	}

	// For debug only
	public TileType[][] getBoard() {
		return board;
	}
	
	public boolean addStopSign(int x, int y) {
		TileType t = board[y * EXTRA_PADDING][x * EXTRA_PADDING];
		if (t == null) {
			return false;
		} //else if (board[y][x] instanceof tunnelTile) {
		
		t.placeStopSign();
		
		return true;
	}
	
	public boolean addBomb(int x, int y) {
		int startY = y * EXTRA_PADDING;
		int startX = x * EXTRA_PADDING;
		
		TileType t = board[startY][startX];
		while (t != null) {
			t.blowUp();
			t = board[y--][x];
		}
		
		t = board[startY][startX];
		while (t != null) {
			t.blowUp();
			t = board[y++][x];
		}
		
		t = board[startY][startX];
		while (t != null) {
			t.blowUp();
			t = board[y][x--];
		}
		
		t = board[startY][startX];
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
		int x = 0;
		int y = 0;
		
		for(int i = 0; i < yHeight * EXTRA_PADDING; i += EXTRA_PADDING) {
			for (int j = 0; j < xHeight * EXTRA_PADDING; j += EXTRA_PADDING) {
				if (board[i][j] == null) {
					gc.drawImage(Output.GRASS_IMAGE, x++ * Output.TILE_SIZE, y * Output.TILE_SIZE);
				} else {
					gc.drawImage(Output.TILE_IMAGE, x++ * Output.TILE_SIZE, y * Output.TILE_SIZE);
				}
			}
			x = 0;
			y++;
		}
	}

	//Debug
//	public void placeRat() {
//		board[1][1].addRat(new Rat(), Direction.NORTH);
//		// placeRatAA(new ArrayList<>(Arrays.asList(new Rat())), new
//		// ArrayList<>(Arrays.asList(Direction.NORTH)), new
//		// ArrayList<>(Arrays.asList(new int[] {1,2})));
//	}

	/**
	 * Put Rat onto game canvas.
	 * @param rats 	the rat that's going to the next tile
	 * @param dir	direction the rat is facing
	 * @param x		x start position of the rat
	 * @param y		y start position of the rat
	 * 
	 */
	public void placeRat(Rat rats, Direction dir, int x, int y) {
		board[x][y].addRat(rats, dir);
	}

	/**
	 * @param rats list of rats to be added in
	 * @param dir  direction the rat came from
	 * @param pos  list of position (x, y) from the top left (Might change this)
	 */
	public void placeRatAA(ArrayList<Rat> rats, ArrayList<Direction> dir, ArrayList<int[]> pos) {
		for (int i = 0; i < rats.size(); i++) {
			//int[] currPos = pos.get(i);
			//board[currPos[0]][currPos[1]].addRat(rats.get(i), dir.get(i));
		}
	}

	/**
	 * Creates 2d array of the map.
	 */
	private void createBoard() {
		int counter = 0;
		for (int i = 0; i < yHeight * EXTRA_PADDING; i++) {
			for (int j = 0; j < xHeight * EXTRA_PADDING; j += EXTRA_PADDING) {
				switch (mapDesign.charAt(counter++)) {
				case GRASS_TILE -> board[i][j] = null;
				case PATH_TILE -> board[i][j] = new PathTile(i, j);
				case JUNCTION_TILE -> board[i][j] = new JunctionTile(i, j);
				//case TUNNEL_TILE -> board[i][j] = new TunnelTile(i, j);
				default -> System.out.println("Map error!");
				}
				board[i][j + 1] = new LightTile(i, j + 1);
			}
			i++;
			for (int k = 0; k < xHeight * EXTRA_PADDING; k++) {
				board[i][k] = new LightTile(i, k);
			}
		}
	}

	/**
	 * Goes through each Tile and moves the rat to the next tile before getting the tiles new list.
	 */
	public void runAllTiles() {
		//Send item to Rat make sure to have boolean to know if it is dead or not
		
		
		
		//Movement
		for (TileType t : allTiles) {
			t.setCurrRat();
		}
		for (TileType t : allTiles) {
			t.getNextDirection();
		}
	}

	// Possible to implement the gui part too!
	/**
	 * Converts the 2d array of the map into a graph.
	 */
	private void createGraph() {
		for (int i = 0; i < yHeight; i += EXTRA_PADDING) {
			for (int j = 0; j < xHeight; j += EXTRA_PADDING) {

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
					if (j != xHeight - 1) {
						if (board[i][j + 1] != null) {
							tiles.add(board[i][j + 1]);
							direction.add(Direction.EAST);
						}
					}

					// Check South
					if (i != yHeight - 1) {
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
					board[i][j].setNeighbourTiles(tiles.toArray(new TileType[2]),
							direction.toArray(new Direction[2]));
				}
			}
		}

	}

}
