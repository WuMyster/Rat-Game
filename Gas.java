import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.image.Image;

public class Gas extends TimeItem {
	
	public static final Image IMAGE = new Image("./img/Gas.png");
	
	public static final String NAME = "Gas";

	private final int x;
	private final int y;
	
    /**
     * Damage gas does to a rat.
     */
    private final int DAMAGE = 100;

    /**
     * Max amount of tiles gas spread to in each direction.
     */
    private static final int MAX_RADIUS = 3;
    
    private static final int START_HP = 15;

    private static final int GAS_EXPAND_TIME = 700; // milliseconds
	

	public Gas (int x, int y) {
		this.hp = START_HP;
		this.x = x;
		this.y = y;
	}
	
	public Gas (int[] xyPos, int hp) {
		this.hp = hp;
		this.x = xyPos[0];
		this.y = xyPos[1];
	}
	
	public Gas (int x, int y, int hp) {
		this.hp = hp;
		this.x = x;
		this.y = y;
	}
	
    public void spreadGas() {
    	Board.spreadGas(x, y, hp);    	
    }
    
    public void itemAction() {
    	this.timer = new Timer();
    	
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				hp--;
				
				if (hp % 5 == 0) { // Growing
					spreadGas();
				} else if (hp > 5) { // Not growing or shrinking
					
				} else {
					timer.cancel();
					Board.clearGas(x, y);
				}
			}
		}, 0, 1000);
	}

	@Override
	public ArrayList<Rat> itemAction(ArrayList<Rat> r) {
		ArrayList<Rat> out = new ArrayList<>();
		if (!r.isEmpty()) {
            for (Rat rat : r) {
            	if (!rat.damageRat(DAMAGE)) {
            		out.add(rat);
            	}
            }
        }
		return out;
	}
	
	@Override
	public String toString() {
		String out = NAME + "," + hp;
		return out;
	}
}
