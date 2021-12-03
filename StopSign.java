
public class StopSign extends Item {
	
	private static final int START_HP = 10;
	
	public StopSign() {
		this.hp = START_HP;
	}
	
	/**
	 * Number of rats trying to access this tile against the stop sign.
	 * @param n number of rats trying to get to this tile
	 * @return number of rats that are able to get into this tile
	 */
	public int numsRatsCanEnter(int n) {
		hp -= n;
		if (hp > 0) {
			return 0;
		}
		return Math.abs(hp);
	}
}
