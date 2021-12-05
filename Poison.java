import java.util.ArrayList;

/**
 * @author Andrew
 */
class Poison extends Item {
	
    public Poison() {
    	hp = 1;
    }

	@Override
	public ArrayList<Rat> itemAction(ArrayList<Rat> r) {
		Rat main = r.get(0);
		RatController.killRat(main);
		r.remove(0);
		hp--;
		return r;
	}
}
