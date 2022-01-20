
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Tile that has 3 or more tiles connected to it.
 * 
 * @author Jing Shiang Gu
 */
public class JunctionTile extends Tile {

	/**
	 * Random generator for direction.
	 */
	private Random rand = new Random();

	/**
	 * Creates a JunctionTile object storing its position.
	 * 
	 * @param x x position on the map
	 * @param y y position on the map
	 */
	public JunctionTile(int x, int y) {
		super(new int[] { x, y });
	}
}
