import java.util.ArrayList;

/**
 * 
 * @author 
 *
 */
abstract class Item {
	
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
	
	public abstract ArrayList<Rat> itemAction(ArrayList<Rat> r);
}
