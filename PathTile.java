
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A standard tile that has at most 2 tiles connected to it.
 * 
 * @author Jing Shiang Gu
 *
 */
public class PathTile extends Tile {

	/**
	 * Constructor to set its location
	 * 
	 * @param x number of tiles away from the left
	 * @param y number of tiles away from the top
	 */
	public PathTile(int x, int y) {
		super(new int[] { x, y });
	}
}
