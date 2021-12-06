/**
 * This class models a single rat.
 * @author Ollie Jarrett
 * @version
 */
public class Rat {
	private boolean isMale;
	private boolean isPregnant;
	private boolean isDeathRat;
	private boolean isSterile;
	private boolean isBreeding;
	private int age;
	private int health;
	private int pregnancyCounter;
	
	
	/**
	 * Constructor for new rats.
	 * Should be used at the start of the level or for newly born rats.
	 * @param isMale the rats sex.
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
	 * Constructor for existing rats.
	 * Used when loading pre-existing rats from a save file.
	 * @param age - how old the rat is.
	 * @param isMale - the rats sex - True = male, False = female.
	 * @param isPregnant - whether the rat is currently pregnant.
	 * @param hp - the rats health.
	 * @param isSterile - whether the rat is sterile - True = sterile, False = not sterile.
	 * @param isBreeding - whether the rat is currently bleeding.
	 * @param isDeathRat - whether the rat is a death rat.
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
	 * Gets the rat type.
	 * @return rat type.
	 * @author J.
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
	 * Gets whether the rat is male.
	 * @return A boolean value - true = male.
	 */
	public boolean getIsMale() {
		return isMale;
	}
	
	/**
	 * Sets the rat sex to a new boolean value newIsMale.
	 * @param newIsMale - whether the rat should be male after the interaction.
	 */
	public void setIsMale(boolean newIsMale) {
		isMale = newIsMale;
	}
	
	/**
	 * Gets the value of the rats pregnancy counter.
	 * @return an integer of the rats pregnancy counter.
	 */
	public int getPregCounter() {
		return pregnancyCounter;
	}
	
	/**
	 * Decrements the rats pregnancy counter and terminates the pregnancy when the counter depletes.
	 */
	public void decrementPregCounter() {
		pregnancyCounter -= 1;
		if(pregnancyCounter == 0) {
			isPregnant = false;
		}
	}
	
	/**
	 * Set the rat pregnancy to a new boolean value.
	 * @param newPregnancyState - true = pregnant, false = not pregnant.
	 */
	public void setPregnancy(boolean newPregnancyState) {
		isPregnant = newPregnancyState;
		if(isPregnant == true) {
			pregnancyCounter = 7;
		}
	}
	
	/**
	 * Gets whether the rat is pregnant or not.
	 * @return isPregnant - true = pregnant.
	 */
	public boolean getPregnant() {
		return isPregnant;
	}
	
	/**
	 * Gets whether the rat is a death rat.
	 * @return isDeathRat - true = the rat is a death rat.
	 */
	public boolean getDeathRat() {
		return isDeathRat;
	}
	
	/**
	 * Sterilises the rat.
	 * Adult rats will become sterile and can no longer breed.
	 * Child rats will become sterile adults afterwards.
	 */
	public void sterilise() {
		isSterile = true;
		if(age<30) {
			age = 30;
		}
	}
	
	/**
	 * Gets whether the rat is sterile.
	 * @return A boolean value - true = sterile.
	 */
	public boolean getSterile() {
		return isSterile;
	}
	
	/**
	 * Sets whether the rat is currently breeding.
	 * @param breedStatus - A boolean value where true = breeding.
	 */
	public void setBreedStatus(boolean breedStatus) {
		isBreeding = breedStatus;
	}
	
	/**
	 * Gets whether the rat is currently breeding.
	 * @return A boolean of whether the rat is breeding.
	 */
	public boolean getIsBreeding() {
		return isBreeding;
	}
	
	/**
	 * Increments the rat age, making it older.
	 */
	public void incrementAge() {
		age++;
	}
	
	/**
	 * Gets how old the rat is.
	 * @return An integer containing the rats age.
	 */
	public int getAge() {
		return age;
	}
	
	/**
	 * Gets whether the rat is a child.
	 * @return A boolean where true = the rat is a child.
	 */
	public boolean isChild() {
		return age < 30;
	}
	
	/**
	 * Damages the rat and kills it if its health is depleted.
	 * @param damage - amount of damage dealt to the rat.
	 */
	public void damageRat(int damage) {
		health -= damage;
		if(health <= 0) {
			RatController.killRat(this);
		}
	}
	
	/**
	 * Gets the rats health value.
	 * @return the rats health.
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * Gets how many points the rat is worth.
	 * @return An integer containing the point value of the rat.
	 */
	public int getPointsUponDeath() {
		return calculatePointsUponDeath();
	}
	
	/**
	 * Translates the rat into a string which can be easily stored.
	 */
	public String toString() {
		String output = "";
		output += age + ",";
		output += isMale + ",";
		output += isPregnant + ",";
		output += health + ",";
		output += isSterile + ",";
		output += isBreeding + ",";
		output += isDeathRat;
		return output;
	}
	
	/**
	 * Calculates how many points the rat is worth.
	 * @return An integer value for the rats worth.
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
