
/**
 * Direction the rat can travel
 * @author Jing Shiang Gu
 *
 */
public enum Direction {
	NORTH(1), 
	SOUTH(0), 
	EAST(3), 
	WEST(2);
	
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
	Direction opposite() {
		return Direction.values() [this.oppo];
	}
	
	int toInt() {
		return toInt(Direction.values() [this.oppo]);
	}
	
	private int toInt(Direction d) {
		return d.oppo;
	}
	
	static Direction toD(int i) {
		return Direction.values() [i];
	}
}
