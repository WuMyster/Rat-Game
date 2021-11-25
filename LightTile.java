
import java.util.ArrayList;

/**
 * Tile to go in between main, visible tiles, this is to allow interaction
 * between rats (and death rats) outside of normal tiles. Will only need to deal
 * with some items: death rat(if item), bomb.
 * 
 * @author 2010573
 *
 */
public class LightTile extends TileType {

	private TileType[] surrounding;

	public LightTile(int x, int y) {
		super(new int[] { x, y });
	}

	private void setTileNeighbours() {
		surrounding = neighbourTiles.values().toArray(new TileType[2]);
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
		// System.out.println("Light: " + X_Y_POS[0] + " " + X_Y_POS[1]);
	}

	/**
	 * Does not hold any items, including stop sign, so will only broadcast stop
	 * sign status around it.
	 */
	@Override
	public Boolean isTileBlocked() {
		System.out.println("DON'T CALL ME");
		return null;
	}

	@Override
	public int damageStopSign(TileType t, int n) {
		try {
			//System.out.println("LightTile: " + surrounding[0].X_Y_POS[0] + " " + surrounding[0].X_Y_POS[1]);
			if (t != surrounding[0]) {
				return surrounding[0].damageStopSign(null, n);
			} else {
				return surrounding[1].damageStopSign(null, n);
			}
		} catch (NullPointerException e) {
			setTileNeighbours();
			//System.out.println("LightTile: " + surrounding[0].X_Y_POS[0] + " " + surrounding[0].X_Y_POS[1]);
			if (t != surrounding[0]) {
				return surrounding[0].damageStopSign(null, n);
			} else {
				return surrounding[1].damageStopSign(null, n);
			}
		}
	}
}
