import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Jing Shiang Gu
 *
 */
public abstract class TileType {

	/**
	 * Type of item on tile, to be given to rat to allow them to interact
	 */
	protected Item itemOnTile;

	/**
	 * Health of item.
	 */
	protected int itemHP;

	/**
	 * How the item loses health.
	 */
	protected ItemDamageType itemDamageType;

	/**
	 * Stop sign will cause the tile to not be accessed. If false, Rat can enter
	 * tile, else Rat will have to take another direction.
	 */
	protected Boolean isBlocked;

	/**
	 * Tiles neighbouring current tile along with the direction to {@code Tile}.
	 */
	protected HashMap<Direction, TileType> neighbourTiles;

	/**
	 * Direction is direction to the previous tile. Rat classes that are arriving to
	 * the tile in the next block of actions.
	 */
	protected HashMap<Direction, ArrayList<Rat>> nextBlock = new HashMap<>();

	/**
	 * Direction is direction to the previous tile Rats that the Tile is currently
	 * dealing with.
	 */
	protected HashMap<Direction, ArrayList<Rat>> currBlock;

	/**
	 * All directions from this tile to other tiles.
	 */
	protected Direction[] directions;

	/**
	 * Number of neighbouring Tiles and their direction
	 */
	protected int limit;

	/**
	 * Final position of tile, won't move.
	 */
	protected final int[] X_Y_POS;

	/**
	 * Pick definition Will go through list of rats on tile and tell the rat class
	 * where to go and tile class which rats are going to it and from what direction
	 * 
	 * Tells rats on this tile which direction to go and other tile class which rats
	 * are going to it and from what direction
	 */
	public abstract void getNextDirection();
	
	/**
	 * To skip past any lightTiles. (Speed 2, for baby rats)
	 */
	public abstract void getAcceleratedDirection(Rat r, Direction prevDirection);

	/**
	 * Constructor for most normal tiles.
	 * 
	 * @param xyPos the position of the Tile
	 */
	public TileType(int[] xyPos) {
		this.X_Y_POS = xyPos;
		resetTile();
	}
	
	private void resetTile() {
		itemOnTile = null;
		itemHP = 0;
		itemDamageType = null;
		isBlocked = false;
		nextBlock =  new HashMap<>();	
	}

	/**
	 * For Tiles that used by Rat but cannot be placed.
	 * 
	 * @param xyPos       The position on the board where this Tile is
	 * @param isPlaceable False if item is not placeable on tile, {@code true} by
	 *                    default
	 */
	public TileType(int[] xyPos, boolean isPlaceable) {
		this(xyPos);
		this.isBlocked = isPlaceable;
	}

	/**
	 * For graph, this {@code Tile} will know about the {@code Tile} and
	 * {@code Direction} of other {@code Tile} classes.
	 * 
	 * @param tiles     list of tiles that is neighbouring this tile
	 * @param direction list of directions to neighbouring tiles
	 */
	public void setNeighbourTiles(TileType[] tiles, Direction[] direction) {
		this.limit = tiles.length;
		this.neighbourTiles = new HashMap<>(limit);
		for (int i = 0; i < tiles.length; i++) {
			this.neighbourTiles.put(direction[i], tiles[i]);
		}
		this.directions = direction;
	}
	
	//public void set

	/**
	 * Switches out old list of Rats the tile was working with, with the list the
	 * Tile is going to be working on now
	 */
	public void setCurrRat() {
		currBlock = nextBlock;
		nextBlock = new HashMap<>();
	}

	/**
	 * Returns true if {@code Tile} cannot be accessed. XX
	 * In future, should be removed to just call damageStopSign. TODO
	 * 
	 * @return {@code true} if this tile cannot be accessed to
	 */
	public Boolean isTileBlocked() {
		return isBlocked;
	}

	/**
	 * Damages stop sign with the number of rats bouncing off it. XX
	 * 
	 * @param n number of rats
	 * @return the number of rats that can pass through it
	 */
	public int damageStopSign(int n) {
		if (!isBlocked) {
			return n;
		}
		itemHP -= n;
		if (itemHP > 0) {
			return 0;
		}
		Output.removeStopSign(X_Y_POS);
		isBlocked = false;
		return Math.abs(itemHP);
	}

	/**
	 * Place stop sign on tile. XX
	 */
	public void placeStopSign() {
		itemHP = 3; // Should call Item class go get health of stop sign
		isBlocked = true;
		// GUI
	}
	
	public void blowUp() {
		//Item delete
		//Rat delete, rat tell rat controller
		resetTile();
		System.out.println("BLOWN UP");
	}
	
	public void placeBomb() {
		blowUp();
	}

	/**
	 * Add rat from other tile to this tile
	 * 
	 * @param r rat to be added to this Tile
	 * @param d direction the rat came from
	 */
	public void addRat(Rat r, Direction d) {
		//System.out.println("Trying to place");
		nextBlock.putIfAbsent(d, new ArrayList<Rat>());
		nextBlock.get(d).add(r);
	}
}
