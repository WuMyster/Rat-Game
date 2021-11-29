import javafx.scene.image.Image;

/**
 * All types of rats and default values for them.
 * @author Jing Shiang Gu
 *
 */
public enum RatType {
	DEATH (1, 2, "DeathRat"), 
	MALE (1, 1, "MaleRat"), 
	FEMALE(1, 1, "FemaleRat"), 
	BABY(2, 2, "BabyRat");
	
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
	private Image[] image = new Image[4];
	
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
	public Image[] getImage() {
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
		try {
			for(int i = 0; i < 4; i++) {
				image[i] = new Image("./img/" + imageLoc + i + ".png");
			}
		} catch (NullPointerException e) {
			System.out.println("Image not found: " + imageLoc);
			System.exit(0);
		}
	}
}
