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
			//TODO System.out.println(ratList.size());
			
			if (!ratList.isEmpty()) { 
				//TODO System.out.println(X_Y_POS[0] + " " + X_Y_POS[1]);
				int i = 0;
				Direction goTo = directions[0] == prevDirection ? directions[1] : directions[0];
				int ratsGoForward; // Number of rats that can keep going onwards
				
				while (i != ratList.size()) {	
							
					TileType tile = neighbourTiles.get(goTo);
					//TODO System.out.println(tile.X_Y_POS[0] + " " + tile.X_Y_POS[1]);
					System.out.println(tile.isBlocked);
					if (tile.isTileBlocked()) {
						ratsGoForward = tile.damageStopSign(ratList.size());
					} else {
						ratsGoForward = ratList.size();
					}
					
					for (; i < ratsGoForward; i++) {
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
					
					Direction tmp = goTo;
					goTo = prevDirection;
					prevDirection = tmp;
				// Prev direction so it keeps going onwards??
				}
			}
		}
	}
	
	//Speeds up aging speed
	private void timeTravel(Rat r) {
		for(int i = 0; i < 45; i++) {
			r.incrementAge();
		}
	} 	
	
	@Override
	public void getAcceleratedDirection(Rat r, Direction prevDirection) {
		Direction goTo = directions[0] == prevDirection ? directions[1] : directions[0];
		
		this.addRat(r, goTo);
		//TODO System.out.println("PathTile accel: " + X_Y_POS[0] + " " + X_Y_POS[1]);
	}
}
