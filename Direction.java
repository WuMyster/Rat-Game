
/**
 * Direction which rat can travel??
 * @author 2010573
 *
 */
public enum Direction {
	NORTH(1), 
	SOUTH(0), 
	EAST(3), 
	WEST(2);
	
	private final int oppo;

	Direction(int oppo) {
		this.oppo = oppo;
	}
	
	/**
	 * Returns the opposite direction of current direction
	 * @return
	 */
	Direction opposite() {
		return Direction.values() [this.oppo];
	}
}
