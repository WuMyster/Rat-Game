import java.util.ArrayList;
import java.util.HashMap;

/**
 * Superclass of all tile types.
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
	 * List of Rats that are alive after dealing with any items and Death Rats
	 */
	protected ArrayList<Rat> aliveRats;

	/**
	 * Direction is direction to the previous tile. Rat classes that are arriving to
	 * the tile in the next block of actions. Does not include Death Rat.
	 */
	protected HashMap<Direction, ArrayList<Rat>> nextBlock = new HashMap<>();

	/**
	 * Direction is direction to the previous tile Rats that the Tile is currently
	 * dealing with. Does not include Death Rat.
	 */
	protected HashMap<Direction, ArrayList<Rat>> currBlock;
	
	/**
	 * Death Rat that is arriving to this tile. 
	 */
	protected HashMap<Direction, ArrayList<DeathRat>> nextDeath = new HashMap<>();
	
	/**
	 * Death Rats that are currently on tile. Will check if still alive.
	 */
	protected HashMap<Direction, ArrayList<DeathRat>> currDeath = new HashMap<>();

	/**
	 * All directions from this tile to other tiles.
	 */
	protected Direction[] directions;

	/**
	 * Number of neighbouring Tiles.
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
	 * Will be run 2nd, after items deal with all rats aside from death rat.
	 */
	public abstract ArrayList<DeathRat> getNextDeathRat();
	
	/**
	 * For now only deals with one moving death rat between 3 tiletypes
	 */
	public abstract void moveDeathRat(DeathRat r, Direction prevDirection);

	/**
	 * Constructor for most normal tiles.
	 * 
	 * @param xyPos the position of the Tile
	 */
	public TileType(int[] xyPos) {
		this.X_Y_POS = xyPos;
		resetTile();
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

	/**
	 * Returns true if rat dies after being given item
	 * @param r the rat receiving the item
	 * @return {@code true} if rat dies after being given item
	 */
	protected boolean giveRatItem(Rat r) {
		if (itemOnTile == null) {
			return false;
		}
		//Check item damage type and remove health as necessary
		if (itemHP == 0) {
			//Run method to do something if needed e.g. bomb
			itemOnTile = null;
		}
		
		//Method to give item away
		
		return true;
	}
	
	//??????? TODO }Item{
	protected boolean setTileItem(Item i, int x, int y) {
//		if (i instanceof Item) { //StopSign
//			//itemHP = StopSign. HEALTH
//			isBlocked = true;
//			//itemOnTile = new StopSign()?
//		}
		return true;
	}

	/**
	 * Place stop sign on tile. XX
	 */
	protected void placeStopSign() {
		itemHP = 3; // Should call Item class go get health of stop sign
		isBlocked = true;
	}

	/**
	 * Returns number of rats that can go onto this tile.
	 * 
	 * @param t the tile that is requesting the information
	 * @param n number of rats
	 * @return the number of rats that can pass through it
	 */
	public int numsRatsCanEnter(TileType t, int n) {
		if (!isBlocked) {
			return n;
		}
		itemHP -= n;
		if (itemHP > 0) {
			return 0;
		}
		Main.removeStopSign(new int[] {X_Y_POS[0] / Board.EXTRA_PADDING,
				X_Y_POS[1] / Board.EXTRA_PADDING});
		isBlocked = false;
		return Math.abs(itemHP);
	}
	
	/**
	 * Add bomb item onto Tile??.
	 */
	public void placeBomb() {
		//itemOnTile = new Bomb();
	}
	
	/**
	 * TODO -> Andrew
	 * Blow up this tile??.
	 */
	public void blowUp() {
		//Item delete
		//Rat delete, rat tell rat controller
		if (itemOnTile != null) {
//			Main.removeBomb(new int[] {X_Y_POS[0] / Board.EXTRA_PADDING,
//					X_Y_POS[1] / Board.EXTRA_PADDING});
		}
		resetTile();
		System.out.println("BLOWN UP");
	}

	/** MOVEMENT
	 * Add rat that is going to this tile.
	 * 
	 * @param r rat to be added to this Tile
	 * @param d direction the rat came from
	 */
	public void addRat(Rat r, Direction d) {
		nextBlock.putIfAbsent(d, new ArrayList<Rat>());
		nextBlock.get(d).add(r);
	}
	
	/** MOVEMENT
	 * Add death rat to this tile.
	 * 
	 * @param r death rat to be added
	 * @param d direction the death rat came from
	 */
	public void addRat(DeathRat r, Direction d) {
		nextDeath.putIfAbsent(d, new ArrayList<DeathRat>());
		nextDeath.get(d).add(r);
	}
	
	/** MOVEMENT
	 * Might join this method with above, Death Rat will need to extend Rat if so...
	 * 
	 * @param dr DeathRat to be added to this Tile
	 * @param d	direction the DeathRat came from
	 */
	public void addDeathRat(DeathRat dr, Direction d) {
		nextDeath.putIfAbsent(d, new ArrayList<DeathRat>());
		nextDeath.get(d).add(dr);
	}

	/**
	 * Sets list of rats the tile is currently dealing with
	 */
	public void setCurrRat() {
		currBlock = nextBlock;
		nextBlock = new HashMap<>();
		
		currDeath = nextDeath;
		nextDeath = new HashMap<>();
	}
	
	/**
	 * Empties tile of all attributes/ things on tile.
	 */
	private void resetTile() {
		itemOnTile = null;
		itemHP = 0;
		itemDamageType = null;
		isBlocked = false;
		nextBlock =  new HashMap<>();	
	}
	
}
