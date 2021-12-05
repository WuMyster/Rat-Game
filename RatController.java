import java.util.ArrayList;
import java.util.Random;
/**
 * This class stores an arraylist of the rats on the board, dealing with rat interactions (breeding) and killing the rats.
 * @author Ollie Jarrett
 * @version
 */
public class RatController {
	private static ArrayList<Rat> ratList = new ArrayList<>();
	private static int points;
	
	
	/**
	 * Tells other classes how many points have been earnt by killing rats
	 * @return An integer representing the points earnt
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
		return true;
	}
	
	
	/**
	 * Adds a new baby rat to rats
	 */
	public static void newBabyRat() {
		Random nextRand = new Random();
		Boolean newRatIsMale = nextRand.nextBoolean();
		ratList.add(new Rat(newRatIsMale));
	}
	
	/**
	 * Takes in a list of toString rat values and adds them to the rat list
	 * @param newRats
	 */
	public static void addRats(String[] newRats) {
		for(int i = 0; i<newRats.length; i++) {
			ratList.add(stringToRat(newRats[i]));
		}
	}

	/**
	 * Takes in a Rat toString rat value and adds it to the rat list
	 * @param newRat - the formatted rat string.
	 */
	public static void addRat(String newRat) {
		ratList.add(stringToRat(newRat));
	}
	
	/**
	 * Removes a specific rat from rats
	 * @param deadRat - the rat to be killed by the rat controller
	 */
	public static void killRat(Rat deadRat) { //static method to kill rats
		points += deadRat.getPointsUponDeath();
		for(int i = 0; i<ratList.size(); i++) {
			if(ratList.get(i) == deadRat) {
				ratList.remove(i);
			}
		}
	}
		
	/**
	 * Constructs a rat from a rats toString output
	 * @param ratString - A toString output from the Rat.toString() method
	 * @return a constructed rat with the same values as in ratString
	 */
	private static Rat stringToRat(String ratString) {
		String[] newRat = ratString.split(",");
		
		int newRatAge = Integer.parseInt(newRat[0]);
		boolean newRatIsMale = Boolean.parseBoolean(newRat[1]);
		boolean newRatIsPregnant = Boolean.parseBoolean(newRat[2]);
		int newRatHP = Integer.parseInt(newRat[3]);
		boolean newRatIsSterile = Boolean.parseBoolean(newRat[4]);
		boolean newRatIsBreeding = Boolean.parseBoolean(newRat[5]);
		boolean newRatIsDeathRat = Boolean.parseBoolean(newRat[6]);
		return new Rat(newRatAge, newRatIsMale, newRatIsPregnant, newRatHP, newRatIsSterile, newRatIsBreeding, newRatIsDeathRat);
	}
	
	
	/**
	 * Sorts an arraylist of rats into two arraylists, those which are moving and those which aren't
	 * @param ratsOnTile - An arraylist of rats on an individual tile
	 * @return A nested arraylist of rats, where the first index contains rats that aren't moving and the second index contains rats which will be moving
	 */
	public static ArrayList<ArrayList<Rat>> ratInteractions(ArrayList<Rat> ratsOnTile) {
		
		ArrayList<ArrayList<Rat>> sortedRatsOnTile = sortRats(ratsOnTile);
		ArrayList<Rat> movingRats = sortedRatsOnTile.get(2);
		
		ArrayList<ArrayList<Rat>> bredRats = breedRats(sortedRatsOnTile.get(0), sortedRatsOnTile.get(1));
		ArrayList<Rat> notBred = bredRats.get(1);
		
		while(notBred.size()>0) {
			movingRats.add(notBred.get(0));
			notBred.remove(0);
		}
		
		ArrayList<ArrayList<Rat>> stationaryMovingRats = new ArrayList<>();
		stationaryMovingRats.add(bredRats.get(0));
		stationaryMovingRats.add(movingRats);
		
		return stationaryMovingRats;
	}
	
	/**
	 * Sorts an arraylist of rats into three arraylists, those which are breedable male rats, breedable female rats and moving rats
	 * @param ratsOnTile - An arraylist of rats
	 * @return A nested arraylist of rats, where the first index contains breedable male rats, the second contains breedable female rats and the third contains moving rats
	 */
	private static ArrayList<ArrayList<Rat>> sortRats(ArrayList<Rat> ratsOnTile) {
		ArrayList<Rat> male = new ArrayList<>();
		ArrayList<Rat> female = new ArrayList<>();
		ArrayList<Rat> moving = new ArrayList<>();
		
		while(ratsOnTile.size() > 0) {
			ratsOnTile.get(0).incrementAge();
			Rat nextRat = ratsOnTile.get(0);
			if(nextRat.isChild()) {
				moving.add(nextRat);
			} else if(nextRat.getIsBreeding() == true) {
				if(nextRat.getIsMale() == false) {
					nextRat.setBreedStatus(false);
					nextRat.setPregnancy(true);
				} else {
					nextRat.setBreedStatus(false);
				}
				moving.add(nextRat);
			} else if(nextRat.getIsMale() == true) {
				if(nextRat.getSterile() == false) {
					male.add(nextRat);
				} else {
					moving.add(nextRat);
				}
			} else {
				if(nextRat.getPregnant() == false && nextRat.getSterile() == false) {
					female.add(nextRat);
				} else {
					moving.add(nextRat);
				}
			}
			ratsOnTile.remove(0);
		}
		
		ArrayList<ArrayList<Rat>> newRatList = new ArrayList<>();
		newRatList.add(male);
		newRatList.add(female);
		newRatList.add(moving);
		return newRatList;
	}
	
	/**
	 * 
	 * @param male - An arraylist of male rats
	 * @param female - An arraylist of female rats
	 * @return A nested arraylist of rats with 2 indexes, the rats who're breeding and those who aren't
	 */
	private static ArrayList<ArrayList<Rat>> breedRats(ArrayList<Rat> male, ArrayList<Rat> female) {
		
		ArrayList<Rat> breeding = new ArrayList<>();
		ArrayList<Rat> notBreeding = new ArrayList<>();
		while(male.size()>0 && female.size()>0) {
			
			male.get(0).setBreedStatus(true);
			female.get(0).setBreedStatus(true);
				
			breeding.add(male.get(0));
			breeding.add(female.get(0));
			
			male.remove(0);
			female.remove(0);
			
		}
		while(male.size()>0) {
			notBreeding.add(male.get(0));
			male.remove(0);
		}
		while(female.size()>0) {
			notBreeding.add(female.get(0));
			female.remove(0);
		}
		ArrayList<ArrayList<Rat>> postBreedRats = new ArrayList<>();
		postBreedRats.add(breeding);				//0
		postBreedRats.add(notBreeding);				//1
		return postBreedRats;
	}
}
