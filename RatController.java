import java.util.ArrayList;
import java.util.Random;
/**
 * 
 * @author Ollie Jarrett
 *
 */
public class RatController {
	private static ArrayList<Rat> rats = new ArrayList<>(); //static list of rats in rat controller
	private static int points; //static ratcontroller stores points 
	
		
	public static void killRat(Rat rat1) { //static method to kill rats
		points += rat1.getPointsUponDeath();
		for(int i = 0; i<rats.size(); i++) {
			if(rats.get(i) == rat1) {
				rats.remove(i);
			}
		}
	}
	
	public static int getPoints() { //ratcontroller.getpoints - static method
		return points;
	}
	
	//give list of rats -> rat interact with rat
	//ratInteractions method - take in rats an arraylist of rats
	//returns 2 arraylists of rats - rats that aren't moving (i.e interacting) and rats that are moving
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
	
	private static ArrayList<ArrayList<Rat>> sortRats(ArrayList<Rat> ratsOnTile) {
		ArrayList<Rat> male = new ArrayList<>();
		ArrayList<Rat> female = new ArrayList<>();
		ArrayList<Rat> moving = new ArrayList<>();
		
		while(ratsOnTile.size() > 0) {
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
	
	public static void addRats(String[] newRats) {
		for(int i = 0; i<newRats.length; i++) {
			String[] newRat = newRats[i].split(",");
			rats.add(stringToRat(newRat));
		}
	}
	
	private Rat stringToRat(String[] newRat) {
		int newRatAge = Integer.parseInt(newRat[0]);
		boolean newRatIsMale = Boolean.parseBoolean(newRat[1]);
		boolean newRatIsPregnant = Boolean.parseBoolean(newRat[2]);
		int newRatHP = Integer.parseInt(newRat[3]);
		boolean newRatIsSterile = Boolean.parseBoolean(newRat[4]);
		boolean newRatIsBreeding = Boolean.parseBoolean(newRat[5]);
		boolean newRatIsDeathRat = Boolean.parseBoolean(newRat[6]);
		return new Rat(newRatAge, newRatIsMale, newRatIsPregnant, newRatHP, newRatIsSterile, newRatIsBreeding, newRatIsDeathRat);
	}
	
	public static void newBabyRat() {
		Random nextRand = new Random();
		Boolean newRatIsMale = nextRand.nextBoolean();
		rats.add(newRatIsMale);
	}
	
}
