
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
	 * Constructor for new rats - used at the start of the level or for newly born rats
	 * @param isMale the rats sex
	 * @param health the rats health
	 */
	public Rat(boolean isMale) {
		this.isMale = isMale;
		this.health = 5;
		isBreeding = false;
		isPregnant = false;
		isDeathRat = false;
		isSterile = false;
		age = 0;
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
	/**
	 * Changes the rat to be pregnant or not pregnant
	 * @param newPregnancyState - true = pregnant, false = not pregnant
	 */
	public void setPregnancy(boolean newPregnancyState) {
		isPregnant = newPregnancyState;
	}
	/**
	 * tells other classes whether the rat is pregnant or not
	 * @return isPregnant - true = pregnant
	 */
	public boolean getPregnant() {
		return isPregnant;
	}
	/**
	 * Tells other classes whether the rat is a death rat
	 * @return isDeathRat - true = the rat is a death rat
	 */
	public boolean getDeathRat() {
		return isDeathRat;
	}
	/**
	 * Sets isSterile to true - the rat can no longer breed
	 */
	public void sterilise() {
		isSterile = true;
	}
	/**
	 * Tells other classes whether the rat is sterile and cannot breed
	 * @return A boolean for if the rat is sterile
	 */
	public boolean getSterile() {
		return isSterile;
	}
	/**
	 * Changes whether the rat is currently breeding
	 * @param breedStatus - true = breeding
	 */
	public void setBreedStatus(boolean breedStatus) {
		isBreeding = breedStatus;
	}
	/**
	 * Tells other classes whether the rat is currently breeding
	 * @return Boolean value for if the rat is breeding
	 */
	public boolean getIsBreeding() {
		return isBreeding;
	}
	/**
	 * Makes the rat older
	 */
	public void incrementAge() {
		age++;
	}
	/**
	 * Tells other classes how old the rat is
	 * @return An integer containing the rats age
	 */
	public int getAge() {
		return age;
	}
	/**
	 * Tells other classes whether the rat is a child
	 * @return A boolean where true = the rat is a child
	 */
	public boolean isChild() {
		if(age<30) {
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
	public void damageRat(int damage) {
		health -= damage;
		if(health <= 0) {
			RatController.killRat(this);
		}
	}
	/**
	 * Gets the rats health value
	 * @return the rats health
	 */
	public int getHealth() {
		return health;
	}
	/**
	 * Tells other classes how many points the rat is worth
	 * @return An integer containing the point value of the rat
	 */
	public int getPointsUponDeath() {
		return calculatePointsUponDeath();
	}
	/**
	 * Calculates how many points the rat is worth
	 * @return An integer value for the rats worth
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
	/**
	 * Translates the rat into a string which can be easily stored
	 */
	public String toString() {
		String output = "";
		output += age.toString() + ",";
		output += isMale.toString() + ",";
		output += isPregnant.toString() + ",";
		output += health.toString() + ",";
		output += isSterile.toString() + ",";
		output += isBreeding.toString() + ",";
		output += isDeathRat.toString();
		return output;
	}
	
}
