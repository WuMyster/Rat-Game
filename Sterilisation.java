import java.util.ArrayList;

import javafx.scene.image.Image;

/**
 * Class modelling a Sterilisation item.
 * @author Andrew Wu
 */
public class Sterilisation extends Item {

	/**
	 * Name of the item.
	 */
	public static final String NAME = "Steralise";
	
	/**
	 * Icon of this image.
	 */
	public static final Image IMAGE = new Image(Main.IMAGE_FILE_LOC + "/Sterilise.png");
	
    /**
     * Health point of item.
     */
    public Sterilisation() {
        hp = 1;
    }
    
    /**
	 * Constructor for Sterilisation object setting hp of item.
	 * 
	 * @param hp	hp of item
	 */
    public Sterilisation(int hp) {
    	this.hp = hp;
    }

    /**
     * Sterilises rat so they can no longer get pregnant and give birth.
     * @param r a rat Object.
     * @return ArrayList of rats on the tile this item was invoked on.
     */
	@Override
	public ArrayList<Rat> itemAction(ArrayList<Rat> r) {
		r.get(0).sterilise();
        hp--;
		return r;
	}
	
	@Override
	public String toString() {
		String out = NAME + Main.FILE_SUB_SEPERATOR + hp;
		return out;
	}
}
