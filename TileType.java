import java.util.ArrayList;
import java.util.Arrays;
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
	 * @param r Baby Rat that is skipping past staying on Light Tile
	 * @param prevDirection direction that Baby Rat from
	 */
	public abstract void getAcceleratedDirection(Rat r, Direction prevDirection);
	
	// Will be run 2nd, after items deal with all rats aside from death rat.
	/**
	 * Moves Death Rat allowing it to take down normal rats along the way.
	 * @return returns list of alive Death Rats on this tile
	 */
	public abstract ArrayList<DeathRat> getNextDeathRat();
	
	// For now only deals with one moving death rat between 3 tiletypes.
	/**
	 * Runs if Death Rat is coming to this tile, accelerates all processes. I.e.
	 * item, rat interactions and movement.
	 * @param dr Death Rat class coming to this tile
	 * @param prevDirection direction the Death Rat came from
	 */
	public abstract void moveDeathRat(DeathRat dr, Direction prevDirection);

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

        // TODO Wu Maybe switch will be better
        if (itemOnTile instanceof Poison) {
            ((Poison) itemOnTile).itemAction(r);
            itemUsed(new int[] {X_Y_POS[0] / Board.EXTRA_PADDING,
                    X_Y_POS[1] / Board.EXTRA_PADDING});
            itemOnTile = null;
            return true;
        }
        if (itemOnTile instanceof SexChangeToFemale) {
            ((SexChangeToFemale) itemOnTile).itemAction(r);
            itemUsed(new int[] {X_Y_POS[0] / Board.EXTRA_PADDING,
                    X_Y_POS[1] / Board.EXTRA_PADDING});
            itemOnTile = null;
            return false;
        }
        if (itemOnTile instanceof SexChangeToMale) {
            ((SexChangeToMale) itemOnTile).itemAction(r);
            itemUsed(new int[] {X_Y_POS[0] / Board.EXTRA_PADDING,
                    X_Y_POS[1] / Board.EXTRA_PADDING});
            itemOnTile = null;
            return false;
        }
        if (itemOnTile instanceof Sterilisation) {
            ((Sterilisation) itemOnTile).itemAction(r);
            itemUsed(new int[] {X_Y_POS[0] / Board.EXTRA_PADDING,
                    X_Y_POS[1] / Board.EXTRA_PADDING});
            itemOnTile = null;
            return false;
        }

		
		//Method to give item away
		
		return false;
	}
	
	//??????? TODO }Item{
	protected boolean setTileItem(Item i, int x, int y) {
        /*
		if (i instanceof Item) { //StopSign
			//itemHP = StopSign. HEALTH
			isBlocked = true;
			//itemOnTile = new StopSign()?
	    }
         */
        itemOnTile = i;

        if (i instanceof Poison) {
            itemHP = ((Poison) i).getItemHP();
            return true;
        }
        if (i instanceof SexChangeToFemale) {
            itemHP = 1;
            return true;
        }
        if (i instanceof SexChangeToMale) {
            itemHP = 1;
            return true;
        }
        if (i instanceof Sterilisation) {
            itemHP = 1;
            return true;
        }
        if (i instanceof Bomb) {
            itemHP = 1;
            ((Bomb) i).itemAction(x, y);
            return true;
        }

		return true;
	}

    /**
     * When item is given to rat and used, item is removed from its Arraylist and subsequently
     * removed from screen.
     * @param pos
     */
    private void itemUsed(int[] pos) {
        ArrayList<int[]> arr = null;
        
        if (itemOnTile instanceof Poison) {
            arr = Main.getPoisonPlace();
        }
        if (itemOnTile instanceof SexChangeToFemale) {
            arr = Main.getSexToFemalePlace();
        }
        if (itemOnTile instanceof SexChangeToMale) {
            arr = Main.getSexToMalePlace();
        }
        if (itemOnTile instanceof Sterilisation) {
            arr = Main.getSterilisePlace();
        }

        if (itemOnTile instanceof Bomb) {
            arr = Main.getBombPlace();
        }

        int[] a = null;
        for (int[] i : arr) {
            if (Arrays.equals(i, pos)) {
                a = i;
            }
        }
        arr.remove(a);
    }

	/**
	 * Place stop sign on tile. XX
	 */
	protected void placeStopSign() {
		itemHP = 30; // Should call Item class go get health of stop sign
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
            if (itemOnTile instanceof Bomb) {
                ((Bomb) itemOnTile).timer.cancel();
            }
            itemUsed(new int[] {X_Y_POS[0] / Board.EXTRA_PADDING,
                    X_Y_POS[1] / Board.EXTRA_PADDING});
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
	 * @deprecated
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
