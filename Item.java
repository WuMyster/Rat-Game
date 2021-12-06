import java.util.ArrayList;

/**
 * Superclass for items.
 * @author Andrew
 */
abstract class Item {

	/**
	 * The hp of the item.
	 */
	protected int hp;
	
	/**
	 * Creates and returns an item from given string.
	 * @param item item name of item
	 * @param hp hp remaining of item
	 * @param xyPos x y position of item
	 * @return item created from string
	 */
	public static Item toItem(String item, int hp, int[] xyPos) {
		if (item.equals(Bomb.NAME)) {
			return new Bomb(xyPos, hp);
		} else if (item.equals(Poison.NAME)) {
			return new Poison(hp);
		} else if (item.equals(SexChangeToFemale.NAME)) {
			return new SexChangeToFemale(hp);
		} else if (item.equals(SexChangeToMale.NAME)) {
			return new SexChangeToMale(hp);
		} else if (item.equals(Sterilisation.NAME)) {
			return new Sterilisation(hp);
		} else if (item.equals(StopSign.NAME)) {
			return new StopSign(xyPos, hp);
		}
		return null;
	}

	/**
	 * Returns {@code true} if item is alive.
	 * @return {@code true} if item is alive.
	 */
	public boolean isAlive() {
		return hp > 0;
	}
	
	/**
	 * Action of the Item to rats.
	 * @param r list of rats it's interacting with
	 * @return list of rats that are alive after interacting with item
	 */
	public abstract ArrayList<Rat> itemAction(ArrayList<Rat> r);
	
	/**
	 * Returns a string representation of this Item.
	 */
	public abstract String toString();
}
