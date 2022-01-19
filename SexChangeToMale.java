import java.util.ArrayList;

import javafx.scene.image.Image;

/**
 * Class modelling a sex change item that turns females to males.
 * @author Salim, Andrew Wu
 * */

public class SexChangeToMale extends Item {

	/**
	 * Name of item.
	 */
	public static final String NAME = "toMale";
	
	/**
	 * Icon of this item.
	 */
	public static final Image IMAGE = new Image(Main.IMAGE_FILE_LOC + "SexChangeToMale.png");
	
    /**
     * Sets item health to 1.
     */
	public SexChangeToMale() {
		hp = 1;
	}
	
	/**
	 * Constructor for SexChangeToMale object setting hp of item.
	 * 
	 * @param hp	hp of item
	 */
	public SexChangeToMale(int hp ) {
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
		r.get(0).setIsMale(true);
		hp--;
		return r;
	}
	
	@Override
	public String toString() {
		String out = NAME + Main.FILE_SUB_SEPERATOR + hp;
		return out;
	}
}
