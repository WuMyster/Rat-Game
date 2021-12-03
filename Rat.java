
/**
 * Models a single rat
 * @author Ollie Jarrett
 *
 */
public class Rat {
	/**
	 * The rats sex
	 */
	private boolean isMale;
	/**
	 * Whether the rat is pregnant
	 */
	private boolean isPregnant;
	/**
	 * Whether the rat is a death rat
	 */
	private boolean isDeathRat;
	/**
	 * Whether the rat can breed
	 */
	private boolean isSterile;
	/**
	 * Whether the rat is currently breeding - breeding rats don't move or interact with other rats
	 */
	private boolean isBreeding;
	
	/**
	 * The age of the rat in ticks
	 */
	private int age;
	/**
	 * The amount of health the rat has
	 */
	private int health;
	/**
	 * The amount of points rewarded when the rat is killed
	 */
	private int pointsUponDeath;
	

	
	/**
	 * Constructor for new rats - used at the start of the level or for newly born rats
	 * @param isMale the rats sex
	 * @param health the rats health
	 */
	public Rat(boolean isMale, int health) {
		this.isMale = isMale;
		this.health = health;
		isBreeding = false;
		isPregnant = false;
		isDeathRat = false;
		isSterile = false;
		age = 0;
		pointsUponDeath = 10;
	}

	/**
	 * Constructor for existing rats from a save file
	 * @param age - how old the rat is
	 * @param isMale - the rats sex - True = male, False = female
	 * @param isPregnant - whether the rat is currently pregnant
	 * @param hp - the rats health
	 * @param isSterile - whether the rat is sterile - True = sterile, False = not sterile
	 * @param isBreeding - whether the rat is currently bleeding
	 * @param isDeathRat - whether the rat is a death rat
	 */
	public Rat(int age, boolean isMale, boolean isPregnant, int hp, boolean isSterile, boolean isBreeding, boolean isDeathRat) {
		this.isMale = isMale;
		this.health = hp;
		this.isBreeding = isBreeding;
		this.isPregnant = isPregnant;
		this.isDeathRat = isDeathRat;
		this.isSterile = isSterile;
		this.age = age;
		pointsUponDeath = calculatePointsUponDeath();
	}
	

	/**
	 * Returns rat type
	 * @return rat type
	 * @author J
	 */
	public RatType getStatus() {
		if (isChild()) {
			return RatType.BABY;
		} 
		if (isDeathRat) {
			return RatType.DEATH;
		}
		return isMale ? RatType.MALE : RatType.FEMALE;
	}
	
	/**
	 * returns whether the rat is male
	 * @return 
	 */
	public boolean getIsMale() {
		return isMale;
	}
	/**
	 * Changes the rat sex to a new input sex
	 * @param newIsMale - whether the rat should be male after the interaction
	 */
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
	public boolean isChild() {
		if(age<50) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Damages the rat and returns a boolean value for if the rat has died
	 * @param damage - amount of damage dealt to the rat
	 * @return a boolean on whether the rat is dead (true = dead)
	 */
	public boolean damageRat(int damage) {
		health -= damage;
		if(health == 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Gets the rats health value
	 * @return the rats health
	 */
	public int getHealth() {
		return health;
	}
	
	public int getPointsUponDeath() {
		return pointsUponDeath;
	}
	/**
	 * 
	 * @return
	 */
	private int calculatePointsUponDeath() {
		int points = 10;
		if(isBreeding) {
			points += 10;
		}
		if(isPregnant) {
			points += 10;
		}
		if(isChild()) {
			points += 10;
		}
		return points;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
