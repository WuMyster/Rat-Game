import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author Jing Shiang Gu
 *
 */
public class Board {

	/**
	 * Number of tiles in between each visible tile +1 (so 2 means 1 extra tile in between)
	 */
	public final static int EXTRA_PADDING = 2;
	
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
	private static Tile[][] board;

	/**
	 * List of all tiles on board.
	 */
	private static ArrayList<Tile> allTiles;

	/**
	 * Constants of Tile letters from string to Grass Tile, current implementation
	 * sets this to be {@code null}.
	 */
	private final static char GRASS_TILE = 'G';

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
			Board.board = new Tile[yHeight * EXTRA_PADDING][xHeight * EXTRA_PADDING];
		} catch (Exception e) {
			e.printStackTrace();
		}
		allTiles = new ArrayList<>();
		createBoard();
		eliminateEmpties();
		createGraph();
	}

    /**
     * Checks if item can be placed on tile. Can be placed if tile is an instance of path but not
     * a tunnel. Also if tile is a junction.
     * @param x x-coordinate being checked.
     * @param y y-coordinate being checked.
     * @return boolean of if item can be placed on tile.
     */
    private static boolean isPlaceableTile(Tile t) {
        if (t == null) {
			return false;
		}  else if (t instanceof TunnelTile) {
			return false;
		}
        return true;
    }

	/**
	 * Draws board onto game window.
	 * 
	 * @param gc Canvas to draw the board on
	 */
	public void drawBoard(GraphicsContext gc) {
		
		Image grassImage = new Image(Main.IMAGE_FILE_LOC + "Grass.png");
		Image[] tunnelImagesEntrance = new Image[4];
		for(int i = 0; i < 4; i++) {
			tunnelImagesEntrance[i] = new Image(Main.IMAGE_FILE_LOC + "Tunnel" + i + ".png");
		}
		Image[] tunnelImages = new Image[2];
		for(int i = 0; i < 2; i++) {
			tunnelImages[i] = new Image(Main.IMAGE_FILE_LOC + "TunnelF" + i + ".png");
		}
		
		int x = 0;
		int y = 0;
		
		for (int i = 0; i < yHeight * EXTRA_PADDING; i += EXTRA_PADDING) {
			for (int j = 0; j < xHeight * EXTRA_PADDING; j += EXTRA_PADDING) {
				if (board[i][j] == null) {
					gc.drawImage(grassImage, 
							x++ * GameGUI.TILE_SIZE, 
							y * GameGUI.TILE_SIZE, 
							GameGUI.TILE_SIZE,
							GameGUI.TILE_SIZE);
				} else if (board[i][j] instanceof TunnelTile) {
					Image t = new Image(Main.IMAGE_FILE_LOC + "tile.png");
									
					
					// Check for NESW entrance
					if (board[i - EXTRA_PADDING][j] != null) {
						if (!(board[i - EXTRA_PADDING][j] instanceof TunnelTile)) {
							t = tunnelImagesEntrance[0];
						} else {
							t = tunnelImages[0];
						}
					} 
					if (board[i][j + EXTRA_PADDING] != null) {
						if (!(board[i][j + EXTRA_PADDING] instanceof TunnelTile)) {
							t = tunnelImagesEntrance[1];
						} else {
							t = tunnelImages[1];
						}
					}
					if (board[i + EXTRA_PADDING][j] != null) {
						if (!(board[i + EXTRA_PADDING][j] instanceof TunnelTile)) {
							t = tunnelImagesEntrance[2];
						} 
					} 
					if (board[i][j - EXTRA_PADDING] != null) {
						if (!(board[i][j - EXTRA_PADDING] instanceof TunnelTile)) {
							t = tunnelImagesEntrance[3];
						}
					} 
					gc.drawImage(t, 
							x++ * GameGUI.TILE_SIZE, 
							y * GameGUI.TILE_SIZE, 
							GameGUI.TILE_SIZE,
							GameGUI.TILE_SIZE);
				} else {
					x++;
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
		for (int i = 0; i < yHeight * EXTRA_PADDING; i++) {
			for (int j = 0; j < xHeight * EXTRA_PADDING; j++) {
				switch (mapDesign.charAt(counter++)) {
				case GRASS_TILE -> board[i][j] = null;
				case PATH_TILE -> board[i][j] = new PathTile(i, j);
				case JUNCTION_TILE -> board[i][j] = new JunctionTile(i, j);
				case TUNNEL_TILE -> board[i][j] = new TunnelTile(i, j);
				default -> { 
					System.out.println("Map error!"); 
					System.exit(0);}
				}
				board[i][++j] = new LightTile(i, j);
			}
			i++;
			for (int j = 0; j < xHeight * EXTRA_PADDING; j++) {
				board[i][j] = new LightTile(i, j);
			}
		}

	}

	/**
	 * Eliminate LightTiles that aren't connected to enough good tiles.
	 */
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
	
	/**
	 * Checks if item can be added to this tile.
	 * 
	 * @param t tile to check
	 * @return {@code true} if item can be placed on this tile
	 */
	private boolean check(Tile t) {
		if (t == null) {
			return false;
		} else if (t instanceof LightTile) {
			return false;
		}
		return true;
	}
	
	/**
	 * Converts the map into a graph.
	 */
	private void createGraph() {
		for (int i = 0; i < yHeight * EXTRA_PADDING; i++) {
			for (int j = 0; j < xHeight * EXTRA_PADDING; j++) {

				if (board[i][j] != null) {
					ArrayList<Tile> tiles = new ArrayList<>(4);
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
					
					allTiles.add(board[i][j]);
					board[i][j].setNeighbourTiles(tiles.toArray(
							new Tile[2]), direction.toArray(new Direction[2]));
				}
			}
		}
	}

	public boolean addItemToTile(String it, int x, int y) {
		Tile t = board[y * EXTRA_PADDING][x * EXTRA_PADDING];
		if (isPlaceableTile(t)) {
			Item i;
			if (it.equals(StopSign.NAME)) {
				i = new StopSign(new int[] {x, y});
			} else {
				i = new Sterilisation();
			}
			
			return t.setTileItem(i);
		}
		return false;
	}
}
