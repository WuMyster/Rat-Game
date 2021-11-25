
import java.util.ArrayList;

/**
 * Tile to go in between main, visible tiles, this is to allow interaction 
 * between rats (and death rats) outside of normal tiles.
 * Will only need to deal with some items: death rat(if item), bomb.
 * @author 2010573
 *
 */
public class LightTile extends TileType {

	public LightTile(int x, int y) {
		super(new int[] {x, y});
	}

	@Override
	public void getNextDirection() {
		for (Direction prevDirection : currBlock.keySet()) {
			ArrayList<Rat> ratList = currBlock.get(prevDirection);
			
			if (!ratList.isEmpty()) { 
				//System.out.println("Invisible");
				Direction goTo = prevDirection.opposite(); //This can be opposite due to nature of tile
				TileType tile = neighbourTiles.get(goTo);
				for (int i = 0; i < ratList.size(); i++) {
					Output.addCurrMovement(X_Y_POS, false, goTo);
					tile.addRat(ratList.get(i), prevDirection);
				}
			}
		}
	}
	
	@Override
	public void getAcceleratedDirection(Rat r, Direction prevDirection) {
		TileType tile = neighbourTiles.get(prevDirection.opposite());
		tile.getAcceleratedDirection(r, prevDirection.opposite());
		//System.out.println("Light: " + X_Y_POS[0] + " " + X_Y_POS[1]);
	}
}
