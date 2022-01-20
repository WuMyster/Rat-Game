
/**
 * Kind of a filler class whilst I try to find an alternative
 * to finding a tile and the best direction to it.
 * @author J
 *
 */
public class TileAndDir {

	/**
	 * Tile that will be explored and stored.
	 */
	final Tile tile;
	
	/**
	 * Initial direction of the tile that connected to this tile.
	 */
	final Direction dir;
	
	/**
	 * Constructor sets the tile and inital direction to get
	 * to this tile
	 * @param t		tile that will be explored then stored
	 * @param d		initial direction to get to this tile
	 */
	public TileAndDir(Tile t, Direction d) {
		this.tile = t;
		this.dir = d;
	}
	
	/**
	 * Checks if this class has the same tile as input.
	 * @param t 	tile to check
	 * @return		boolean if the tile is the same as tile 
	 * 				in this class
	 */
	public boolean equals(Tile t) {
		if (t == null) {
			return false;
		}
		return t.equals(tile);
	}
}
