
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
        this.hp = 1;
    }
    
    /**
	 * Constructor for Sterilisation object setting hp of item.
	 * 
	 * @param hp	hp of item
	 */
    public Sterilisation(int hp) {
    	this.hp = hp;
    }
	
	@Override
	public String toString() {
		String out = NAME + Main.FILE_SUB_SEPERATOR + 1;
		return out;
	}
}
