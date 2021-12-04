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
	 * Error message if file is not found.
	 */
	private final static String ERR_MSG = "Images are missing, please redownload file, make sure you save a copy of your save files!";
	
	/**
	 * Places where Rat images are found. TODO Move to Main and have all files stored there.
	 */
	private final static String IMAGE_LOC = "./img/";
	
	/**
	 * Extension of image files.
	 */
	private final static String FILE_TYPE = ".png";
	
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
	private RatType(int size, int speed, String imagePic) {
		this.size = size;
		this.speed = speed;
		try {
			for(int i = 0; i < 4; i++) {
				image[i] = new Image(IMAGE_LOC + imagePic + i + FILE_TYPE);
			}
		} catch (NullPointerException e) {
			System.err.println(ERR_MSG);
			System.out.println("AA");
			System.exit(0);
		}
	}
}
