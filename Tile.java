import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author 2010573
 *
 */
public abstract class Tile {

	/**
	 * Type of item on tile, to be given to rat to allow them to interact
	 */
	protected Item itemOnTile;
	
	/**
	 * Health of item.
	 */
	protected int itemHP = 0;
	
	/**
	 * How the item loses health.
	 */
	protected ItemDamageType itemDamageType = null;
	
	/**
	 * Number of neighbour tiles.
	 */
	protected int limit; //Needs to be defined later
	
	/**
	 * Stop sign will cause the tile to not be accessed.
	 * If false, Rat can enter tile, 
	 * else Rat will have to take another direction.
	 */
	protected Boolean isBlocked = false;
	
	/**
	 * Tiles neighbouring current tile along with the direction to {@code Tile}.
	 */
	protected HashMap<Direction, Tile> neighbourTiles = new HashMap<>(limit);
	
	
	/**
	 * Direction is direction to the previous tile.
	 * Rat classes that are arriving to the tile in the next block of actions.
	 */
	protected HashMap<Direction, ArrayList<Rat>> nextBlock = new HashMap<>();
	
	/**
	 * Direction is direction to the previous tile
	 * Rats that the Tile is currently dealing with.
	 */
	protected HashMap<Direction, ArrayList<Rat>> currBlock = new HashMap<>();
	
	/**
	 * All directions from this tile to other tiles.
	 */
	protected Direction[] directions = new Direction[limit];
	
	/** Pick definition
	 * Will go through list of rats on tile and tell the rat class where to go
	 *  and tile class which rats are going to it and from what direction
	 *  
	 * Tells rats on this tile which direction to go and other tile 
	 * 	class which rats are going to it and from what direction
	 */
	public abstract void getNextDirection();
	
	/**
	 * For graph, this {@code Tile} will know about the {@code Tile} and
	 * {@code Direction} of other Tile classes.
	 * 
	 * @param tiles list of tiles that is neighbouring this tile
	 * @param direction list of directions to neighbouring tiles
	 */
	public void setNeighbourTiles (Tile[] tiles, Direction[] direction) {
		for (int i = 0; i < limit; i++) {
			neighbourTiles.put(direction[i], tiles[i]);
		}
	}
	
	/**
	 * Returns true if {@code Tile} cannot be accessed.
	 * @return {@code true} if this tile cannot be accessed to
	 */
	public Boolean isTileBlocked() {
		return isBlocked;
	}
	
	/**
	 * Damages stop sign with the number of rats bouncing off it.
	 * 
	 * @param n number of rats
	 * @return the number of rats that can pass through it
	 */
	public int damageStopSign(int n) {
		if (!isBlocked) {
			return n;
		}
		itemHP -= n;
		if (itemHP >= 0) {
			return 0;
		}
		isBlocked = false;
		return Math.abs(itemHP);
	}
	
	/**
	 * Place stop sign on tile.
	 */
	public void placeStopSign() {
		itemHP = 20; //Should call Item class go get health of stop sign
		isBlocked = true;
		//GUI
	}
}
