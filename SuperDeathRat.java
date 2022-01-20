import javafx.scene.image.Image;

public class SuperDeathRat extends DeathRat {

	public static final Image IMAGE = new Image(Main.IMAGE_FILE_LOC + "ItemSuperDeathRat.png");
	
	public SuperDeathRat() {
		super();
	}
	
	public SuperDeathRat(int hp) {
		super(hp);
	}
	
	public Direction chooseDirection(int x, int y) {
		Direction d = Board.findClosestRat(y, x);
		System.out.println("Find Direction: " + d);
		
		return d;
	}
}
