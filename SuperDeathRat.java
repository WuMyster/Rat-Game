import javafx.scene.image.Image;

/**
 * Models a Death Rat but is much smarter since it will actively go towards
 * the nearest normal rat. WIP, currently ignores StopSign.
 * 
 * @author J
 *
 */
public class SuperDeathRat extends DeathRat {

	/**
	 * Icon image of the superDeath Rat.
	 */
	public static final Image IMAGE = new Image(Main.IMAGE_FILE_LOC + "ItemSuperDeathRat.png");
	
	/**
	 * Constructor creates a SuperDeathRat object.
	 */
	public SuperDeathRat() {
		super();
	}
	
	/**
	 * Constructor creates a SuperDeathRat object with 
	 * a specified health.
	 * @param hp	health of the SuperDeathRat
	 */
	public SuperDeathRat(int hp) {
		super(hp);
	}
	
	/**
	 * Returns the direction to the nearest normal rat.
	 * 
	 * @param x	x pos of the SuperDR
	 * @param y	y pos of the SuperDR
	 * @return	direction of the nearest rat
	 */
	public Direction chooseDirection(int x, int y) {		
		return Board.findClosestRat(y, x);;
	}
}
