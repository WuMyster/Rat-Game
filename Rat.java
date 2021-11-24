
/**
 * 
 * @author Ollie
 *
 */
public class Rat {
	private boolean isMale;
	private boolean isPregnant;
	private boolean isDeathRat;
	private boolean isSterile;
	private boolean isBreeding;
	
	private int age;
	private int health;
	private int pointsUponDeath;
	
	private int xPosition;
	private int yPosition;
	private int direction;
	
	public Rat(boolean male, int health, int xPos, int yPos, int direction) {
		this.isMale = male;
		this.health = health;
		this.xPosition = xPos;
		this.yPosition = yPos;
		this.direction = direction;
		isBreeding = false;
		isPregnant = false;
		isDeathRat = false;
		isSterile = false;
		age = 0;
		pointsUponDeath = 30;
	}

	
	
}
