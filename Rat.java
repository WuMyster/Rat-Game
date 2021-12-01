
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
	

	
	//Constructs a baby rat
	public Rat(boolean male, int health) {
		this.isMale = male;
		this.health = health;
		isBreeding = false;
		isPregnant = false;
		isDeathRat = false;
		isSterile = false;
		age = 0;
		pointsUponDeath = 30;
	}

	//constructs a rat from a save file
	public Rat(int age, boolean male, boolean pregnant, int hp, boolean sterile, boolean breeding, boolean deathRat) {
		this.isMale = male;
		this.health = hp;
		isBreeding = breeding;
		isPregnant = pregnant;
		isDeathRat = deathRat;
		isSterile = sterile;
		this.age = age;
		pointsUponDeath = 30;
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
	public boolean isChild() {
		if(age<50) {
			return true;
		}
		else {
			return false;
		}
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
