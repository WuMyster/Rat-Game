import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * @author 2010573
 *
 */
public class Map {

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
	private Tile[][] board;

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
	 * Constants of Tile letters from string to Tunnel Tile.
	 */
	private final static char TUNNEL_TILE = 'T';

	// private Image playerImage;
	private static Image grassImage = new Image("Grass.png");
	private static Image tileImage = new Image("Tile.png");
	// private String start = "C:\\Users\\jsgu1\\eclipse-workspace\\CS230CW1\\";

	/**
	 * Constructs a {@code Map} from input string.
	 * 
	 * @param mapDesign input string of map
	 * @param xHeight   max height of map
	 * @param yHeight   max width of map
	 */
	public Map(String mapDesign, int xHeight, int yHeight) {
		this.mapDesign = mapDesign;
		this.xHeight = xHeight;
		this.yHeight = yHeight;
		this.board = new Tile[yHeight][xHeight];
		createBoard();
		createGraph();
	}

	// For debug only
	public Tile[][] getBoard() {
		return board;
	}

	/**
	 * Will draw the map onto gc
	 * @param gc Canvas to draw the map on
	 */
	public void drawMap(GraphicsContext gc) {
		int x = 0;
		int y = 0;
		for (Tile[] a : board) {
			for (Tile b : a) {
				if (b == null) {
					gc.drawImage(grassImage, x++ * Output.TILE_SIZE, 
							y * Output.TILE_SIZE, Output.TILE_SIZE, Output.TILE_SIZE);
				} else {
					gc.drawImage(tileImage, x++ * Output.TILE_SIZE, 
							y * Output.TILE_SIZE, Output.TILE_SIZE, Output.TILE_SIZE);
				}
			}
			x = 0;
			y++;
		}
	}

	/**
	 * Creates 2d array of the map.
	 */
	private void createBoard() {
		int counter = 0;
		for (int i = 0; i < yHeight; i++) {
			for (int j = 0; j < xHeight; j++) {
				switch (mapDesign.charAt(counter++)) {
				case GRASS_TILE -> board[i][j] = null;
				case PATH_TILE -> board[i][j] = new PathTile();
				case JUNCTION_TILE -> board[i][j] = new JunctionTile();
				case TUNNEL_TILE -> board[i][j] = new TunnelTile();
				default -> System.out.println("Map error!");
				}
			}
		}
	}

	// Possible to implement the gui part too!
	/**
	 * Converts the 2d array of the map into a graph.
	 */
	private void createGraph() {
		for (int i = 0; i < yHeight; i++) {
			for (int j = 0; j < xHeight; j++) {

				if (board[i][j] != null) {
					int counter = 0;
					Tile[] tiles = new Tile[4];
					Direction[] direction = new Direction[4];

					// Check North
					if (i != 0) {
						if (board[i - 1][j] != null) {
							tiles[counter] = board[i - 1][j];
							direction[counter++] = Direction.NORTH;
						}
					}

					// Check East
					if (j != xHeight - 1) {
						if (board[i][j + 1] != null) {
							tiles[counter] = board[i][j + 1];
							direction[counter++] = Direction.EAST;
						}
					}

					// Check South
					if (i != yHeight - 1) {
						if (board[i + 1][j] != null) {
							tiles[counter] = board[i + 1][j];
							direction[counter++] = Direction.SOUTH;
						}
					}

					// Check West
					if (j != 0) {
						if (board[i][j - 1] != null) {
							tiles[counter] = board[i][j - 1];
							direction[counter++] = Direction.WEST;
						}
					}
					board[i][j].setNeighbourTiles(tiles, direction);
				}
			}
		}

	}

}
