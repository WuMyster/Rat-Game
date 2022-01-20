import java.util.HashMap;
import java.util.Random;

/**
 * This class provides a skeletal implementation of the {@code Tile}, it is a
 * superclass of all {@code Tile}s.
 * 
 * @author Jing Shiang Gu
 * @author Andrew Wu
 */
public abstract class Tile {

	/**
	 * Tiles neighbouring current tile along with the direction to {@code Tile}.
	 */
	protected HashMap<Direction, Tile> neighbourTiles;
	
	/**
	 * All directions from this tile to other tiles.
	 */
	protected Direction[] directions;

	/**
	 * Number of neighbouring Tiles.
	 */
	protected int limit;

	/**
	 * Final position of tile, won't move.
	 */
	protected final int[] X_Y_POS;

	/**
	 * Basic XY position of the tile from the board.
	 */
	protected final int[] ORIGINAL_X_Y_POS;
	
	/**
	 * Random generator.
	 */
	protected Random rand = new Random();
	
	/**
	 * Determines if this tile needs to be blown.
	 */
	protected boolean detontate = false;

	private Object itemOnTile;
	
	/**
	 * Constructor for tiles.
	 * 
	 * @param xyPos the position of the Tile
	 */
	public Tile(int[] xyPos) {
		this.X_Y_POS = xyPos;
		this.ORIGINAL_X_Y_POS = new int[] { X_Y_POS[0] / Board.EXTRA_PADDING,
				X_Y_POS[1] / Board.EXTRA_PADDING };
	}

	/**
	 * For graph, this {@code Tile} will know about the {@code Tile} and
	 * {@code Direction} of other {@code Tile} classes.
	 * 
	 * @param tiles     list of tiles that is neighbouring this tile
	 * @param direction list of directions to neighbouring tiles
	 */
	public void setNeighbourTiles(Tile[] tiles, Direction[] direction) {
		this.limit = tiles.length;
		this.neighbourTiles = new HashMap<>(limit);
		for (int i = 0; i < tiles.length; i++) {
			this.neighbourTiles.put(direction[i], tiles[i]);
		}
		this.directions = direction;
	}

	protected boolean setTileItem(Item i) {
        if (itemOnTile != null) {
            return false;
        }
        itemOnTile = i;
        return true;
    }
	
	public boolean checkTile() {
		return itemOnTile instanceof StopSign;
	}
}
