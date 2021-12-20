import java.util.ArrayList;
import java.util.Arrays;

public class RatControllerTest {

	public static void main(String[] args) {
		Rat m1 = new Rat(100, true, 10, false, false, 0);
		Rat f1 = new Rat(100, false, 10, false, false, 0);
		Rat f2 = new Rat(100, false, 10, false, false, 0);
		
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
