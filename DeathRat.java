

public class DeathRat {
	
	Rat rat;
	
	/**
	 * Constructor for Death Rat
	 * @author j
	 */
	public DeathRat() {
		rat = new Rat(50, true, true, 20, true, true, true);
	}
	
	public Rat itemAction() {
		Rat deathRat = new Rat(true, 10);
		deathRat.becomeDeathRat();
		return deathRat;
	}
	
}
