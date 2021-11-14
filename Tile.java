import java.util.ArrayList;
import java.util.HashMap;

public abstract class Tile {
	
	protected int itemHP = 0;
	protected int limit;
	protected Boolean isBlocked = false;
	
	protected Item itemOnTile;
	
	protected HashMap<Direction, Tile> neighbourTiles = new HashMap<>(limit);
	
	//Direction is direction to the previous tile
	protected HashMap<Direction, ArrayList<Rat>> nextBlock = new HashMap<>();
	protected HashMap<Direction, ArrayList<Rat>> currBlock = new HashMap<>();
	
	public void setNeighbourTiles (Tile[] tiles, Direction[] direction) {
		for (int i = 0; i < limit; i++) {
			neighbourTiles.put(direction[i], tiles[i]);
		}
	}
	
	public abstract void getNextDirection();
	
	public Boolean isTileBlocked() {
		return isBlocked;
	}
	
	/**
	 * Damages stop sign
	 * @param n
	 * @return
	 */
	public int damageStopSign(int n) {
		itemHP -= n;
		if (itemHP >= 0) {
			return 0;
		}
		isBlocked = false;
		return Math.abs(itemHP);
	}
	
	/**
	 * Add stop sign to tile
	 */
	public void placeStopSign() {
		itemHP = 20; //Should call Item class go get health of stop sign
		isBlocked = true;
		//GUI
	}
}
