import java.util.ArrayList;
import java.util.Arrays;

public class RatControllerTest {

	public static void main(String[] args) {
		Rat m1 = new Rat(100, true, false, 10, false, false, false);
		Rat f1 = new Rat(100, false, false, 10, false, false, false);
		Rat f2 = new Rat(100, false, false, 10, false, false, false);
		
		ArrayList<ArrayList<Rat>> t1 = RatController.ratInteractions(new ArrayList<>(Arrays.asList(m1, f1, f2)));
		
		for(ArrayList<Rat> rs : t1) {
			for(Rat r : rs) {
				System.out.println(r.toString());
			}
			System.out.println("=============");
		}
		
		System.out.println("ASDFASDF");
		t1 = RatController.ratInteractions(new ArrayList<>(Arrays.asList(m1, f1, f2)));
		for(ArrayList<Rat> rs : t1) {
			for(Rat r : rs) {
				System.out.println(r.toString());
			}
			System.out.println("=============");
		}
		
	}
}
