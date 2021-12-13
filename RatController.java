import java.util.ArrayList;
import java.util.Random;
/**
 * This class stores an arraylist of the rats on the board, dealing with rat interactions (breeding) and killing the rats.
 * @author Ollie Jarrett
 * @version
 */
public class RatController {
	
	/**
	 * List of rats alive.
	 */
	private static ArrayList<Rat> ratList = new ArrayList<>();
	
	/**
	 * Maximum number of rats before losing.
	 */
	private static int maxNumOfRats = 10;
	
	/**
	 * Number of points earned in this game.
	 */
	private static int points;
	
	private static Random nextRand = new Random();
	

	/**
	 * Sets the max number of rats allowed before the game ends.
	 * @param max - the maximum number of rats allowed
	 */
	public static void setRatController(int max) {
		maxNumOfRats = max;
	}

	/**
	 * Returns points earned from killing rats.
	 * @return An integer representing the points earned.
	 */
	public static int getPoints() {
		return points;
	}
	
	/**
	 * Will need to compare number of rats on the map to the max number of rats 
	 * you should have.
	 * @return {@code true} if the number of rats do not exceed the max number
	 * of rats allowed on the map
	 */
	public static boolean continueGame() {
		return ratList.size() < maxNumOfRats && !ratList.isEmpty();
	}
	
	/**
	 * Adds a new baby rat of random sex to the ratList.
	 * @return The newly constructed baby rat
	 */
	public static Rat newBabyRat() {
		Rat r = new Rat(nextRand.nextBoolean()); // boolean to determine gender
		ratList.add(r);
		return r;
	}
	
	/**
	 * Takes in a list of toString rat values and adds them to the rat list.
	 * @param newRats - an array of strings.
	 */
	public static void addRats(String[] newRats) {
		for(int i = 0; i < newRats.length; i++) {
			ratList.add(stringToRat(newRats[i]));
		}
	}
	
	/**
	 * Takes in a Rat class toString() value and adds it to the rat list.
	 * @param newRat - the formatted rat string.
	 * @return Returns a single constructed rat
	 */
	public static Rat addRat(String newRat) {
		Rat r = stringToRat(newRat);
		ratList.add(r);
		return r;
	}
	
	/**
	 * Removes a specific rat from rats.
	 * @param deadRat - the rat to be killed by the rat controller.
	 */
	public static void killRat(Rat deadRat) {
		points += deadRat.getPointsUponDeath();
		ratList.remove(deadRat);
	}
	
	/**
	 * Deals with rat to rat interactions.
	 * Sorts rats into stationary rats and moving rats.
	 * Breeds rats which are breedable.
	 * @param ratsOnTile - An arraylist of rats on an individual tile
	 * @return A nested arraylist of rats, where the first index contains rats 
	 * that aren't moving and the second index contains rats which will be moving
	 */
	public static ArrayList<ArrayList<Rat>> ratInteractions(ArrayList<Rat> ratsOnTile) {
		
		// Divide list into rats who are and aren't moving, 0 and 1 are gender of rats ready to mate
		ArrayList<ArrayList<Rat>> sortedRatsOnTile = sortRats(ratsOnTile);
		ArrayList<Rat> movingRats = sortedRatsOnTile.get(2); // Rats that aren't ready to mate
		ArrayList<Rat> stationaryRats = sortedRatsOnTile.get(3); // Not moving for whatever reason
		
		ArrayList<ArrayList<Rat>> bredRats = breedRats(sortedRatsOnTile.get(0), sortedRatsOnTile.get(1));
		
		stationaryRats.addAll(bredRats.get(0)); // Adding in rats that are mating
		movingRats.addAll(bredRats.get(1)); // Rats that failed to mate
		
		ArrayList<ArrayList<Rat>> stationaryMovingRats = new ArrayList<>();
		stationaryMovingRats.add(stationaryRats); 
		stationaryMovingRats.add(movingRats);
		
		return stationaryMovingRats;
	}
	
	/**
	 * Sorts a list of rats into a nested list with three sub-lists
	 * =>breedable male rats
	 * =>breedable female rats
	 * =>moving rats
	 * =>Not moving rats.
	 * @param ratsOnTile - An arraylist of rats
	 * @return A nested arraylist of rats, where the first index contains breedable male rats, the second contains breedable female rats and the third contains moving rats
	 */
	private static ArrayList<ArrayList<Rat>> sortRats(ArrayList<Rat> ratsOnTile) {
		ArrayList<Rat> male = new ArrayList<>();
		ArrayList<Rat> female = new ArrayList<>();
		ArrayList<Rat> stop = new ArrayList<>();
		ArrayList<Rat> moving = new ArrayList<>();
		
		for (Rat r : ratsOnTile) {
			if (r.isChild()) {
				moving.add(r);
			} else if (r.isBreeding()) {
				r.setBreedStatus(false);
				stop.add(r);
			} else if (r.isPregnant()) {
				int stage = r.getPregCounter();
				if (stage == 5 || stage == 1) {
					// moving.add(newBabyRat());
					stop.add(r);
				} else {
					moving.add(r);
				}
			} else if (r.isSterile()) {
				moving.add(r);
			} else {
				if (r.isMale()) {
					male.add(r);
				} else {
					female.add(r);
				}
			}
		}
		
		ArrayList<ArrayList<Rat>> newRatList = new ArrayList<>();
		newRatList.add(male);
		newRatList.add(female);
		newRatList.add(moving);
		newRatList.add(stop);
		return newRatList;
	}
	
	/**
	 * Takes in two lists of rats and breeds as many as it can.
	 * @param male - A list of male rats.
	 * @param female - A list of female rats.
	 * @return A nested list of rats with 2 indexes, the rats who're breeding and those who aren't.
	 */
	private static ArrayList<ArrayList<Rat>> breedRats(ArrayList<Rat> male, ArrayList<Rat> female) {
		
		ArrayList<Rat> breeding = new ArrayList<>();
		ArrayList<Rat> notBreeding = new ArrayList<>();
		
		int i;
		for (i = 0; i != male.size() && i != female.size(); i++) {
			Rat mRat = male.get(i);
			Rat fRat = female.get(i);
			
			mRat.changeBreed();
			fRat.changeBreed();
			fRat.setPregnant();
			
			breeding.add(mRat);
			breeding.add(fRat);
		}

		notBreeding.addAll(new ArrayList<>(male.subList(i, male.size())));
		notBreeding.addAll(new ArrayList<>(female.subList(i, female.size())));
		
		ArrayList<ArrayList<Rat>> postBreedRats = new ArrayList<>();
		postBreedRats.add(breeding);				//0
		postBreedRats.add(notBreeding);				//1
		return postBreedRats;
	}

	/**
	 * Constructs a rat from a rats toString output.
	 * @param ratString - A toString output from the Rat class toString() method.
	 * @return a constructed rat with the same values as in ratString.
	 */
	private static Rat stringToRat(String ratString) {
		String[] newRat = ratString.split(",");
		
		int age = Integer.parseInt(newRat[0]);
		boolean isMale = Boolean.parseBoolean(newRat[1]);
		boolean isPregnant = Boolean.parseBoolean(newRat[2]);
		int hp = Integer.parseInt(newRat[3]);
		boolean isSterile = Boolean.parseBoolean(newRat[4]);
		boolean isBreeding = Boolean.parseBoolean(newRat[5]);
		boolean isDeath = Boolean.parseBoolean(newRat[6]);
		return new Rat(age, isMale, isPregnant, hp, isSterile, isBreeding, isDeath);
	}
	
}
