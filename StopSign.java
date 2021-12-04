import javafx.scene.image.Image;

public class StopSign extends Item {
	
	private static final int START_HP = 3;
	private static final int DIVIDER = 3;
	private final int[] xyPos;
	private int currState;
	
	
	private static final Image[] STATES = new Image[] {
			new Image("/img/StopSign0.png"),
			new Image("/img/StopSign1.png"),
			new Image("/img/StopSign2.png")
	};
	
	public StopSign(int[] xyPos) {
		this.xyPos = xyPos;
		this.hp = START_HP;
		currState = START_HP / DIVIDER;
		
	}
	
	/**
	 * Number of rats trying to access this tile against the stop sign.
	 * @param n number of rats trying to get to this tile
	 * @return number of rats that are able to get into this tile
	 */
	public int numsRatsCanEnter(int n) {
		hp -= n;
		
		if (currState != hp / DIVIDER) {
			currState = hp / DIVIDER;
			System.out.println(currState);
			// Main.damageStopSign(xyPos, currState);
		}
		
		if (hp > 0) {
			return 0;
		}
		return Math.abs(hp);
	}
	
	public static Image getState(int s) {
		return STATES[s];
	}
}
