import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Jing Shiang Gu
 *
 */
public class PathTile extends TileType {

	/**
	 * Constructor to set its location
	 * 
	 * @param x number of tiles away from the left
	 * @param y number of tiles away from the top
	 */
	public PathTile(int x, int y) {
		super(new int[] { x, y });
	}

	/**
	 * Pick definition Will go through list of rats on tile and tell the rat class
	 * where to go and tile class which rats are going to it and from what direction
	 * 
	 * Tells rats on this tile which direction to go and other tile class which rats
	 * are going to it and from what direction
	 */
	@Override
	public void getNextDirection() {
		for (Direction prevDirection : currBlock.keySet()) {
			ArrayList<Rat> ratList = currBlock.get(prevDirection);
			
			if (!ratList.isEmpty()) { 
				int i = 0;
				Direction goTo = directions[0] == prevDirection ? directions[1] : directions[0];
				int ratsGoForward; // Number of rats that can keep going onwards
				
				while (i != ratList.size()) {	
							
					TileType a = neighbourTiles.get(goTo);
					if (a.isTileBlocked()) {
						ratsGoForward = a.damageStopSign(ratList.size());
					} else {
						ratsGoForward = ratList.size();
					}
					
					TileType tile = neighbourTiles.get(goTo);
					for (; i < ratsGoForward; i++) {
						Output.addCurrMovement(X_Y_POS, ratList.get(i).isChild(), goTo);
						tile.addRat(ratList.get(i), goTo.opposite());
					}
					
					Direction tmp = goTo;
					goTo = prevDirection;
					prevDirection = tmp;
				// Prev direction so it keeps going onwards??
				}
			}
		}
	}

}
