import java.util.ArrayList;

/**
 * Class modelling a Sterilisation item.
 * @author Andrew Wu
 */
public class Sterilisation extends Item {

    /**
     * Health point of item.
     */
    public Sterilisation() {
        hp = 1;
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
}
