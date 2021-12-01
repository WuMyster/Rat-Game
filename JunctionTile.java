import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * @author Jing Shiang Gu
 */
public class JunctionTile extends TileType {

	private Random rand = new Random();

	/**
	 * Creates a JunctionTile object storing its position.
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
					// TODO System.out.println(tile.X_Y_POS[0] + " " + tile.X_Y_POS[1]);
					//System.out.println(tile.isBlocked);

					ratsGoForward = tile.numsRatsCanEnter(this, ratList.size());
			
					for (; i < ratsGoForward; i++) {
						//Future want this to be a switch case statement ratList.get(i).getStatus() should return a RatType
						if (ratList.get(i).isChild()) {
							Main.addCurrMovement(X_Y_POS, goTo, RatType.BABY, 4);
							tile.getAcceleratedDirection(ratList.get(i), goTo.opposite());
							//timeTravel(ratList.get(i));
						} else {
							if (ratList.get(i).getDeathRat()) {
								Main.addCurrMovement(X_Y_POS, goTo, RatType.DEATH, 4);
								tile.getAcceleratedDirection(ratList.get(i), goTo.opposite());
							} else {
								RatType gen = ratList.get(i).getIsMale() ? RatType.MALE : RatType.FEMALE;
								Main.addCurrMovement(X_Y_POS, goTo, gen, 4);
								tile.addRat(ratList.get(i), goTo.opposite());
							}
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
		//System.out.println("Junction Tile speed not implemented yet");

		//Maybe try inserting .opposite to fix turn around issues
		//Direction goTo = getADirection(prevDirection);
		this.addRat(r, prevDirection.opposite());
	}
	
	//Debug Speeds up aging
		private void timeTravel(Rat r) {
			for(int i = 0; i < 45; i++) {
				r.incrementAge();
			}
		} 
}
