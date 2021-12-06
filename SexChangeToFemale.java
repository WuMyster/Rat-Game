import java.util.ArrayList;

/**
 * Class modelling a sex change item that turns males to females.
 * @author Salim, Andrew Wu
 */
public class SexChangeToFemale extends Item {

	public static String name = "toFemale";
	
    /**
     * Health point of item.
     */
	public SexChangeToFemale() {
		hp = 1;
	}
	
	public SexChangeToFemale(int hp) {
		this.hp = hp;
	}
	
    /**
     * Sets the gender of a rat to male regardless of it's starting gender.
     * Removes 1 from hp when used.
     * @param r a Rat object.
     * @return ArrayList of alive rats on the tile this item was invoked on.
     */
	@Override
	public ArrayList<Rat> itemAction(ArrayList<Rat> r) {
		r.get(0).setIsMale(false);
		hp--;
		return r;
	}
	
	@Override
	public String toString() {
		String out = name + "," + hp;
		return out;
	}
}
