import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MessageOfDay {
	
	private static final String URL_PUZZLE = "http://cswebcat.swansea.ac.uk/puzzle";
	private static final String URL_SOLUTION = "http://cswebcat.swansea.ac.uk/message?solution=";
	private static final String END_TEXT = "CS-230";
	private static final String ERROR_MSG = "Wrong answer!";
	private static final String BAD_URL = "Bad URL!";
	private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

	/**
	 * DEBUG
	 * @param args cli input
	 */
	public static void main(String[] args) {
		System.out.println(getMsgDay());
	}
	
	/**
	 * Returns the message of the day.
	 * @return message of the day
	 */
	public static String getMsgDay() {
		String input = getInformation(URL_PUZZLE, "");
		String out = String.valueOf(input.length() + END_TEXT.length());

		int counter = 1;
		boolean adder = false;

		for (char c : input.toCharArray()) {
			int i = c - 65 + (adder ? counter : -counter);
			while (i < 0) {
				i += 26;
			}
			out += ALPHABET[i % 26];

			adder = !adder;
			counter++;
		}
		return getInformation(URL_SOLUTION + out + END_TEXT, " ");
	}
	
	private static String getInformation(String webURL, String extra) {
		URL url = null;
		Scanner sc = null;
		String a = "";
		try {
			url = new URL(webURL);
			sc = new Scanner(url.openStream());
			while (sc.hasNext()) {
				a += sc.next() + extra;
			}
		} catch (MalformedURLException e2) {
			System.err.println(BAD_URL);
			// e2.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.err.println(ERROR_MSG);
			// e.printStackTrace();
			System.exit(0);
		} finally {
			sc.close();
		}
		return a;
	}
}
