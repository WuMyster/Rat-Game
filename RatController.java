import java.util.ArrayList;
import java.util.Random;
/**
 * Has a list of all alive rats, deals with rat interactions (breeding) and killing rats.
 * 
 * @author Ollie Jarrett
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
	
	/**
	 * Random generator.
	 */
	private static Random nextRand = new Random();
	
	/**
	 * Number of male rats on the board.
	 */
	private static int maleCounter;
	
	/**
	 * Number of female rats on the board.
	 */
	private static int femaleCounter;	

	/**
	 * Sets values of different elements.
	 * 
	 * @param max		maximum number of rats allowed
	 * @param points 	points already earned from game
	 */
	public static void setRatController(int max, int points) {
		maxNumOfRats = max;
		RatController.points = points;
	}

	/**
	 * Returns points earned from killing rats.
	 * @return 	number of points earned
	 */
	public static int getPoints() {
		return points;
	}
	
	/**
	 * Returns list of number of male rats and female rats as a percentage.
	 * 
	 * @return	list of percentage of male rats and female rats
	 */
	public static int[] getCounter() {
		int[] out = new int[2];
		out[0] = (maleCounter * 100) / maxNumOfRats;
		out[1] = (femaleCounter * 100) / maxNumOfRats;
		return out;
	}
	
	/**
	 * Will need to compare number of rats on the map to the max number of rats 
	 * you should have.
	 * @return {@code true} if the game has finished
	 */
	public static boolean stopGame() {
		return ratList.isEmpty() || ratList.size() >= maxNumOfRats;
	}
	
	/**
	 * Adds a new baby rat of random sex to the ratList.
	 * @return newly constructed baby rat
	 */
	public static Rat newBabyRat() {
		boolean gender = nextRand.nextBoolean();
		increaseRatCounter(gender);
		Rat r = new Rat(gender); // boolean to determine gender
		ratList.add(r);
		return r;
	}
	
	/**
	 * Takes in a list of toString rat values and adds them to the rat list.
	 * @param newRats 	list of rat information as strings
	 */
	public static void addRats(String[] newRats) {
		for(int i = 0; i < newRats.length; i++) {
			Rat r = stringToRat(newRats[i]);
			increaseRatCounter(r.isMale());
			ratList.add(r);
		}
	}
	
	/**
	 * Takes in a Rat class toString() value and adds it to the rat list.
	 * 
	 * @param newRat	formatted rat string
	 * @return 			a constructed {@code rat}
	 */
	public static Rat addRat(String newRat) {
		Rat r = stringToRat(newRat);
		increaseRatCounter(r.isMale());
		ratList.add(r);
		return r;
	}
	
	/**
	 * Takes in a boolean to create a baby rat of specific gender.
	 * 
	 * @param gender	new Baby rat gender
	 * @return 			new baby {@code rat}
	 */
	public static Rat addRat(Boolean gender) {
		Rat r = new Rat(gender);
		increaseRatCounter(r.isMale());
		ratList.add(r);
		return r;
	}
	
	/**
	 * Removes a specific rat from rats.
	 * 
	 * @param deadRat	the rat to be killed
	 */
	public static void killRat(Rat deadRat) {
		points += deadRat.getPointsUponDeath();
		decreaseRatCuonter(deadRat.isMale());
		ratList.remove(deadRat);
	}
	
	/**
	 * Returns list of list of stationary rats and list of moving rats.
	 *
	 * @param ratsOnTile	list of rats that are interacting with each other
	 * @return 				list of rats where first list is stationary rats and 
	 * 					second list is moving rats
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
	 * Returns nested list of rats of different movement status.
	 * 
	 * @param ratsOnTile	list of rats interacting
	 * @return 				list of rats 0) male rats that can breed, 
	 * 					1) female rats that can breed, 2) rats that will not move,
	 * 					3) list of rats moving
	 */
	private static ArrayList<ArrayList<Rat>> sortRats(ArrayList<Rat> ratsOnTile) {
		ArrayList<Rat> male = new ArrayList<>();
		ArrayList<Rat> female = new ArrayList<>();
		ArrayList<Rat> stop = new ArrayList<>();
		ArrayList<Rat> moving = new ArrayList<>();
		
		for (Rat r : ratsOnTile) {
			if (r.isChild()) {			// If child
				moving.add(r);
			} else if (r.isBreeding()) { // If breeding
				r.setBreedStatus(false);
				stop.add(r);
			} else if (r.isPregnant()) { // If pregnant
				if (r.giveBirth()) {
					moving.add(newBabyRat());
					stop.add(r);
				} else {
					moving.add(r);
				}
			} else if (r.isSterile()) {	// If sterile
				moving.add(r);
			} else {					// Otherwise attempt to breed
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
	 * 
	 * @param male 		list of male rats ready to breed
	 * @param female	list of female rats ready to breed
	 * @return 			List of rats who are breeding and list of rats not breeding
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
	 * Increases the counter of the different gender of rats.
	 * 
	 * @param gender	{@code true} if rat is male
	 */
	private static void increaseRatCounter(boolean gender) {
		if (gender) {
			maleCounter++;
		} else {
			femaleCounter++;
		}
	}
	
	private static void decreaseRatCuonter(boolean gender) {
		if (gender) {
			maleCounter--;
		} else {
			femaleCounter--;
		}
	}

	/**
	 * Creates a rat from a rats toString output.
	 * @param ratString 	String format of Rat
	 * @return {@code Rat} from specified input
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
