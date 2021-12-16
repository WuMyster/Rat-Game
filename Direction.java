
/**
 * Direction the rat can travel
 * @author Jing Shiang Gu
 *
 */
public enum Direction {
	/**
	 * Direction and number associated  for North
	 */
	NORTH(1), 
	/**
	 * Direction and number associated  for South
	 */
	SOUTH(0), 
	/**
	 * Direction and number associated  for East
	 */
	EAST(3), 
	/**
	 * Direction and number associated for West
	 */
	WEST(2);
	
	/**
	 * Number that corresponds to the opposing direction
	 */
	private final int oppo;

	/**
	 * Sets value for each direction.
	 * @param oppo number the direction is
	 */
	Direction(int oppo) {
		this.oppo = oppo;
	}
	
	/**
	 * Returns the opposite direction of current direction
	 * @return opposite direction
	 */
	public Direction opposite() {
		return Direction.values() [this.oppo];
	}
	
	/**
	 * Converts a number to a direction.
	 * @param i the number that corresponds to its direction
	 * @return the direction that is its number
	 */
	public static Direction toD(int i) {
		System.out.print(Direction.values() [i]);
		return Direction.values() [i];
	}
	
	/**
	 * Converts a direction to a number.
	 * @return the number pointing to this direction
	 */
	public int toInt() {
		return toInt(Direction.values() [this.oppo]);
	}
	
	/**
	 * Converts direction into the number position it is.
	 * @param d direction
	 * @return number the direction is designated to
	 */
	private int toInt(Direction d) {
		return d.oppo;
	}
}
