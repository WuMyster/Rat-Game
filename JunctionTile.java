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

				// Keep in mind stop signs!! Currently not implemented
				Direction goTo = getADirection(prevDirection);

				int i;
				for (i = 0; i < ratList.size(); i++) {
					neighbourTiles.get(goTo).addRat(ratList.get(i), goTo.opposite());
					// Tell rat to go to previous direction
					// Tell that Tile the direction the rat came from using prevDirection.opposite()
				}
				// Prev direction so it keeps going onwards??
				Output.addCurrMovement(X_Y_POS, false, goTo);
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

}
