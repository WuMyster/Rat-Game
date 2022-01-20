
/**
 * Kind of a filler class whilst I try to find an alternative
 * to finding a tile and the best direction to it.
 * @author J
 *
 */
public class TileAndDir {

	final Tile tile;
	final Direction dir;
	
	public TileAndDir(Tile t, Direction d) {
		this.tile = t;
		this.dir = d;
	}
	
	public boolean equals(Tile t) {
		if (t == null) {
			return false;
		}
		return t.equals(tile);
	}
}
