import javafx.scene.image.Image;

/**
 * All types of rats and default values for them.
 * @author Jing Shiang Gu
 *
 */
public enum RatType {
	DEATH (1, 2, "DeathRat.png"), 
	MALE (1, 1, "MaleRat.png"), 
	FEMALE(1, 1, "FemaleRat.png"), 
	BABY(2, 2, "BabyRat.png");
	
	/**
	 * Size multiplier of rat. Bigger is smaller
	 */
	private final int size;
	
	/**
	 * Speed multiplier of rat. Bigger is faster.
	 */
	private final int speed;
	
	/**
	 * Image of rat.
	 */
	private Image image;
	
	/**
	 * Returns size of rat. Bigger is smaller.
	 * @return size of rat
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Returns speed of rat. Bigger is faster.
	 * @return speed of rat.
	 */
	public int getSpeed() {
		return speed;
	}
	
	/**
	 * Returns image of rat.
	 * @return image of rat
	 */
	public Image getImage() {
		return image;
	}	
	
	/**
	 * Set values of each type of rat.
	 * @param size how big rat is (bigger is smaller)
	 * @param speed how fast rat can move (bigger is faster)
	 */
	private RatType(int size, int speed, String imageLoc) {
		this.size = size;
		this.speed = speed;
		this.image = new Image(imageLoc);
	}
}
