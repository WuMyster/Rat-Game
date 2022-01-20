/**
 * This class models a single rat.
 * 
 * @author Ollie Jarrett
 */
public class Rat {

	/**
	 * Age of when the rat becomes an adult.
	 */
	private static int ADULT_AGE = 11;

	/**
	 * Pregnancy stages of a rat giving birth.
	 */
	private static int MAX_PREGNANCY = 10;

	/**
	 * True if rat is male.
	 */
	private boolean isMale;

	/**
	 * True if rat cannot breed anymore.
	 */
	private boolean isSterile;

	/**
	 * True if the rat is breeding.
	 */
	private boolean isBreeding;

	/**
	 * Age of the rat.
	 */
	private int age;

	/**
	 * The health of the rat.
	 */
	private int health;

	/**
	 * The pregnancy number of the rat.
	 */
	private int pregnancyCounter;

	/**
	 * Constructor for new rats. Should be used at the start of the level or for
	 * newly born rats.
	 * 
	 * @param isMale the rats sex.
	 */
	public Rat(boolean isMale) {
		this.isMale = isMale;
		this.health = 5;
		this.isSterile = false;
		this.age = 0;
		resetRat();
	}

	/**
	 * Constructor for existing rats. Used when loading pre-existing rats from a
	 * save file.
	 * 
	 * @param age        		how old the rat is
	 * @param isMale     		the rats sex - True = male, False = female
	 * @param hp         		the rats health
	 * @param isSterile  		whether the rat is sterile - True = sterile, False = not
	 *                   sterile
	 * @param isBreeding 		boolean if rat is breeding
	 * @param pregnancyCounter	breeding stage of the rat
	 */
	public Rat(int age, boolean isMale, int hp, boolean isSterile, boolean isBreeding, int pregnancyCounter) {
		this.isMale = isMale;
		this.health = hp;
		this.isBreeding = isBreeding;
		this.isSterile = isSterile;
		this.age = age;
		this.pregnancyCounter = pregnancyCounter;
	}

	/**
	 * Gets the rat type.
	 * 
	 * @return rat type.
	 * @author J.
	 */
	public RatType getStatus() {
		if (isChild()) {
			return RatType.BABY;
		}
		return isMale ? RatType.MALE : RatType.FEMALE;
	}

	/**
	 * Gets whether the rat is male.
	 * 
	 * @return A boolean value - true = male.
	 */
	public boolean isMale() {
		return isMale;
	}

	/**
	 * Sets the rat sex to a new boolean value newIsMale.
	 * 
	 * @param newIsMale - whether the rat should be male after the interaction.
	 */
	public void setIsMale(boolean newIsMale) {
		if (isMale != newIsMale) {
			RatController.changeValue(newIsMale);
			isMale = newIsMale;
			resetRat();
		}
	}

	/**
	 * Gets the value of the rats pregnancy counter.
	 * 
	 * @return an integer of the rats pregnancy counter.
	 */
	public int getPregCounter() {
		return pregnancyCounter;
	}

	/**
	 * Returns {@code true} if this rat is giving birth.
	 * 
	 * @return {@code true} if rat is giving birth
	 */
	public boolean giveBirth() {
		return pregnancyCounter == 6 || pregnancyCounter == 3;
	}

	/**
	 * Changes stage of the pregnancy counter. (This is for LightTile)
	 */
	public void addPregStep() {
		pregnancyCounter++;
	}

	/**
	 * Sets this rat to be pregnant.
	 */
	public void setPregnant() {
		pregnancyCounter = MAX_PREGNANCY;
	}

	/**
	 * Gets whether the rat is pregnant or not.
	 * 
	 * @return isPregnant - true = pregnant.
	 */
	public boolean isPregnant() {
		pregnancyCounter--;
		return pregnancyCounter > 0;
	}

	/**
	 * Sterilises the rat. Adult rats will become sterile and can no longer breed.
	 * Child rats will become sterile adults afterwards.
	 */
	public void sterilise() {
		isSterile = true;
	}

	/**
	 * Gets whether the rat is sterile.
	 * 
	 * @return A boolean value - true = sterile.
	 */
	public boolean isSterile() {
		return isSterile;
	}

	/**
	 * Sets whether the rat is currently breeding.
	 * 
	 * @param breedStatus - A boolean value where true = breeding.
	 */
	public void setBreedStatus(boolean breedStatus) {
		isBreeding = breedStatus;
	}

	/**
	 * Gets whether the rat is currently breeding.
	 * 
	 * @return A boolean of whether the rat is breeding.
	 */
	public boolean isBreeding() {
		return isBreeding;
	}

	/**
	 * Switch status of breeding.
	 */
	public void changeBreed() {
		isBreeding = !isBreeding;
	}

	/**
	 * Gets how old the rat is.
	 * 
	 * @return An integer containing the rats age.
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Gets whether the rat is a child.
	 * 
	 * @return A boolean where true = the rat is a child.
	 */
	public boolean isChild() {
		return age++ < ADULT_AGE;
	}

	/**
	 * Damages the rat and kills it if its health is depleted.
	 * 
	 * @param damage - amount of damage dealt to the rat.
	 * @return {@code true} if the rat died
	 */
	public boolean damageRat(int damage) {
		health -= damage;
		if (health <= 0) {
			RatController.killRat(this);
			return true;
		}
		return false;
	}

	/**
	 * Gets the rats health value.
	 * 
	 * @return the rats health.
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Gets how many points the rat is worth.
	 * 
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
		output += age + Main.FILE_SUB_SEPERATOR;
		output += isMale + Main.FILE_SUB_SEPERATOR;
		output += health + Main.FILE_SUB_SEPERATOR;
		output += isSterile + Main.FILE_SUB_SEPERATOR;
		output += isBreeding + Main.FILE_SUB_SEPERATOR;
		output += pregnancyCounter + Main.FILE_SUB_SEPERATOR;
		return output;
	}

	/**
	 * Calculates how many points the rat is worth.
	 * 
	 * @return An integer value for the rats worth.
	 */
	private int calculatePointsUponDeath() {
		int points = 10;
		points += isBreeding ? 10 : 0;
		points += isPregnant() ? 10 : 0;
		points += isChild() ? 10 : 0;
		return points;
	}

	private void resetRat() {
		this.isBreeding = false;
		this.pregnancyCounter = 0;
	}
}
