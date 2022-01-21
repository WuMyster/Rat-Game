import java.util.ArrayList;
import java.util.HashMap;

/**
 * A standard tile that has at most 2 tiles connected to it.
 * 
 * @author Jing Shiang Gu
 *
 */
public class PathTile extends Tile {

	/**
	 * Constructor to set its location
	 * 
	 * @param x number of tiles away from the left
	 * @param y number of tiles away from the top
	 */
	public PathTile(int x, int y) {
		super(new int[] { x, y });
	}

	// Might need to split up this tile, giveItemsToRat should already have
	// aliveRats list.
	// Return list of death rats on this tile.
	@Override
	public ArrayList<DeathRat> getNextDeathRat() {
		// Check number of rats and number of lists of rats to just assign it if needed.
		
		if (currDeath.isEmpty()) {
			return new ArrayList<>();
		}
		
		setDeathRat(); // Ensures only moving death rats are considered
		
		int beforeDeathInter = aliveRats.size();
		
		// Pass in ArrayList of Moving Rats still alive to each DeathRat on the tile
		for (Direction prevDirection : currDeath.keySet()) {
			for (DeathRat dr : currDeath.get(prevDirection)) {
				aliveRats = dr.killRats(aliveRats, -1);
			}
		}
		
		// Now moves death rats
		ArrayList<DeathRat> drs = moveEachDeathRat();
		
		// Remove fallen rats from list.
		// Will not automatically move the rats in case other death rats come here.
		if (aliveRats.isEmpty()) {
			currBlock = new HashMap<>();
		} else if (aliveRats.size() == beforeDeathInter) {
			// Interesting as to why there is no change...
			System.err.println("aliveRats list has not changed! "  + X_Y_POS[0] + " " +
					X_Y_POS[1]);
		} else {
			// done by method correctList() in Tile.java
			correctList();
		}
		return drs;
	}
	
	/**
	 * Move all alive Death Rats and return list of alive Death Rats
	 * @return list of alive Death Rats
	 */
	private ArrayList<DeathRat> moveEachDeathRat() {
		ArrayList<DeathRat> drs = new ArrayList<>();
		for (Direction prevDirection : currDeath.keySet()) {
			Direction goTo = directions[0] == prevDirection ? directions[1] : directions[0];
			for (DeathRat dr : currDeath.get(prevDirection)) {
				if (dr.isAlive()) {
					Tile t;
					if (dr instanceof SuperDeathRat) {
						Direction d = ((SuperDeathRat) dr).chooseDirection(X_Y_POS[0], X_Y_POS[1]);
						t = neighbourTiles.get(d);
						t.moveDeathRat(dr, d.opposite());
						dr.initalMove(X_Y_POS, d);
						removeItem();
					} else {
						int i;
						do {
							t = neighbourTiles.get(goTo);
							i = t.numsRatsCanEnter(this, 1);

							Direction tmp = goTo;
							goTo = prevDirection;
							prevDirection = tmp;
						} while (i == 0);

						// Need better logic for this
						t.moveDeathRat(dr, prevDirection.opposite());
						dr.initalMove(X_Y_POS, prevDirection);
					}
					drs.add(dr);
				}
			}
		}
		return drs;
	}

