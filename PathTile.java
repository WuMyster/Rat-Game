import java.util.ArrayList;

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
		// Pass in ArrayList of Rats still alive to each DeathRat on the tile
		for (Direction prevDirection : currDeath.keySet()) {
			for (DeathRat dr : currDeath.get(prevDirection)) {
				aliveRats = dr.killRats(aliveRats, -1);
				
			}
		}
		
		// Now moves death rats
		ArrayList<DeathRat> drs = new ArrayList<>();
		for (Direction prevDirection : currDeath.keySet()) {
			Direction goTo = directions[0] == prevDirection ? directions[1] : directions[0];
			for (DeathRat dr : currDeath.get(prevDirection)) {
				if (dr.isAlive()) {
					
					TileType t;
					int i;
					do {
						t = neighbourTiles.get(goTo);
						i = t.numsRatsCanEnter(this, 1);
						
						Direction tmp = goTo;
						goTo = prevDirection;
						prevDirection = tmp;
					} while (i == 0);
					
					// Need better logic for this
					t.moveDeathRat(dr, prevDirection.opposite());
					drs.add(dr);
					dr.initalMove(X_Y_POS, prevDirection);
				}
				
			}
		}
		
		// Remove fallen rats from list.
		// Will not automatically move the rats in case other death rats come here.
		for (Direction prevDirection : currBlock.keySet()) {
			ArrayList<Rat> tmp = new ArrayList<>();
			ArrayList<Rat> rs = currBlock.get(prevDirection);
			if(rs != null) {
				for(Rat r : rs) {
					if (exists(r)) {
						tmp.add(r);
					}
				}
				currBlock.put(prevDirection, tmp);
			}
		}
		return drs;
	}
	
	/**
	 * Returns {@code true} if Rat is in alive rats list.
	 * @param r the rat to find
	 * @return {@code true} if rat exists in list
	 */
	private boolean exists(Rat r) {
		return aliveRats.remove(r);
	}
	
	//Death rat and stop sign stopping rats from moving away hence moving towards DR
	@Override
	public void moveDeathRat(DeathRat dr, Direction prevDirectionDR) {
		System.out.println("ASDF");
		Direction prevDirectionR = prevDirectionDR == directions[0] ? directions[1] : directions[0];
			ArrayList<Rat> rs1 = currBlock.get(prevDirectionR);
			ArrayList<Rat> rs2 = new ArrayList<>();
			if (rs1 != null) {
				System.out.println("Not null");
				
				//Check for stop sign?
				for(Rat r : rs1) {
					if (r.getStatus() == RatType.BABY) {
						if (dr.killRat(r, 2)) {
							Main.addCurrMovement(X_Y_POS, prevDirectionR.opposite(), RatType.BABY, 2);
						} else {
							// Should be moved afterwards if more death rats come
							rs2.add(r);
						}
					} else {
						rs2.add(r);
					}
				}
			
			// Have rs2 analysed in terms of movement
			//kill Rats
			rs1 = new ArrayList<>();
			for(Rat r : rs2) {
				if (dr.killRat(r, 2)) {
					Main.addCurrMovement(X_Y_POS, prevDirectionR.opposite(), r.getStatus(), 2);
				} else {
					rs1.add(r);
				}
			}
			
			currBlock.put(prevDirectionR, rs1);
		}
		
		// Now that all rats going towards DR from this tile are dealt with, deal with any stragglers who
	    // are bounced back by stop sign IF DR is alive and stop sign is present in next tile
		if (dr.isAlive()) {
			Direction goTo = prevDirectionDR == directions[0] ? directions[0] : directions[1];
			ArrayList<Rat> ratList = currBlock.get(goTo);
			int ratsGoToDeath = 0; 
			int i = 0;
//			while (i != ratList.size()) {	
//				TileType tile = neighbourTiles.get(goTo);
//				
//				ratsGoToDeath = tile.numsRatsCanEnter(this, ratList.size());
//				i += ratsGoToDeath;
//			}
			
			// Only check stop sign forward of rat
			if (ratList != null) {
				TileType tile = neighbourTiles.get(goTo);
				ratsGoToDeath = tile.numsRatsCanEnter(this, ratList.size());
				if (ratsGoToDeath != 0) {
					System.out.println(ratList);
					System.out.println(ratsGoToDeath);
					System.out.println("==");
					ratList = dr.killRats((new ArrayList<Rat>(ratList.subList(0, ratsGoToDeath))), 3);
				}
				// ratList = dr.killRats((ArrayList<Rat>) ratList.subList(0, ratsGoToDeath), 3); //Should all be adult rats
			}
		}
		
		
		
		if (dr.isAlive()) {
			this.addRat(dr, prevDirectionDR);
		}
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
