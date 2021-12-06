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
	 * Returns {@code true} if item is alive.
	 * @return {@code true} if item is alive.
	 */
	public boolean isAlive() {
		return hp > 0;
	}
	
	public abstract ArrayList<Rat> itemAction(ArrayList<Rat> r);
	
	public abstract String toString();
	
	public static Item toItem(String item, int hp, int[] xyPos) {
		if (item.equals(Bomb.name)) {
			return new Bomb(xyPos, hp);
		} else if (item.equals(Poison.name)) {
			return new Poison(hp);
		} else if (item.equals(SexChangeToFemale.name)) {
			return new SexChangeToFemale(hp);
		} else if (item.equals(SexChangeToMale.name)) {
			return new SexChangeToMale(hp);
		} else if (item.equals(Sterilisation.name)) {
			return new Sterilisation(hp);
		} else if (item.equals(StopSign.name)) {
			return new StopSign(xyPos, hp);
		}
		return null;
	}
}
