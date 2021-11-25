import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * @author Jing Shiang Gu
 */
public class JunctionTile extends TileType {

	private Random rand = new Random();

	public JunctionTile(int x, int y) {
		super(new int[] { x, y });
	}

	/**
	 * Will choose a random direction for each Rat to go to.
	 */
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
					System.out.println(tile.isBlocked);

					ratsGoForward = tile.damageStopSign(ratList.size());
			
					for (; i < ratList.size(); i++) {
						if (ratList.get(i).isChild()) {
							Output.addCurrMovement(X_Y_POS, true, goTo);
							tile.getAcceleratedDirection(ratList.get(i), goTo.opposite());
							//timeTravel(ratList.get(i));
						} else {
							//TODO System.out.println("NOT CHILD");
							Output.addCurrMovement(X_Y_POS, false, goTo);
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
	 * @param prevDirection
	 * @return
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
		// TODO Auto-generated method stud
		System.out.println("Junction Tile speed not implemented yet");

		Direction goTo = getADirection(prevDirection.opposite());
		this.addRat(r, goTo.opposite());

	}
}
