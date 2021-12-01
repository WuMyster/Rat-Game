

public class DeathRat {
	
	Rat rat;
	int[] xyPos;
	Direction d;
	int move;
	
	/**
	 * Constructor for Death Rat with basically garbage args.
	 * @author j
	 */
	public DeathRat() {
		rat = new Rat(50, true, true, 20, true, true, true);
	}
	
	/**
	 * Initial movement parameters for rat.
	 * @param xyPos xy position it started on
	 * @param d		direction it's heading
	 * @author J
	 */
	public void initalMove(int[] xyPos, Direction d) {
		this.xyPos = xyPos;
		this.d = d;
		this.move = 4; // TODO chang to constant
	}
	
	/**
	 * Change it's move limit.
	 * @param move limit it's going forwards to
	 * @author J
	 */
	public void changeMove(int move) {
		this.move = move;
	}
	
	public Rat itemAction() {
		Rat deathRat = new Rat(true, 10);
		deathRat.becomeDeathRat();
		return deathRat;
	}
	
}