	// Death rat and stop sign stopping rats from moving away hence moving towards
	// DR
	@Override
	public void moveDeathRat(DeathRat dr, Direction prevDirectionDR) {
		// Deal with all rats going towards Death Rat
		
		Direction dirToDeath = prevDirectionDR == directions[0] ? directions[1] : directions[0];
		Direction dirAwayDeath = prevDirectionDR == directions[1] ? directions[1] : directions[0];
		ArrayList<Rat> currList = currBlock.get(dirToDeath);
		ArrayList<Rat> escaped = new ArrayList<>();
		
		if (currList != null) {
			// Baby rats are faster so it will reach Death Rat first
			for (Rat r : currList) {
				if (r.getStatus() == RatType.BABY) {
					if (dr.killRat(r, 2)) {
						GameGUI.addCurrMovement(X_Y_POS, dirToDeath.opposite(), RatType.BABY, 2);
					} else {
						// Should be moved afterwards if more death rats come
						escaped.add(r); 
					}
				} else {
					escaped.add(r);
				}
			}

			// Then Death Rats will deal with slower adult rats (If contains baby rat then it has
			// already died, so no adults will die before baby
			currList = escaped;
			escaped = new ArrayList<>();
			for (Rat r : currList) {
				if (dr.killRat(r, 3)) {
					GameGUI.addCurrMovement(X_Y_POS, dirToDeath.opposite(), r.getStatus(), 1);
				} else {
					escaped.add(r);
				}
			}
			currBlock.put(dirToDeath, escaped);
		}
		// Now that all rats going towards DR from this tile are dealt with, deal with
		// any stragglers who are bounced back by stop sign IF DR is alive and stop sign
		// is present in next tile - basically same as before
		
		currList = currBlock.get(dirAwayDeath);
		int ratsGoToDeath = -1;
		int beforeDeath = 0;
		if (dr.isAlive() && currList != null) {
			beforeDeath = currList.size();
			Tile tile = neighbourTiles.get(dirToDeath); //?
			
			// Number of rats towards death	after boucing off stop sign		
			ratsGoToDeath = currList.size() - tile.numsRatsCanEnter(this, currList.size());
			int i = 0;
			// Let Death Rat first deal with Baby rats
			for (; i < ratsGoToDeath && i < currList.size(); i++) {
				Rat r = currList.get(i);
				if (r.getStatus() == RatType.BABY) {
					if (dr.killRat(currList.get(i), 2)) {
						GameGUI.addCurrMovement(X_Y_POS, dirToDeath.opposite(), RatType.BABY, 2);
					} else {
						escaped.add(r);
					}
				} else {
					escaped.add(r);
				}
			}
			
			// Now deal with adult rats (using same list)
			currList = escaped;
			escaped = new ArrayList<>();
			for (i = 0; i < currList.size(); i++) {
				Rat r = currList.get(i);
				if (dr.killRat(currList.get(i), 3)) {
					GameGUI.addCurrMovement(X_Y_POS, dirToDeath.opposite(), r.getStatus(), 1);
				} else {
					escaped.add(r);
				}
			}
			currBlock.put(dirToDeath, escaped);
			
			// Now get rats that have bounced back due to stop sign that DR haven't yet killed
			ArrayList<Rat> a = new ArrayList<>(currBlock.get(dirAwayDeath).subList(i, beforeDeath));
			if (a != null) {
				currBlock.put(dirAwayDeath, a);
			}
			currList = escaped;
			escaped = new ArrayList<>(); 
		}

		// Does not deal with non-moving rats as rats on this tile will be dealt with next time 
		// the death rat starts moving
		if (dr.isAlive()) {
			this.addRat(dr, prevDirectionDR);
		}
	}

	/**
	 * Pick definition Will go through list of rats on tile and tell the rat class
	 * where to go and tile class which rats are going to it and from what
	 * direction.
	 * 
	 * Tells rats on this tile which direction to go and other tile class which rats
	 * are going to it and from what direction.
	 */
	@Override
	public void getNextDirection() {
		// For moving rats
		for (Direction prevDirection : currBlock.keySet()) {
			ArrayList<Rat> ratList = currBlock.get(prevDirection);

			if (!ratList.isEmpty()) {
				int i = 0;
				Direction goTo = directions[0] == prevDirection ? directions[1] : directions[0];
				int ratsGoForward; // Number of rats that can keep go in current direction

				while (i != ratList.size()) {
					Tile tile = neighbourTiles.get(goTo);

					ratsGoForward = tile.numsRatsCanEnter(this, ratList.size());
					for (; i < ratsGoForward; i++) {
						if (ratList.get(i).isChild()) {
							GameGUI.addCurrMovement(X_Y_POS, goTo, RatType.BABY, 4);
							tile.getAcceleratedDirection(ratList.get(i), goTo.opposite());
						} else {
							GameGUI.addCurrMovement(X_Y_POS, goTo, ratList.get(i).getStatus(), 4);
							tile.addRat(ratList.get(i), goTo.opposite());
						}
					}
					Direction tmp = goTo;
					goTo = prevDirection;
					prevDirection = tmp;
				}
			}
		}
	}

	@Override
	public void getRatInteractions() {
		super.getRatInteractions();

		// This method should be moved up
		ArrayList<ArrayList<Rat>> rs = RatController.ratInteractions(aliveRats);	
		for (Rat r : rs.get(0)) {
			for (Direction dir : directions) {
				ArrayList<Rat> rats = currBlock.get(dir);
				if (rats != null) {
					if (currBlock.get(dir).contains(r)) {
						this.addRat(r, dir);
						GameGUI.addCurrMovement(X_Y_POS, dir.opposite(), r.getStatus(), 0);
					}
				}
			}
		}
		
		aliveRats = rs.get(1);
	}
	
	@Override
	public void getAcceleratedDirection(Rat r, Direction prevDirection) {
		this.addRat(r, prevDirection.opposite());
	}
}
