/**
 * 
 * @author 
 *
 */
class Item {
	/**
	 * The hp of the item.
	 */
	protected int hp;
	
	/**
	 * Returns {@code true} if item is alive.
	 * @return {@code true} if item is alive.
	 */
	public boolean isAlive() {
		return hp > 0;
	}
}
