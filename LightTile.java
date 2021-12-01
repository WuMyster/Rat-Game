
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

	/**
	 * Will also store tiles around it not caring about direction??.
	 * 
	 * @param tiles     list of tiles around it
	 * @param direction list of direction to respective tile in tiles
	 */
	@Override
	public void setNeighbourTiles(TileType[] tiles, Direction[] direction) {
		super.setNeighbourTiles(tiles, direction);
		this.surrounding = tiles;
	}

	@Override
	public void moveDeathRat(DeathRat r, Direction prevDirection) {
		
		// For rats going towards the death rat
		ArrayList<Rat> dealing = currBlock.get(prevDirection.opposite());
		while(r.killRat(dealing.get(0), 1) && !dealing.isEmpty()) {
			dealing.remove(0);
		}
		currBlock.put(prevDirection.opposite(), dealing);
	}

	// Might need to split up this tile, giveItemsToRat should already have
	// aliveRats list.
	/**
	 * Death Rat should not be on this tile.
	 */
	@Override
	public ArrayList<DeathRat> getNextDeathRat() {
//		// Pass in ArrayList of rats on this tile.
//		aliveRats = new ArrayList<>();
//		for (Direction prevDirection : currBlock.keySet()) {
//			aliveRats.addAll(currBlock.get(prevDirection));
//		}
//		// Pass in ArrayList of Rats for each DeathRat -> ArrayList of rats still alive
//		for (Direction prevDirection : currDeath.keySet()) {
//			for (DeathRat dr : currDeath.get(prevDirection)) {
//				// aliveRats = dr.rats(aliveRats);
//			}
//		}
//		
//		ArrayList<DeathRat> drs = new ArrayList<>();
//		// Now moving death rats
//		for (Direction prevDirection : currDeath.keySet()) {
//			Direction goTo = directions[0] == prevDirection ? directions[1] : directions[0];
//			TileType t = neighbourTiles.get(goTo);
//			for (DeathRat dr : currDeath.get(prevDirection)) {
//				// If dr is alive if (dr.alive){
//				
//				t.moveDeathRat(dr, goTo.opposite());
//				// Main.addCurrMovement(X_Y_POS, goTo, RatType.DEATH, 4);
//				drs.add(dr);
//				dr.initalMove(X_Y_POS, goTo.opposite());
//			}
//		}
		
		
		//Since this will deal with all rats on tile, it should set currMovement = new HashMap<>();
		return null;
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
	
	// private buffer

	/**
	 * Light tile won't decide where it goes, already predetermined by previous tile
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
