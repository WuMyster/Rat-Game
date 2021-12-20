
import java.util.ArrayList;

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

	@Override
	public void moveDeathRat(DeathRat dr, Direction prevDirection) {

		// For rats going towards the death rat (not including baby rat since they
		// shouldn't be on this tile
		ArrayList<Rat> currList = currBlock.get(prevDirection.opposite());
		ArrayList<Rat> escaped = new ArrayList<>();
		if (currList != null) {
			for (int i = 0; i < currList.size(); i++) {
				if (dr.killRat(currList.get(i), 1)) {
					GameGUI.addCurrMovement(X_Y_POS, prevDirection, currList.get(i).getStatus(), 2);
				} else {
					escaped.add(currList.get(i));
				}
			}
			currBlock.put(prevDirection.opposite(), escaped);
		}
		
		// Deal with rats staying put on this tile
		if (dr.isAlive()) {
			for (Direction dir : bufferNextBlock.keySet()) {
				ArrayList<Rat> ratList = bufferNextBlock.get(dir);
				escaped = new ArrayList<>();
				
				for (Rat r : ratList) {
					if (dr.killRat(r, 2)) {
						GameGUI.addCurrMovement(X_Y_POS, dir.opposite(), r.getStatus(), 0, 2);
					} else {
						escaped.add(r);
					}
				}
				bufferNextBlock.put(dir, escaped);
			}
		}
		

		// Adult rats going away from death rat (not including baby rat)
		if (dr.isAlive()) {
			currList = currBlock.get(prevDirection);
			escaped = new ArrayList<>();
			if (currList != null) {
				for (int i = 0; i < currList.size(); i++) {
					if (dr.killRat(currList.get(i), 1)) {
						GameGUI.addCurrMovement(X_Y_POS, prevDirection.opposite(), currList.get(i).getStatus(), 2);
					} else {
						escaped.add(currList.get(i));
					}
				}
				currBlock.put(prevDirection, escaped);
			}
		}

		if (dr.isAlive()) {
			Tile t = neighbourTiles.get(prevDirection.opposite());
			t.moveDeathRat(dr, prevDirection);
		}
	}

	@Override
	public void getRatInteractions() {
		super.getRatInteractions();

		ArrayList<ArrayList<Rat>> rs = RatController.ratInteractions(aliveRats);
		for (Rat r : rs.get(0)) {
			if (r.getPregCounter() > 7) {
				r.addPregStep();
			}
			Direction d;
			if (currBlock.get(directions[0]) == null) {
				d = directions[1];
			} else {
				d = currBlock.get(directions[0]).contains(r) ? directions[0] : directions[1];
			}
			bufferNextBlock.putIfAbsent(d, new ArrayList<>());
			bufferNextBlock.get(d).add(r);
		}
		aliveRats = rs.get(1);
	}

	/**
	 * Death Rat should not be on this tile. So return error message if there is one
	 */
	@Override
	public ArrayList<DeathRat> getNextDeathRat() {
		if (!currDeath.isEmpty() || !nextDeath.isEmpty()) {
			System.err.println("Death rat on invalid tile - light tile");
		}
		return new ArrayList<>();
	}

	@Override
	public void getNextDirection() {
		for (Direction prevDirection : currBlock.keySet()) {
			ArrayList<Rat> ratList = currBlock.get(prevDirection);

			if (!ratList.isEmpty()) {
				Direction goTo = prevDirection.opposite();
				Tile tile = neighbourTiles.get(goTo);
				for (int i = 0; i < ratList.size(); i++) {
					GameGUI.addCurrMovement(X_Y_POS, goTo, ratList.get(i).getStatus(), 4);
					tile.addRat(ratList.get(i), goTo.opposite());
				}
			}
		} 
		
		for (Direction dir : bufferNextBlock.keySet()) {
			for (Rat r : bufferNextBlock.get(dir)) {
				GameGUI.addCurrMovement(X_Y_POS, dir.opposite(), r.getStatus(), 0);
				this.addRat(r, dir);
			}
		}
	}

	/**
	 * Light tile won't decide where it goes, already predetermined by previous
	 * tile, will go straight to next tile.
	 */
	@Override
	public void getAcceleratedDirection(Rat r, Direction prevDirection) {
		Tile tile = neighbourTiles.get(prevDirection.opposite());
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
	public int numsRatsCanEnter(Tile t, int n) {
		if (t != surrounding[0]) {
			return surrounding[0].numsRatsCanEnter(null, n);
		} else {
			return surrounding[1].numsRatsCanEnter(null, n);
		}
	}
}
