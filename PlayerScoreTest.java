import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PlayerScoreTest {

	public static void main(String[] args) {
		PlayerScore a = new PlayerScore("a", 20, 30, 40);
		PlayerScore b = new PlayerScore("b", 10, 30, 40);
		PlayerScore c = new PlayerScore("c", 30, 30, 40);
		ArrayList<PlayerScore> lis = new ArrayList<>(Arrays.asList(a, b, c));
		
		for (PlayerScore p : lis) {
			System.out.println(p);
		}
		
		Collections.sort(lis);
		Collections.reverse(lis);
		
		System.out.println("\n\n");
		
		for (PlayerScore p : lis) {
			System.out.println(p);
		}
	}
}
