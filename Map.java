
/**
 * Pass in
 * @author 2010573
 *
 */
public class Map {
	
	private String mapDesign;
	private int xHeight; //Can re-implement so these aren't stored
	private int yHeight;
	private Tile[][] board;
	
	//? Is final in the correct place?
	private static final char GRASS_TILE = 'G'; 
	private final static char PATH_TILE = 'P';
	private final static char BRIDGE_TILE = 'B';
	
	/**
	 * Creates Map instance
	 * @param mapDesign input string of map
	 * @param xHeight	max height of map
	 * @param yHeight	max width of map
	 */
	public Map (String mapDesign, int xHeight, int yHeight) {
		this.mapDesign = mapDesign;
		this.xHeight = xHeight;
		this.yHeight = yHeight;
		this.board = new Tile[yHeight][xHeight];
		createBoard();
		createGraph();
	}
	
	//For debug only
	public Tile[][] getBoard() {
		return board;
	}
	
	/**
	 * Creates 2d array of map
	 */
	private void createBoard() {
		int counter = 0;
		for (int i = 0; i < yHeight; i++) {
			for (int j = 0; j < xHeight; j++) {
				switch (mapDesign.charAt(counter++)) {
				case GRASS_TILE -> board[i][j] = null;
				case PATH_TILE -> board[i][j] = new PathTile();
				case BRIDGE_TILE -> board[i][j] = new BridgeTile();
				default -> System.out.println("Map error!");
				}
			}
		}
	}
	
	
	//Possible to implement the gui part too!
	/**
	 * Creates graph of the map
	 */
	private void createGraph() {
		for (int i = 0; i < yHeight; i++) {
			for (int j = 0; j < xHeight; j++) {
				
				if (board[i][j] != null) {
					int counter = 0;
					Tile[] tiles = new Tile[4];
					Direction[] direction = new Direction[4];
					
					//Check North
					if (i != 0) {
						if (board[i - 1][j] != null) {
							tiles[counter] = board[i - 1][j];
							direction[counter++] = Direction.NORTH;
						}
					}
					
					//Check East
					if (j != xHeight - 1) {
						if (board[i][j + 1] != null) {
							tiles[counter] = board[i][j + 1];
							direction[counter++] = Direction.EAST;
						}
					}
					
					//Check South
					if (i != yHeight - 1) {
						if (board[i + 1][j] != null) {
							tiles[counter] = board[i + 1][j];
							direction[counter++] = Direction.SOUTH;
						}
					}
					
					//Check West
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
