import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * 
 * @author Jing Shiang Gu
 */
public class JunctionTile extends TileType {

	private Random rand = new Random();

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
	 * Will choose a random direction for each Rat to go to.
	 */
	@Override
	public void getNextDirection() {
		for (Direction prevDirection : currBlock.keySet()) {
			ArrayList<Rat> ratList = currBlock.get(prevDirection);
			if (!ratList.isEmpty()) {
				int ratsGoForward;
				int i = 0;
				while (i != ratList.size()) {

					Direction goTo = getADirection(prevDirection);
					TileType tile = neighbourTiles.get(goTo);

					ratsGoForward = tile.numsRatsCanEnter(this, ratList.size());

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

		// Pass in ArrayList of rats on this tile.
		aliveRats = new ArrayList<>();
		for (Direction prevDirection : currBlock.keySet()) {
			aliveRats.addAll(currBlock.get(prevDirection));
		}

		for (Direction prevDirection : currDeath.keySet()) {
			for (DeathRat dr : currDeath.get(prevDirection)) {
				aliveRats = dr.killRats(aliveRats, 0);

			}
		}

		// Now moving death rats
		ArrayList<DeathRat> drs = new ArrayList<>();
		for (Direction prevDirection : currDeath.keySet()) {
			
			Direction goTo = getADirection(prevDirection);
			
			TileType t = neighbourTiles.get(goTo);
			for (DeathRat dr : currDeath.get(prevDirection)) {
				if (dr.isAlive()) {
					t.moveDeathRat(dr, goTo.opposite());
					drs.add(dr);
					dr.initalMove(X_Y_POS, goTo);
				}
			}
		}
		return drs;
	}

	@Override
	public void moveDeathRat(DeathRat r, Direction prevDirection) {
		// TODO Auto-generated method stub
		System.out.println("Death rat movement not yet implemented!");
	}
}
