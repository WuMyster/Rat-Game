import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class provides a skeltal implementation of the {@code Tile}, it is a
 * superclass of all {@code Tile}s.
 * 
 * @author Jing Shiang Gu
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
	protected int itemHP;

	/**
	 * Stop sign will cause the tile to not be accessed. If false, Rat can enter
	 * tile, else Rat will have to take another direction.
	 */
	protected Boolean isBlocked;

	/**
	 * Sterile Item will cause this tile to not allow any rats to have sex.
	 */
	protected Boolean isSterile;

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

	// @Jing, added this to clean up code below.
	protected final int[] ORIGINAL_X_Y_POS;

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

	// Will be run 2nd, after items deal with all rats aside from death rat.
	/**
	 * Moves Death Rat allowing it to take down normal rats along the way.
	 * 
	 * @return returns list of alive Death Rats on this tile
	 */
	public abstract ArrayList<DeathRat> getNextDeathRat();

	// For now only deals with one moving death rat between 3 tiletypes.
	/**
	 * Runs if Death Rat is coming to this tile, accelerates all processes. I.e.
	 * item, rat interactions and movement.
	 * 
	 * @param dr            Death Rat class coming to this tile
	 * @param prevDirection direction the Death Rat came from
	 */
	public abstract void moveDeathRat(DeathRat dr, Direction prevDirection);

	/**
	 * Constructor for most normal tiles.
	 * 
	 * @param xyPos the position of the Tile
	 */
	public Tile(int[] xyPos) {
		this.X_Y_POS = xyPos;
		this.ORIGINAL_X_Y_POS = new int[] { X_Y_POS[0] / Board.getExtraPadding(),
				X_Y_POS[1] / Board.getExtraPadding() };
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
	 * Give rat[s] items on the tile
	 */
	protected void giveRatItem() {
		// Create list of rats
		aliveRats = new ArrayList<>();
		for (Direction dir : currBlock.keySet()) {
			aliveRats.addAll(currBlock.get(dir));
		}
		if (itemOnTile != null) {
            if (!aliveRats.isEmpty()) {
                aliveRats = itemOnTile.itemAction(aliveRats);
                if (!itemOnTile.isAlive()) {
                    Main.removeItem(itemOnTile, ORIGINAL_X_Y_POS);
                    itemOnTile = null;
                }
			}
		}

//        // TODO Wu Find out a way to reduce repetition here
		// TODO for Andrew - reduced.
//        if (itemOnTile instanceof Poison) {
//            ((Poison) itemOnTile).itemAction(r);
//            removeItem();
//            return true;
//        }
//        if (itemOnTile instanceof SexChangeToFemale) {
//            ((SexChangeToFemale) itemOnTile).itemAction(r);
//            removeItem();
//            return false;
//        }
//        if (itemOnTile instanceof SexChangeToMale) {
//            ((SexChangeToMale) itemOnTile).itemAction(r);
//            removeItem();
//            return false;
//        }
//        if (itemOnTile instanceof Sterilisation) {
//            ((Sterilisation) itemOnTile).itemAction(r);
//            removeItem();
//            return false;
//        }
//        // TODO itemused not changed over to main yet
//        if (itemOnTile instanceof Gas) {
//            ((Gas) itemOnTile).itemAction(r);
//            //itemUsed(ORIGINAL_X_Y_POS);
//            //itemOnTile = null;
//            //return true;
//        }
	}

	private void removeItem() {
		Main.removeItem(itemOnTile, ORIGINAL_X_Y_POS);
		itemOnTile = null;
	}

	// ??????? TODO }Item{
	protected boolean setTileItem(Item i, int x, int y) {
		/*
		 * if (i instanceof Item) { //StopSign //itemHP = StopSign. HEALTH isBlocked =
		 * true; //itemOnTile = new StopSign()? }
		 */
		if (itemOnTile != null) {
			return false;
		}
		itemOnTile = i;
		
		if (i instanceof Bomb) {
			((Bomb) i).itemAction(x, y);
		} else if (i instanceof Gas) {
			((Gas) i).add(x, y);
		} // else if (i instanceof StopSign) {
//			isBlocked = true;
//		}
			

		return true;

//		if (i instanceof Poison) {
//			itemHP = ((Poison) i).getItemHP();
//			return true;
//		}
//		if (i instanceof SexChangeToFemale) {
//			itemHP = 1;
//			return true;
//		}
//		if (i instanceof SexChangeToMale) {
//			itemHP = 1;
//			return true;
//		}
//		if (i instanceof Sterilisation) {
//			itemHP = 1;
//			return true;
//		}
		
		}
//        if (i instanceof secondGas) {
//            itemHP = 1;
//            return true;
//        }
		

	/**
	 * Have the rats on this tile interact with each other.
	 */
	public void getRatInteractions() {
		// Del // WILL BE MOVED TO ITEMS
		bufferNextBlock = new HashMap<>();
	}

	/**
	 * Makes sure the list the tile is currently dealing with don't involve rats
	 * that are not moving.
	 */
	public void correctList() {
		ArrayList<Rat> tmp1 = aliveRats;
		for (Direction prevDirection : currBlock.keySet()) {
			ArrayList<Rat> tmp = new ArrayList<>();
			ArrayList<Rat> rs = currBlock.get(prevDirection);
			if (rs != null) {
				for (Rat r : rs) {
					if (aliveRats.remove(r)) {
						tmp.add(r);
					} else if (r.getAge() < 2) {
						tmp.add(r);
						System.out.println("New");
					} else {
						System.out.println("Del" + r);

					}
				}
				currBlock.put(prevDirection, tmp);
			}
		}
		aliveRats = tmp1;
	}

	/**
	 * Place stop sign on tile.
	 */

	protected boolean placeStopSign() {
		if (itemOnTile != null) {
			return false;
		}
		itemOnTile = new StopSign(X_Y_POS);
		isBlocked = true;
		return true;
	}

	/**
	 * Returns number of rats that can go onto this tile.
	 * 
	 * @param t the tile that is requesting the information
	 * @param n number of rats
	 * @return the number of rats that can pass through it
	 */
	public int numsRatsCanEnter(Tile t, int n) {
		if (!isBlocked) {
			return n;
		}
		if (itemOnTile == null) {
			System.err.println("No item on tile!!");
			return n;
		}
		int out = ((StopSign) itemOnTile).numsRatsCanEnter(n);
		if (!itemOnTile.isAlive()) {
			Main.removeStopSign(new int[] { X_Y_POS[0] / Board.EXTRA_PADDING, X_Y_POS[1] / Board.EXTRA_PADDING });
			itemOnTile = null;
			isBlocked = false;
		}
		return out;
	}

	/**
	 * Add bomb item onto Tile??.
	 * 
	 * @deprecated
	 */
	public void placeBomb() {
		// itemOnTile = new Bomb();
	}


	public void blowUp() {
		if (itemOnTile != null) {
			if (itemOnTile instanceof Bomb) {
				((Bomb) itemOnTile).timer.cancel();
			}
			Main.removeItem(itemOnTile, ORIGINAL_X_Y_POS);
		}

        // Create list of rats
        aliveRats = new ArrayList<>();
        for (Direction dir : currBlock.keySet()) {
            aliveRats.addAll(currBlock.get(dir));
        }

        if (!aliveRats.isEmpty()) {
            for (int i = 0; i < aliveRats.size(); i++) {
                RatController.killRat(aliveRats.get(i));
            }
        }
		resetTile();
		System.out.println("BLOWN UP");
	}

	/**
	 * MOVEMENT Add rat that is going to this tile.
	 * 
	 * @param r rat to be added to this Tile
	 * @param d direction the rat came from
	 */
	public void addRat(Rat r, Direction d) {
		nextBlock.putIfAbsent(d, new ArrayList<Rat>());
		nextBlock.get(d).add(r);
	}

	/**
	 * MOVEMENT Add death rat to this tile.
	 * 
	 * @param r death rat to be added
	 * @param d direction the death rat came from
	 */
	public void addRat(DeathRat r, Direction d) {
		nextDeath.putIfAbsent(d, new ArrayList<DeathRat>());
		nextDeath.get(d).add(r);
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
		if (itemOnTile != null) {
			Main.removeItem(itemOnTile, ORIGINAL_X_Y_POS);
		}
		itemOnTile = null;
		isBlocked = false;
		isSterile = false;
		// TODO Remove item from tile.
		itemHP = 0;
		isBlocked = false;
		nextBlock = new HashMap<>();
		currBlock = new HashMap<>();
		nextDeath = new HashMap<>();
		currDeath = new HashMap<>();
	}

}
