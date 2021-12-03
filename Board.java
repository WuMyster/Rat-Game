import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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
	private static Tile[][] board;

	/**
	 * List of all tiles on board.
	 */
	private static ArrayList<Tile> allTiles;
	
	/**
	 * List of death rats.
	 */
	private static ArrayList<DeathRat> deathRatBuffer;

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
	private final static int EXTRA_PADDING = 2;

    public static int getExtraPadding() {
        return EXTRA_PADDING;
    }

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
			this.board = new Tile[yHeight * EXTRA_PADDING][xHeight * EXTRA_PADDING];
		} catch (Exception e) {
			e.printStackTrace();
		}
		allTiles = new ArrayList<>();
		createBoard();
		eliminateEmpties();
			createGraph();
	}

	/**
	 * Debug only!!
	 * @return
	 * @deprecated
	 */
	public static Tile[][] getBoard() {
		return board;
	}

    public static boolean isItemPlaceable(int x, int y) {
        TileType t = board[y * EXTRA_PADDING][x * EXTRA_PADDING];
        if (t instanceof PathTile && !(t instanceof TunnelTile)) {
            return true;
        }
        if (t instanceof JunctionTile) {
            return true;
        }
        return false;
    }

	/**
	 * Adds effect of stop sign to tile
	 * @param x x position of tile on map
	 * @param y y position of tile on map
	 * @return {@code true} if stop sign can be placed
	 */
	public boolean addStopSign(int x, int y) {
		Tile t = board[y * EXTRA_PADDING][x * EXTRA_PADDING];
		if (t == null) {
			return false;
		}  else if (t instanceof TunnelTile) {
			return false;
		}

        //TODO Wu move this to item
		t.placeStopSign();

		return true;
	}

	/**
	 * TODO
	 * Adds effect of bomb to tile.
	 * @param x x position of tile on map
	 * @param y y position of tile on map
	 * @return {@code true} if bomb can be placed at that location.
	 */
	public void addBomb(int x, int y) {
        Tile t = board[y * EXTRA_PADDING][x * EXTRA_PADDING];
        Bomb bomb = new Bomb();

        t.setTileItem(bomb, x, y);
	}

    public void addPoison(int x, int y) {
        Tile t = board[y * EXTRA_PADDING][x * EXTRA_PADDING];
        Poison p = new Poison();

        t.setTileItem(p, x, y);
        }

    public void addSexToFemale(int x, int y) {
        Tile t = board[y * EXTRA_PADDING][x * EXTRA_PADDING];
        SexChangeToFemale toFemale = new SexChangeToFemale();

        t.setTileItem(toFemale, x, y);
    }

    public void addSexToMale(int x, int y) {
        Tile t = board[y * EXTRA_PADDING][x * EXTRA_PADDING];
        SexChangeToMale toMale = new SexChangeToMale();

        t.setTileItem(toMale, x, y);
    }

    public void addSterilise(int x, int y) {
        Tile t = board[y * EXTRA_PADDING][x * EXTRA_PADDING];
        Sterilisation s = new Sterilisation();

        t.setTileItem(s, x, y);
    }

    public void addGas(int x, int y) {
        TileType t = board[y * EXTRA_PADDING][x * EXTRA_PADDING];
        Gas gas = new Gas();

        t.setTileItem(gas, x, y);

    }

	/**
	 * Draws board onto game window.
	 * 
	 * @param gc Canvas to draw the board on
	 */
	public void drawBoard(GraphicsContext gc) {
		
		Image grassImage = new Image("Grass.png");
		//Image tileImage = new Image("Tile.png");
		Image[] tunnelImages = new Image[4];
		for(int i = 0; i < 4; i++) {
			tunnelImages[i] = new Image("Tunnel" + i + ".png");
		}
		
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
				} else if (board[i][j] instanceof TunnelTile) {
					Image t = grassImage;
					
					// Check for NESW entrance
					if (board[i - EXTRA_PADDING][j] != null &&
							!(board[i - EXTRA_PADDING][j] instanceof TunnelTile)) {
						t = tunnelImages[0];
					} else if (board[i][j + EXTRA_PADDING] != null &&
							!(board[i][j + EXTRA_PADDING] instanceof TunnelTile)) {
						t = tunnelImages[1];
					} else if (board[i + EXTRA_PADDING][j] != null &&
							!(board[i + EXTRA_PADDING][j] instanceof TunnelTile)) {
						t = tunnelImages[2];
					} else if (board[i][j - EXTRA_PADDING] != null &&
							!(board[i][j - EXTRA_PADDING] instanceof TunnelTile)) {
						t = tunnelImages[3];
					} 
					gc.drawImage(t, 
							x++ * Main.TILE_SIZE, 
							y * Main.TILE_SIZE, 
							Main.TILE_SIZE,
							Main.TILE_SIZE);
				} else {
					x++;
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
	 * @param dir  direction the rat is came from
	 * @param x    x start position of the rat
	 * @param y    y start position of the rat
	 * 
	 */
	public void placeRat(Rat rats, Direction dir, int x, int y) {
		board[x * EXTRA_PADDING][y * EXTRA_PADDING].addRat(rats, dir);
	}

	/**
	 * Put Death Rat onto game canvas.
	 * 
	 * @param rat death rat to be added to map
	 * @param dir direction the death rat came from
	 * @param x x start position of the death rat
	 * @param y y start position of the death rat
	 */
	public void placeRat(DeathRat rat, Direction dir, int x, int y) {
		board[x * EXTRA_PADDING][y * EXTRA_PADDING].addRat(rat, dir);
	}

	/**
	 * Goes through each Tile and moves the rat to the next tile before getting the
	 * tiles new list.
	 */
	public void runAllTiles() {
		// Send item to Rat make sure to have boolean to know if it is dead or not
		deathRatBuffer = new ArrayList<>();
		// Movement
		for (Tile t : allTiles) {
			t.setCurrRat();
		}
		// First give item to rat(s)
		for (Tile t : allTiles) {
			t.giveRatItem();
		}
		
		// Secondly move Death rats and any rats in its path
		for (Tile t : allTiles) {
			deathRatBuffer.addAll(t.getNextDeathRat());
		}
		
		// Before moving all other rats
		for (Tile t : allTiles) {
			t.getNextDirection();
		}
		
		// Now adding in death rat movements
		for (DeathRat dr : deathRatBuffer) {
			Main.addCurrMovement(dr.getXyPos(), dr.getD(), RatType.DEATH, dr.getMove());
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

	private boolean check(Tile t) {
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
					board[i][j].setNeighbourTiles(tiles.toArray(
							new Tile[2]), direction.toArray(new Direction[2]));
				}
			}
		}

	}

}
