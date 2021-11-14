import java.util.ArrayList;
import java.util.HashMap;

public class PathTile extends Tile {
	
	private HashMap<Tile, Direction> neighbourTiles = new HashMap<>(2);
	
	private Item itemOnTile;
	private int itemHP = 0;
	private Boolean isBlocked = false;
	
	private HashMap<Direction, ArrayList<Rat>> nextBlock = new HashMap<>();
	private HashMap<Direction, ArrayList<Rat>> currBlock = new HashMap<>();
	
	
	public void setNeighbourTiles (Tile[] tiles, Direction[] direction) {
		for (int i = 0; i < 2; i++) {
			neighbourTiles.put(tiles[i], direction[i]);
		}
	}
	
	public Direction getNextDirection() {
		return Direction.NORTH;
	}
	
	public void placeStopSign() {
		itemHP = 20;
		isBlocked = true;
	}
	
	public Boolean isTileBlocked() {
		return isBlocked;
	}
	
	public int itemHealth(int n) {
		itemHP -= n;
		if (itemHP >= 0) {
			return 0;
		}
		isBlocked = false;
		return Math.abs(itemHP);
	}
}
