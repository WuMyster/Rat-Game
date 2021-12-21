import java.util.ArrayList;

import javafx.scene.image.Image;

public class DeathRat {
	
	/**
	 * Name of DeathRat.
	 */
	public static final String NAME = "D";
	
	/**
	 * Icon of Death Rat.
	 */
	public static final Image IMAGE = new Image(Main.IMAGE_FILE_LOC + "ItemDeathRat.png");
	
	/**
	 * XY position on the board the death rat starts from.
	 */
	private int[] xyPos;
	
	/**
	 * Direction the Death rat is facing and moving towards.
	 */
	private Direction d;
	
	/**
	 * How far the Death Rat moves
	 */
	private int move;
	
	/**
	 * Inital hp of the Death rat
	 */
	private int hp;
	
	/**
	 * Start hp of all Death rats
	 */
	private static int START_HP = 5;
	
	/**
	 * Time it stays still on tile in tile time.
	 */
	private static int STOP_TIME = 5;
	
	/**
	 * Default distance of the Death Rat.
	 */
	private static int DEFAULT_MOVE = 4;
	
	/**
	 * Constructor for Death Rat. Does not interact with items other
	 * than Stop Signs and Bombs.
	 */
	public DeathRat() {
		this.hp = START_HP + STOP_TIME;
	}
	
	/**
	 * Constructor for Death Rat. Does not interact with items other
	 * than Stop Signs and Bombs. HP is set for this Death Rat
	 * @param hp hot points left on this Death Rat
	 */
	public DeathRat(int hp) {
		this.hp = hp;
	}
	
	/**
	 * Returns the XY position of the Death Rat on the board.
	 * @return XY position on the board
	 */
	public int[] getXyPos() {
		return xyPos;
	}
	
	/**
	 * Returns {@code true} if this death rat can move.
	 * @return true if Death Rat can move
	 */
	public boolean canMove() {
		if (hp <= START_HP) {
			return true;
		}
		hp--;
		return false;
	}
	
	public boolean isAlive() {
		return hp > 0;
	}

	/**
	 * Gets the direction it's heading.
	 * @return direction it's heading
	 */
	public Direction getD() {
		return d;
	}

	/**
	 * Move limit of the Death Rat.
	 * @return move limit
	 */
	public int getMove() {
		return move;
	}
	
	/**
	 * Initial movement parameters for rat.
	 * @param xyPos xy position it started on
	 * @param d		direction it's heading
	 * @author J
	 */
	public void initalMove(int[] xyPos, Direction d) {
		this.xyPos = xyPos;
		this.d = d;
		this.move = DEFAULT_MOVE;
	}
	
	/**
	 * Change it's move limit.
	 * @param r rat to kill
	 * @param move limit it's going forwards to
	 * @author J
	 */
	public boolean killRat(Rat r, int move) {
		hp -= 1;
		if (hp >= 0) {
			r.damageRat(20);
			return true;
		}
		this.move = move;
		return false;
	}
	
	/**
	 * Kills as many rats as it can from this list and returns a list of rats that are still alive.
	 * Currently only for rats on tiles.
	 * @param rs list of rats the DR will take on
	 * @param move limit it's going forwards to
	 * @return list of rats that survive attack
	 */
	public ArrayList<Rat> killRats(ArrayList<Rat> rs, int move) {	
		if (rs.size() == 0) { 
			return rs;
		}
		hp -= rs.size();
		if (hp >= 0) {
			for (Rat r : rs) {
				r.damageRat(20);
			}
			return new ArrayList<>();
		}
		this.move = move;
		
		rs.subList(rs.size() + hp, rs.size());
		return rs;
	}	
	
	public String toString() {
		return NAME + Main.FILE_SUB_SEPERATOR + String.valueOf(hp);
	}
}
