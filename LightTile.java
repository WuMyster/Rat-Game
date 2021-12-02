
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
	 * 
	 * @param x x position on the map
	 * @param y y position on the map
	 */
	public LightTile(int x, int y) {
		super(new int[] { x, y });
	}

	@Override
	public void setNeighbourTiles(TileType[] tiles, Direction[] direction) {
		super.setNeighbourTiles(tiles, direction);
		this.surrounding = tiles;
	}

	@Override
	public void moveDeathRat(DeathRat dr, Direction prevDirection) {
		
		// For rats going towards the death rat (not including baby rat)
		ArrayList<Rat> dealing = currBlock.get(prevDirection.opposite());
		ArrayList<Rat> escaped = new ArrayList<>();
		if (dealing != null) {
			for (int i = 0; i < dealing.size(); i++) {
				if (dr.killRat(dealing.get(i), 1)) {
					Main.addCurrMovement(X_Y_POS, prevDirection, dealing.get(i).getStatus(), 2);
				} else {
					escaped.add(dealing.get(i));
				}
			}
			currBlock.put(prevDirection.opposite(), escaped);
		}
		
		// Adult rats going away from death rat (not including baby rat stuff)
		if (dr.isAlive()) {
			dealing = currBlock.get(prevDirection);
			if (dealing != null) {
				for (int i = 0; i < dealing.size(); i++) {
					if (dr.killRat(dealing.get(i), 1)) {
						Main.addCurrMovement(X_Y_POS, 
								prevDirection.opposite(), 
								dealing.get(i).getStatus(), 
								2);
					} else {
						escaped.add(dealing.get(i));
					}
				}
				currBlock.put(prevDirection, escaped);
			}
		}
		
		if (dr.isAlive()) {
			TileType t = neighbourTiles.get(prevDirection.opposite());
			t.moveDeathRat(dr, prevDirection);
		}
	}
	
	/**
	 * Death Rat should not be on this tile.
	 */
	@Override
	public ArrayList<DeathRat> getNextDeathRat() {
		return new ArrayList<>();
	}

	@Override
	public void getNextDirection() {
		for (Direction prevDirection : currBlock.keySet()) {
			ArrayList<Rat> ratList = currBlock.get(prevDirection);

			if (!ratList.isEmpty()) {
				Direction goTo = prevDirection.opposite();
				TileType tile = neighbourTiles.get(goTo);
				for (int i = 0; i < ratList.size(); i++) {
					RatType gen = ratList.get(i).getIsMale() ? RatType.MALE : RatType.FEMALE;
					Main.addCurrMovement(X_Y_POS, goTo, gen, 4);
					tile.addRat(ratList.get(i), goTo.opposite());
				}
			}
		}
	}

	/**
	 * Light tile won't decide where it goes, already predetermined by previous tile, 
	 * will go straight to next tile.
	 */
	@Override
	public void getAcceleratedDirection(Rat r, Direction prevDirection) {
		TileType tile = neighbourTiles.get(prevDirection.opposite());
		tile.getAcceleratedDirection(r, prevDirection.opposite());
	}

	/**
	 * This tile will not have any stop sign, so it will return number of rats for
	 * the next tile
	 * 
	 * @param t the tile that is requesting how many rats can go through
	 * @param n number of rats that tile the other tile has
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
