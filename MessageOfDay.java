import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MessageOfDay {
	
	/**
	 * Page to visit to get Puzzle.
	 */
	private static final String URL_PUZZLE = "http://cswebcat.swansea.ac.uk/puzzle";
	
	/**
	 * Page to visit + answers to get Quote of the day.
	 */
	private static final String URL_SOLUTION = "http://cswebcat.swansea.ac.uk/message?solution=";
	
	/**
	 * Text needed at the end of puzzle.
	 */
	private static final String END_TEXT = "CS-230";
	
	/**
	 * Incorrect answer error message.
	 */
	private static final String ERROR_MSG = "Wrong answer!";
	
	/**
	 * URL is not valid error message.
	 */
	private static final String BAD_URL = "Bad URL!";
	
	/**
	 * The alphabet in a list.
	 */
	private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

	/**
	 * DEBUG Add static
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
		String input = getInformation(URL_PUZZLE);
		String out = String.valueOf(input.length() + END_TEXT.length());

		int counter = 1;

		for (char c : input.toCharArray()) {
			int i = c - 65 + (counter % 2 == 0 ? counter : -counter);
			while (i < 0) {
				i += 26;
			}
			out += ALPHABET[i % 26];

			counter++;
		}
		return getInformation(URL_SOLUTION + out + END_TEXT);
	}
	
	/**
	 * Returns webpage output from given URL
	 * @param webURL URL to visit
	 * @return string from webpage
	 */
	private static String getInformation(String webURL) {
		URL url = null;
		Scanner sc = null;
		String out = "";
		try {
			url = new URL(webURL);
			sc = new Scanner(url.openStream());
			sc.useDelimiter("\n");
			while (sc.hasNext()) {
				out += sc.next();
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
		return out;
	}
}
