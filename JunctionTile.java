import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Tile that has 3 or more tiles connected to it.
 * @author Jing Shiang Gu
 */
public class JunctionTile extends Tile {

	/**
	 * Random generator for direction.
	 */
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
					for (Rat r : ratList) {
						RatType status = r.getStatus();
						if (status == RatType.BABY) {
							Main.addCurrMovement(X_Y_POS, goTo, RatType.BABY, 4);
							tile.getAcceleratedDirection(r, goTo.opposite());
						} else {
							Main.addCurrMovement(X_Y_POS, goTo, r.getStatus(), 4);
							tile.addRat(r, goTo.opposite());
						}
					}
				}
			}
		}
		// For non-moving rats
		for (Direction prevDirection : bufferNextBlock.keySet()) {
			ArrayList<Rat> ratList = bufferNextBlock.get(prevDirection);
			// Similar to above but no need to check for stop signs
			for (Rat r : ratList) {
				Main.addCurrMovement(X_Y_POS, prevDirection.opposite(), r.getStatus(), 0);
				this.addRat(r, prevDirection.opposite());
			}
		}
	}
	
	@Override
	public void getRatInteractions() {
		super.getRatInteractions();
		
		// This implementation should be moved up
		ArrayList<ArrayList<Rat>> rs = RatController.ratInteractions(aliveRats);	
		
		ArrayList<Rat> asdf = rs.get(0);
		for (Rat r : asdf) {
			Direction d = null;
			
			for (Direction dir : directions) {
				ArrayList<Rat> a = currBlock.get(dir);
				if (a == null) {
					
				} else if (a.contains(r)) {
					d = dir;
				}
			}
			bufferNextBlock.putIfAbsent(d, new ArrayList<>());
			bufferNextBlock.get(d).add(r);
		}	
		aliveRats = rs.get(1);
	}

	@Override
	public void getAcceleratedDirection(Rat r, Direction prevDirection) {
		this.addRat(r, prevDirection.opposite());
	}
	
	/**
	 * Set up a predetermined route for each rat, so no rat will pass by a death rat
	 */
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

		for (Direction dir : bufferNextBlock.keySet()) {
			ArrayList<Rat> r = bufferNextBlock.get(dir);
			for (Direction prevDirection : currDeath.keySet()) {
				for (DeathRat dR : currDeath.get(prevDirection)) {
					bufferNextBlock.put(dir, dR.killRats(r, -1));
				}
			}
		}
		
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
				} else {
					slowerRats.add(r);
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

	@Override
	public ArrayList<DeathRat> getNextDeathRat() {
		// Check number of rats and number of lists of rats to just assign it if needed.
		// TODO
 
		if (currDeath.isEmpty()) {
			return new ArrayList<>();
		}

		setDeathRat(); // Ensures only moving death rats are considered
		
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
			buffer = new HashMap<>();
			nextBlock = new HashMap<>();
		} else if (aliveRats.size() == beforeDeath) {
			// Interesting as to why there is no change...
			System.err.println("aliveRats list has not changed! " + X_Y_POS[0] + " " + X_Y_POS[1]);
		} else {
			// Will run correctList from Tile.java
		}
		return drs;
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
}
