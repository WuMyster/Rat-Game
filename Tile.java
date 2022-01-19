import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * This class provides a skeletal implementation of the {@code Tile}, it is a
 * superclass of all {@code Tile}s.
 * 
 * @author Jing Shiang Gu
 * @author Andrew Wu
 */
public abstract class Tile {

	/**
	 * Type of item on tile, to be given to rat to allow them to interact
	 */
	protected Item itemOnTile;

	/**
	 * Tiles neighbouring current tile along with the direction to {@code Tile}.
	 */
	protected HashMap<Direction, Tile> neighbourTiles;

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
	 * List of rats staying on current tile. May be killed by Death rat later on.
	 * Once given all clear, will be moved to nextBlock.
	 */
	protected HashMap<Direction, ArrayList<Rat>> bufferNextBlock = new HashMap<>();

	/**
	 * Death Rats that are arriving to this tile.
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
	 * Basic XY position of the tile from the board.
	 */
	protected final int[] ORIGINAL_X_Y_POS;
	
	/**
	 * Random generator.
	 */
	protected Random rand = new Random();
	
	/**
	 * Determines if this tile needs to be blown.
	 */
	protected boolean detontate = false;

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
	 * 
	 * @param r             Baby Rat that is skipping past staying on Light Tile
	 * @param prevDirection direction that Baby Rat from
	 */
	public abstract void getAcceleratedDirection(Rat r, Direction prevDirection);

	/**
	 * Moves Death Rat allowing it to take down normal rats along the way.
	 * 
	 * @return returns list of alive Death Rats on this tile
	 */
	public abstract ArrayList<DeathRat> getNextDeathRat();

	/**
	 * Runs if Death Rat is coming to this tile, accelerates all processes. I.e.
	 * item, rat interactions and movement.
	 * 
	 * @param dr            Death Rat class coming to this tile
	 * @param prevDirection direction the Death Rat came from
	 */
	public abstract void moveDeathRat(DeathRat dr, Direction prevDirection);

	/**
	 * Constructor for tiles.
	 * 
	 * @param xyPos the position of the Tile
	 */
	public Tile(int[] xyPos) {
		this.X_Y_POS = xyPos;
		this.ORIGINAL_X_Y_POS = new int[] { X_Y_POS[0] / Board.EXTRA_PADDING,
				X_Y_POS[1] / Board.EXTRA_PADDING };
		resetTile();
	}

	/**
	 * For graph, this {@code Tile} will know about the {@code Tile} and
	 * {@code Direction} of other {@code Tile} classes.
	 * 
	 * @param tiles     list of tiles that is neighbouring this tile
	 * @param direction list of directions to neighbouring tiles
	 */
	public void setNeighbourTiles(Tile[] tiles, Direction[] direction) {
		this.limit = tiles.length;
		this.neighbourTiles = new HashMap<>(limit);
		for (int i = 0; i < tiles.length; i++) {
			this.neighbourTiles.put(direction[i], tiles[i]);
		}
		this.directions = direction;
	}

	/**
	 * Sets list of rats the tile is currently dealing with
	 */
	public void setCurrRat() {
		currBlock = nextBlock;
		nextBlock = new HashMap<>();

		currDeath = nextDeath;
		nextDeath = new HashMap<>();
		
        // Create list of rats
        aliveRats = new ArrayList<>();
        for (Direction dir : currBlock.keySet()) {
            aliveRats.addAll(currBlock.get(dir));
        }
		
		if (detontate) {
			blowUpTile();
		} else {
			giveRatItem();
			getRatInteractions();
			correctList(); // Make sure only moving rats are considered
		}
	}
	
	/**
	 * Give rat[s] items on the tile
	 */
	protected void giveRatItem() {
		if (itemOnTile != null) {
            if (!aliveRats.isEmpty()) {
                aliveRats = itemOnTile.itemAction(aliveRats);
                if (!itemOnTile.isAlive()) {
                    removeItem();
                }
			}
		}
	}
	
	/**
	 * Have the rats on this tile interact with each other.
	 */
	public void getRatInteractions() {
		bufferNextBlock = new HashMap<>();
	}

	/**
	 * Sorts the rats on this tile to ensure only alive rats are interacting.
	 */
	public void correctList() {
		ArrayList<Rat> tmp1 = aliveRats;
		aliveRats = new ArrayList<>();
		for (Direction prevDirection : currBlock.keySet()) {
			ArrayList<Rat> tmp = new ArrayList<>();
			ArrayList<Rat> rs = currBlock.get(prevDirection);
			if (rs != null) {
				for (Rat r : rs) {
					if (tmp1.remove(r)) {
						tmp.add(r);
					}
				}
				aliveRats.addAll(tmp);
				currBlock.put(prevDirection, tmp);
			} else {
				System.out.println("NULL");
			}
		}
		
		for (Rat r : tmp1) {
			Direction d = directions[rand.nextInt(2)];
			currBlock.putIfAbsent(d, new ArrayList<>());
			currBlock.get(d).add(r);
		}
	}

