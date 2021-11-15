import java.util.Random;

/**
 * 
 * @author 2010573
 *
 */
public class JunctionTile extends Tile {
	
	private Random rand = new Random();

	/**
	 * Will choose a random direction for each Rat to go to.
	 */
	public void getNextDirection() {
		// TODO Auto-generated method stub
		System.out.println(rand.nextInt(3));
	}

}
