import java.util.ArrayList;

/**
@author Salim, Andrew
 */
public class SexChangeToFemale extends Item {
	
	public SexChangeToFemale() {
		hp = 1;
	}
	
    /**
     * if rat is not male and thus female, then make rat male. Otherwise keep male. Subtract one from item count
     * @param a a Rat object.
     */
	@Override
	public ArrayList<Rat> itemAction(ArrayList<Rat> r) {
		r.get(0).setIsMale(false);
		hp--;
		return r;
	}
}
