
/**
 * An invisible tile in between standard tiles, allow interactions between rats
 * and some items (death rat and bomb) outside of the standard tiles
 * 
 * @author Jing Shiang Gu
 *
 */
public class LightTile extends Tile {

	/**
	 * List of tiles next to this tile.
	 */
	private Tile[] surrounding;

	/**
	 * Creates a LightTile object storing its position.
	 * 
	 * @param x x position on the map
	 * @param y y position on the map
	 */
	public LightTile(int x, int y) {
		super(new int[] { x, y });
	}

	@Override
	public void setNeighbourTiles(Tile[] tiles, Direction[] direction) {
		super.setNeighbourTiles(tiles, direction);
		this.surrounding = tiles;
	}
}
