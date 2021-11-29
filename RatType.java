import javafx.scene.image.Image;

/**
 * 
 * @author Jing Shiang Gu
 *
 */
public enum RatType {
	DEATH (1, 2), 
	MALE (1, 1), 
	FEMALE(1, 1), 
	BABY(2, 2);
	
	private final int size;
	private final int speed;
	private Image art;
	
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
	
	public Image getImage() {
		return art;
	}
	
	public void setImage(Image art) {
		this.art = art;
	}
	
	
	/**
	 * Set values of each type of rat.
	 * @param size how big rat is (bigger is smaller)
	 * @param speed how fast rat can move (bigger is faster)
	 */
	private RatType(int size, int speed) {
		this.size = size;
		this.speed = speed;
	}
	
}
