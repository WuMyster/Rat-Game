import java.util.ArrayList;

/**
 * Models a poison item.
 * @author Andrew Wu
 */
class Poison extends Item {

    /**
     * Health point of item.
     */
    public Poison() {
    	hp = 1;
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
}
