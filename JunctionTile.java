import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * 
 * @author Jing Shiang Gu
 */
public class JunctionTile extends Tile {

	private Random rand = new Random();

	/**
	 * Confusingly, direction is where the Rat is going, so will need to oppsosite
	 * every direction.
	 */
	private HashMap<Direction, ArrayList<Rat>> buffer = new HashMap<>();

	/**
	 * Creates a JunctionTile object storing its position.
	 * 
	 * @param x x position on the map
	 * @param y y position on the map
	 */
	public JunctionTile(int x, int y) {
		super(new int[] { x, y });
	}
	
	private void createBuffer() {
		for (Direction prevDirectionRat : currBlock.keySet()) {
			ArrayList<Rat> ratList = currBlock.get(prevDirectionRat);
			if (!ratList.isEmpty()) {
				for (Rat r : ratList) {
					Direction goTo = getADirection(prevDirectionRat);
					Tile tile;
					int ratsGoForward;
					do {
						tile = neighbourTiles.get(goTo);
						ratsGoForward = tile.numsRatsCanEnter(this, 1);

						prevDirectionRat = goTo;
						goTo = getADirection(prevDirectionRat);
					} while (ratsGoForward == 0);

					buffer.putIfAbsent(prevDirectionRat, new ArrayList<>());
					buffer.get(prevDirectionRat).add(r);
				}
			}
		}
	}

	@Override
	public void moveDeathRat(DeathRat dr, Direction prevDirectionDR) {
		// TODO Auto-generated method stub

		// For now assign random directions to every rat
		createBuffer();

		ArrayList<Rat> ratsToDoom = buffer.get(prevDirectionDR);
		ArrayList<Rat> slowerRats = new ArrayList<>();
		if (ratsToDoom != null) {
			for (Rat r : ratsToDoom) {
				if (r.getStatus() == RatType.BABY) {
					if (dr.killRat(r, 2)) {
						Main.addCurrMovement(X_Y_POS, prevDirectionDR, RatType.BABY, 2);
					} else {
						slowerRats.add(r);
					}
				}
			}
		}

		ratsToDoom = new ArrayList<>();
		for (Rat r : slowerRats) {
			if (dr.killRat(r, 3)) {
				Main.addCurrMovement(X_Y_POS, prevDirectionDR, r.getStatus(), 1);
			} else {
				ratsToDoom.add(r);
			}
		}
		buffer.put(prevDirectionDR, ratsToDoom);
		
		if (dr.isAlive()) {
			this.addRat(dr, prevDirectionDR);
		}
	}

	/**
	 * Will choose a random direction for Rat to go to.
	 */
	@Override
	public void getNextDirection() {
		if (buffer.isEmpty()) {
			for (Direction prevDirection : currBlock.keySet()) {
				ArrayList<Rat> ratList = currBlock.get(prevDirection);
				if (!ratList.isEmpty()) {
					int i = 0;
					while (i != ratList.size()) {
	
						Direction goTo = getADirection(prevDirection);
						Tile tile = neighbourTiles.get(goTo);
	
						int ratsGoForward = tile.numsRatsCanEnter(this, ratList.size());
	
						for (; i < ratsGoForward; i++) {
							RatType status = ratList.get(i).getStatus();
							if (status == RatType.BABY) {
								Main.addCurrMovement(X_Y_POS, goTo, RatType.BABY, 4);
								tile.getAcceleratedDirection(ratList.get(i), goTo.opposite());
							} else {
								Main.addCurrMovement(X_Y_POS, goTo, ratList.get(i).getStatus(), 4);
								tile.addRat(ratList.get(i), goTo.opposite());
							}
						}
						prevDirection = goTo;
					}
				}
			}
		} else {
			for (Direction goTo : buffer.keySet()) {
				ArrayList<Rat> ratList = buffer.get(goTo);
				if (!ratList.isEmpty()) {
					Tile tile = neighbourTiles.get(goTo);
					for(Rat r : ratList) {
						RatType status = r.getStatus();
						if (status == RatType.BABY) {
							Main.addCurrMovement(X_Y_POS, goTo.opposite(), RatType.BABY, 4);
							tile.getAcceleratedDirection(r, goTo);
						} else {
							Main.addCurrMovement(X_Y_POS, goTo.opposite(), r.getStatus(), 4);
							tile.addRat(r, goTo);
						}
					}
				}
			}
		}
	}

