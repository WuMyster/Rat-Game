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
	
	// Might need to split up this tile, giveItemsToRat should already have aliveRats list.
	// Return list of death rats on this tile.
	@Override
	public ArrayList<DeathRat> getNextDeathRat() {
		// Check number of rats and number of lists of rats to just assign it if needed.
		
		// Pass in ArrayList of rats on this tile.
		aliveRats = new ArrayList<>();
		for (Direction prevDirection : currBlock.keySet()) {
			aliveRats.addAll(currBlock.get(prevDirection));
		}
		// Pass in ArrayList of Rats to DeathRats ArrayList of rats still alive
//		for (Direction prevDirection : currDeath.keySet()) {
//			for (DeathRat dr : currDeath.get(prevDirection)) {
//				// aliveRats = dr.rats(aliveRats);
//			}
//		}
		
		// Now moving death rats
		ArrayList<DeathRat> drs = new ArrayList<>();
		for (Direction prevDirection : currDeath.keySet()) {
			Direction goTo = directions[0] == prevDirection ? directions[1] : directions[0];
			TileType t = neighbourTiles.get(goTo);
			for (DeathRat dr : currDeath.get(prevDirection)) {
				// If dr is alive if (dr.alive){
				
				t.moveDeathRat(dr, goTo.opposite());
				Main.addCurrMovement(X_Y_POS, goTo, RatType.DEATH, 4);
				//drs.add(dr);
				//dr.initalMove(X_Y_POS, goTo);
			}
		}
		
		// Remove fallen rats from list
		for (Direction prevDirection : currBlock.keySet()) {
			ArrayList<Rat> rs = currBlock.get(prevDirection);
			for(Rat r : rs) {
				if (!exists(r)) {
					rs.remove(r);
				}
				// rs.removeIf(n -> (!exists(r)));
			}
		}
		return drs;
	}
	
	private boolean exists(Rat r) {
		for (Rat rs : aliveRats) {
			if (rs == r) {
				// aliveRats.remove(r); //Cut down on costs?
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void moveDeathRat(DeathRat r, Direction prevDirection) {
		this.addRat(r, prevDirection);
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
						//Future want this to be a switch case statement ratList.get(i).getStatus() should return a RatType
						if (ratList.get(i).isChild()) {
							Main.addCurrMovement(X_Y_POS, goTo, RatType.BABY, 4);
							tile.getAcceleratedDirection(ratList.get(i), goTo.opposite());
							//timeTravel(ratList.get(i)); //Speeds up aging of rat
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
					
					Direction tmp = goTo;
					goTo = prevDirection;
					prevDirection = tmp;
				}
			}
		}
	}
	
	@Override
	public void getAcceleratedDirection(Rat r, Direction prevDirection) {
		//Direction goTo = directions[0] == prevDirection ? directions[1] : directions[0];
		this.addRat(r, prevDirection.opposite());
	}
	
	//Debug Speeds up aging
	private void timeTravel(Rat r) {
		for(int i = 0; i < 45; i++) {
			r.incrementAge();
		}
	}
}
