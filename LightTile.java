
import java.util.ArrayList;

/** 
 * An invisible tile in between standard tiles, allow interactions between rats
 * and some items (death rat and bomb) outside of the standard tiles
 * 
 * @author Jing Shiang Gu
 *
 */
public class LightTile extends TileType {

	/**
	 * List of tiles next to this tile.
	 */
	private TileType[] surrounding;

	/**
	 * Creates a LightTile object storing its position.
	 * @param x x position on the map
	 * @param y y position on the map
	 */
	public LightTile(int x, int y) {
		super(new int[] { x, y });
	}

	/**
	 * Will also store tiles around it not caring about direction??.
	 * @param tiles list of tiles around it
	 * @param direction list of direction to respective tile in tiles
	 */
	@Override
	public void setNeighbourTiles(TileType[] tiles, Direction[] direction) {
		super.setNeighbourTiles(tiles, direction);
		this.surrounding = tiles;
	}

	@Override
	public void getNextDirection() {
		for (Direction prevDirection : currBlock.keySet()) {
			ArrayList<Rat> ratList = currBlock.get(prevDirection);

			if (!ratList.isEmpty()) {
				Direction goTo = prevDirection.opposite(); // This can be opposite due to nature of tile
				TileType tile = neighbourTiles.get(goTo);
				for (int i = 0; i < ratList.size(); i++) {
					Output.addCurrMovement(X_Y_POS, false, goTo);
					tile.addRat(ratList.get(i), prevDirection);
				}
			}
		}
	}

	/**
	 * Light tile won't decide where it goes, already predetermined by previous tile
	 */
	@Override
	public void getAcceleratedDirection(Rat r, Direction prevDirection) {
		TileType tile = neighbourTiles.get(prevDirection.opposite());
		tile.getAcceleratedDirection(r, prevDirection.opposite());
	}

	/**
	 * This tile will not have any stop sign, so it will return 
	 * number of rats for the next tile
	 * @param t the tile that is requesting how many rats can go through
	 * @param n number of rats that tile has
	 */
	@Override
	public int numsRatsCanEnter(TileType t, int n) {
		if (t != surrounding[0]) {
			return surrounding[0].numsRatsCanEnter(null, n);
		} else {
			return surrounding[1].numsRatsCanEnter(null, n);
		}
	}
}
