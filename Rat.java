
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
	
	//Constructs a baby rat
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

	//constructs a rat from a save file
	public Rat(int age, boolean male, int xPos, int yPos, int direction, boolean pregnant, int hp, boolean sterile, boolean breeding, boolean deathRat) {
		this.isMale = male;
		this.health = hp;
		this.xPosition = xPos;
		this.yPosition = yPos;
		this.direction = direction;
		isBreeding = breeding;
		isPregnant = pregnant;
		isDeathRat = deathRat;
		isSterile = sterile;
		this.age = age;
		pointsUponDeath = 30;
	}
	
	
	public boolean getIsMale() {
		return isMale;
	}
	public void setIsMale(boolean newIsMale) {
		isMale = newIsMale;
	}
	
	public void setPregnancy(boolean newPregnancyState) {
		isPregnant = newPregnancyState;
	}
	public boolean getPregnancy() {
		return isPregnant;
	}
	
	public void becomeDeathRat() {
		isDeathRat = true;
	}
	public boolean getDeathRat() {
		return isDeathRat;
	}
	
	public void sterilise() {
		isSterile = true;
	}
	public boolean getSterile() {
		return isSterile;
	}
	
	public void setBreedStatus(boolean breedStatus) {
		isBreeding = breedStatus;
	}
	public boolean getIsBreeding() {
		return isBreeding;
	}
	
	public void incrementAge() {
		age++;
	}	
	public int getAge() {
		return age;
	}
	
	public void damageRat(int damage) {
		health -= damage;
	}
	public int getHealth() {
		return health;
	}
	
	public void changePointsUponDeath(int pointsDifference) {
		pointsUponDeath += pointsDifference;
	}
	public int getPointsUponDeath() {
		return pointsUponDeath;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
