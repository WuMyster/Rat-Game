import java.util.ArrayList;

/**
 * @author Salim, Andrew
 * */

public class SexChangeToMale extends Item {
	
	public SexChangeToMale() {
		hp = 1;
	}

	@Override
	public ArrayList<Rat> itemAction(ArrayList<Rat> r) {
		r.get(0).setIsMale(true);
		hp--;
		return r;
	}
}
