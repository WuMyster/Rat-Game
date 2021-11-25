import java.util.ArrayList;

public class LightTile extends TileType {

	public LightTile(int x, int y) {
		super(new int[] {x, y});
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getNextDirection() {
		for (Direction prevDirection : currBlock.keySet()) {
			ArrayList<Rat> ratList = currBlock.get(prevDirection);
			
			if (!ratList.isEmpty()) { 
				System.out.println("Invisible");
				Direction goTo = directions[0] == prevDirection ? directions[1] : directions[0];
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
		//Is this really needed?
		//System.out.println("ERROR: LightTile accelerated direction has been sparked");
		/*
		Direction goTo = directions[0] == prevDirection ? directions[1] : directions[0];
		int i = 0;
		while (i != 1) {	
			TileType a = neighbourTiles.get(goTo);
			i = a.damageStopSign(1);
			Direction tmp = goTo;
			goTo = prevDirection;
			prevDirection = tmp;
		}
		
		TileType tile = neighbourTiles.get(goTo);
		Output.addCurrMovement(X_Y_POS, true, goTo);
		tile.addRat(r, goTo.opposite());
		*/
		//Direction goTo = directions[0] == prevDirection ? directions[1] : directions[0];
		TileType tile = neighbourTiles.get(prevDirection.opposite());
		tile.getAcceleratedDirection(r, prevDirection.opposite());
		System.out.println("Light: " + X_Y_POS[0] + " " + X_Y_POS[1]);
	}

}
