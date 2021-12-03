import java.util.ArrayList;

public class DeathRat {
	
	Rat rat;
	int[] xyPos;
	Direction d;
	int move;
	
	int hp = 15; //For now
	
	/**
	 * Constructor for Death Rat with basically garbage args.
	 * @author j
	 */
	public DeathRat() {
		rat = new Rat(50, true, true, 20, true, true, true);
	}
	
	/**
	 * Should be changed!
	 * @return
	 */
	public Rat itemAction() {
		Rat deathRat = new Rat(true, 10);
		
		return deathRat;
	}
	
	public int[] getXyPos() {
		return xyPos;
	}

	public Direction getD() {
		return d;
	}

	public int getMove() {
		return move;
	}
	
	public boolean isAlive() {
		// Check hp
		return hp > 0;
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
	 * @param r rat to kill
	 * @param move limit it's going forwards to
	 * @author J
	 */
	public boolean killRat(Rat r, int move) {
		hp -= 1;
		if (hp >= 0) {
			return true;
		}
		this.move = move;
		return false;
	}
	
	//Need to think of way to prevent being drawn if dead
	public ArrayList<Rat> killRats(ArrayList<Rat> r, int move) {
		if (r.size() == 0) {
			return r;
		}
		hp -= r.size();
		if (hp >= 0) {
			System.out.println();
			return new ArrayList<>();
		}
		this.move = move;
		
		r.subList(r.size() + hp, r.size());
		return r;
	}
	
	// This might not be needed anymore, is item, but should be treated like Rat
	
	
}