	/**
	 * Returns number of rats that can go onto this tile.
	 * 
	 * @param t the tile that is requesting the information
	 * @param n number of rats
	 * @return the number of rats that can pass through it
	 */
	public int numsRatsCanEnter(Tile t, int n) {
		if (!(itemOnTile instanceof StopSign)) {
			return n;
		}
		if (itemOnTile == null) {
			System.err.println("No item on tile!!");
			return n;
		}
		int out = ((StopSign) itemOnTile).numsRatsCanEnter(n);
		if (!itemOnTile.isAlive()) {
			removeItem();
		}
		return out;
	}
	
	/**
	 * Returns a list of rats and their position in string format.
	 * @return list of rats and their positions in string format
	 */
	public ArrayList<String> getRats() {
		String here = ORIGINAL_X_Y_POS[0] + Main.FILE_SUB_SEPERATOR +
				ORIGINAL_X_Y_POS[1];
		ArrayList<String> out = new ArrayList<>();
		for (Direction d : nextBlock.keySet()) {
			for (Rat r : nextBlock.get(d)) {
				String dir = d.toInt() + Main.FILE_SUB_SEPERATOR + here;
				out.add(r + Main.FILE_MAIN_SEPERATOR + dir);
			}
		}
		for (Direction d : nextDeath.keySet()) {
			for (DeathRat dr : nextDeath.get(d)) {
				String dir = d.toInt() + "," + here;
				out.add(dr + Main.FILE_MAIN_SEPERATOR + dir);
			}
		}
		return out;
	}
	
	/**
	 * Returns item and their position in string format.
	 * @return item and their position in string format
	 */
	public String getItem() {
		if (itemOnTile == null) {
			return null;
		}
		return itemOnTile.toString() + Main.FILE_MAIN_SEPERATOR + ORIGINAL_X_Y_POS[0] + 
				Main.FILE_SUB_SEPERATOR + ORIGINAL_X_Y_POS[1];
	}
	
	/**
	 * Sets tile to be blown up.
	 */
	public void blowUp() {
		detontate = true;
	}
	
	/**
	 * Removes Gas item from tile.
	 */
	public void clearGas() {
		if (itemOnTile instanceof Gas) {
			removeItem();
		}
	}

	/**
	 * Add rat that is going to this tile.
	 * 
	 * @param r rat to be added to this Tile
	 * @param d direction the rat came from
	 */
	public void addRat(Rat r, Direction d) {
		nextBlock.putIfAbsent(d, new ArrayList<Rat>());
		nextBlock.get(d).add(r);
	}

	/**
	 * Add death rat to this tile.
	 * 
	 * @param r death rat to be added
	 * @param d direction the death rat came from
	 */
	public void addRat(DeathRat r, Direction d) {
		nextDeath.putIfAbsent(d, new ArrayList<DeathRat>());
		nextDeath.get(d).add(r);
	}

	/**
	 * Sets the movement of the death rat.
	 */
	protected void setDeathRat() {
		// Ensures all Death Rats can move
		for (Direction prevDirection : currDeath.keySet()) {
			ArrayList<DeathRat> moves = new ArrayList<>();
			for (DeathRat dr : currDeath.get(prevDirection)) {
				if (dr.canMove()) {
					moves.add(dr);
				} else {
					nextDeath.putIfAbsent(prevDirection, new ArrayList<>());
					nextDeath.get(prevDirection).add(dr);
					GameGUI.addCurrMovement(X_Y_POS, prevDirection.opposite(), 
							RatType.DEATH, 0);
				}
			}
			currDeath.put(prevDirection, moves);
		}
	}

    /**
     * Sets item on tile.
     * @param i item to be placed on tile.
     * @return boolean if item is placed on tile.
     */
	protected boolean setTileItem(Item i) {
        if (itemOnTile != null) {
            return false;
        }
        itemOnTile = i;
        if (i instanceof TimeItem) {
            ((TimeItem) i).itemAction();
        }
        return true;
    }

	/**
	 * Empties tile of all attributes/ things on tile.
	 */
	private void resetTile() {
		if (itemOnTile != null) {
			removeItem();
		}
		nextBlock = new HashMap<>();
		currBlock = new HashMap<>();
		nextDeath = new HashMap<>();
		currDeath = new HashMap<>();
	}


    /**
     * Blows up a tile by:
     * - removing any existing item
     * - kills the rats present on the tile
     */
	private void blowUpTile() {
		if (itemOnTile != null) {
			removeItem();
		}
        for (int i = 0; i < aliveRats.size(); i++) {
            RatController.killRat(aliveRats.get(i));
        }
		resetTile();
	}
	
	private void removeItem() {
		if (itemOnTile instanceof TimeItem) {
			((TimeItem) itemOnTile).timer.cancel();
		}
		GameGUI.removeItem(ItemType.fromItem(itemOnTile), ORIGINAL_X_Y_POS);
		itemOnTile = null;
	}

}