	/**
	 * Get direction that isn't the same as the direction it came from or direction
	 * it tried.
	 * 
	 * @param prevDirection direction the rat came from or direction it tried
	 * @return direction that Rat should next try go to
	 */
	private Direction getADirection(Direction prevDirection) {
		int num = rand.nextInt(limit);
		while (directions[num] == prevDirection) {
			num = rand.nextInt(limit);
		}
		return directions[num];
	}

	@Override
	public void getAcceleratedDirection(Rat r, Direction prevDirection) {
		// System.out.println("Junction Tile speed not implemented yet");

		// Maybe try inserting .opposite to fix turn around issues
		// Direction goTo = getADirection(prevDirection);
		this.addRat(r, prevDirection.opposite());
	}

	// Debug Speeds up aging
	private void timeTravel(Rat r) {
		for (int i = 0; i < 45; i++) {
			r.incrementAge();
		}
	}

	@Override
	public ArrayList<DeathRat> getNextDeathRat() {
		// Check number of rats and number of lists of rats to just assign it if needed.
		// TODO
		
		if (currDeath.isEmpty()) {
			return new ArrayList<>();
		}
		
		for (Direction dir : bufferNextBlock.keySet()) {
			ArrayList<Rat> r = bufferNextBlock.get(dir);
			for (Direction prevDirection : currDeath.keySet()) {
				for (DeathRat dr : currDeath.get(prevDirection)) {
					bufferNextBlock.put(dir, dr.killRats(r, -1));
				}
			}
		}

		int beforeDeath = aliveRats.size();
		for (Direction prevDirection : currDeath.keySet()) {
			for (DeathRat dr : currDeath.get(prevDirection)) {
				aliveRats = dr.killRats(aliveRats, -1);
			}
		}

		// Now moving death rats
		ArrayList<DeathRat> drs = new ArrayList<>();
		for (Direction prevDirection : currDeath.keySet()) {
			Direction goTo = getADirection(prevDirection);
			for (DeathRat dr : currDeath.get(prevDirection)) {
				if (dr.isAlive()) {
					Tile t;
					int i;

					do {
						t = neighbourTiles.get(goTo);
						i = t.numsRatsCanEnter(this, 1);

						prevDirection = goTo;
						goTo = getADirection(prevDirection);
					} while (i == 0);

					t.moveDeathRat(dr, prevDirection.opposite());
					drs.add(dr);
					dr.initalMove(X_Y_POS, prevDirection);
				}
			}
		}

		// Remove fallen rats from list.
		// Will not automatically move the rats in case other death rats come here.
		// Compare size of lists!
		if (aliveRats.isEmpty()) {
			currBlock = new HashMap<>();
		} else if (aliveRats.size() == beforeDeath) {
			// Interesting as to why there is no change...
			System.err.println("aliveRats list has not changed! " + X_Y_POS[0] + " " +
			X_Y_POS[1]);
		} else { 
			for (Direction prevDirection : currBlock.keySet()) {
				ArrayList<Rat> tmp = new ArrayList<>();
				ArrayList<Rat> rs = currBlock.get(prevDirection);
				if (rs != null) {
					for (Rat r : rs) {
						if (exists(r)) {
							tmp.add(r);
						}
					}
					currBlock.put(prevDirection, tmp);
				}
			}
		}

		return drs;
	}

	/**
	 * Returns {@code true} if Rat is in alive rats list.
	 * 
	 * @param r the rat to find
	 * @return {@code true} if rat exists in list
	 */
	private boolean exists(Rat r) {
		return aliveRats.remove(r);
	}
}
