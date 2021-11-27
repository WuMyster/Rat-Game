import java.util.ArrayList;
import java.util.HashMap;

/**
 * A standard tile that has at most 2 tiles connected to it.
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
	 * where to go and tile class which rats are going to it and from what direction.
	 * 
	 * Tells rats on this tile which direction to go and other tile class which rats
	 * are going to it and from what direction.
	 */
	@Override
	public void getNextDirection() {
		for (Direction prevDirection : currBlock.keySet()) {
			ArrayList<Rat> ratList = currBlock.get(prevDirection);
			
			if (!ratList.isEmpty()) { 
				int i = giveRatItem(ratList.get(0)) ? 1 : 0;
				Direction goTo = directions[0] == prevDirection ? directions[1] : directions[0];
				int ratsGoForward; // Number of rats that can keep go in current direction
				
				while (i != ratList.size()) {	
					TileType tile = neighbourTiles.get(goTo);
					
					ratsGoForward = tile.numsRatsCanEnter(this, ratList.size());
					for (; i < ratsGoForward; i++) {
						if (ratList.get(i).isChild()) {
							Main.addCurrMovement(X_Y_POS, true, goTo);
							tile.getAcceleratedDirection(ratList.get(i), goTo.opposite());
							//timeTravel(ratList.get(i)); //Speeds up aging of rat
						} else {
							Main.addCurrMovement(X_Y_POS, false, goTo);
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
	
	//Debug Speeds up aging
	private void timeTravel(Rat r) {
		for(int i = 0; i < 45; i++) {
			r.incrementAge();
		}
	} 	
	
	@Override
	public void getAcceleratedDirection(Rat r, Direction prevDirection) {
		Direction goTo = directions[0] == prevDirection ? directions[1] : directions[0];
		this.addRat(r, goTo);
	}
}
