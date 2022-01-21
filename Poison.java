import java.util.ArrayList;

import javafx.scene.image.Image;

/**
 * Models a poison item.
 * @author Andrew Wu
 */
class Poison extends Item {

	/**
	 * Name of item.
	 */
	public static final String NAME = "Poison";
	
	/**
	 * Icon image of the item.
	 */
	public static final Image IMAGE = new Image(Main.IMAGE_FILE_LOC + "Poison.png");
	
    /**
     * Create and sets health point of item.
     */
    public Poison() {
    	hp = 1;
    }
    
    /**
     * Constructs Poison object with set health.
     * 
     * @param hp	health of item.
     */
    public Poison(int hp) {
    	this.hp = hp;
    }

    /**
     * The effect of the Poison item.
     * Kills the first rat that is passed to it.
     * @param r a rat object
     * @return ArrayList with remaining alive rats.
     */
	public ArrayList<Rat> itemAction(ArrayList<Rat> r) {
		Rat main = r.get(0);
		RatController.killRat(main);
		r.remove(0);
		hp--;
		return r;
	}
	
	@Override
	public String toString() {
		String out = NAME + Main.FILE_SUB_SEPERATOR + hp;
		return out;
	}
}
