import java.util.ArrayList;
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
	public static void ratInteractions(ArrayList<Rat> ratsOnTile) {
		
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
		
		for(int i = 0; i<ratsOnTile.size(); i++) {
			Rat nextRat = ratsOnTile.get(i);
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
			ratsOnTile.remove(i);
		}
		
		ArrayList<ArrayList<Rat>> newRatList = new ArrayList<>();
		newRatList.add(male);				//0
		newRatList.add(female);				//1
		newRatList.add(moving);				//2
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
}
