
/**
 * Pass in
 * @author 2010573
 *
 */
public class Map {
	
	private String mapDesign;
	private int xHeight; //Can reimplement so these aren't stored
	private int yHeight;
	private Tile[][] board;
	
	//? Is final in the correct place?
	private static final char GRASS_TILE = 'G'; 
	private final static char PATH_TILE = 'P';
	private final static char BRIDGE_TILE = 'B';
	
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
	private void createGraph() {
		
	}
	
}
