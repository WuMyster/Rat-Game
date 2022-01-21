import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

import javafx.application.Platform;

/**
 * Number of available items that can be used.
 * 
 * @author J
 *
 */
public class Inventory {
	
	/**
	 * Max number of items. #IDE complain that this variable must be before itemCounter.#
	 * +1 due to DeathRat being treated like item but not in ItemType.
	 */
	public static final int MAX_NUM_ITEMS = ItemType.values().length + 2;
	
	/**
	 * Current number of items for each item.
	 */
	private static final int[] itemCounter = new int[MAX_NUM_ITEMS];
	
	/**
	 * Max number of items.
	 */
	private static final int MAX_ITEM = 5;

	/**
	 * Random number generator.
	 */
	private static final Random RAND = new Random();
	
	/**
	 * Timer for each item being added on.
	 */
	private static Timer timer = new Timer();
	
	/**
	 * Time in between each item being added on in miliseconds.
	 */
	private static final int TIME_STOP = 1000;
	
	/**
	 * Starts the inventory count down.
	 */
	public static void startInv() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				addNext(-1, 0);
			}
		}, 0, TIME_STOP);
	}
	
	/**
	 * Stops inventory count down.
	 */
	public static void stopInv() {
		timer.cancel();
		timer = new Timer();
	}
	
	/**
	 * Removes a counter for an item.
	 * @param it 	Item Type to remove
	 */
	public static void removeItem(ItemType it) {
		int n = toInt(it);
		itemCounter[n]--;
		changeCounterNum(n, itemCounter[n]);
	}
	
	/**
	 * Removes a counter for death rat.
	 * @param superDR 	boolean if DR used was superDR
	 */
	public static void removeDeathRatCounter(boolean superDR) {
		int n = MAX_NUM_ITEMS - (superDR ? 1 : 2);
		itemCounter[n]--;
		changeCounterNum(n, itemCounter[n]);
	}
	
	/**
	 * Writes current inventory state to file.
	 * 
	 * @param filename	filename of save file
	 */
	public static void writeInventoryToFile(String filename) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileOutputStream(new File(filename),true));
			for (int i : itemCounter) {
				out.append((char) (i + '0'));
				out.append(Main.FILE_MAIN_SEPERATOR.charAt(0));
			}
			out.append('\n');
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the values for the inventory number
	 * 
	 * @param input 	number of items in inventory string format
	 */
	public static void setInventory(String input) {
		String[] in = input.split(Main.FILE_MAIN_SEPERATOR);
		for (int i = 0; i < itemCounter.length; i++) {
			int num = Integer.valueOf(in[i]);
			itemCounter[i] = num;
			changeCounterNum(i, num);
		}
	}
	
	/**
	 * Adds extra counter to number of items available.
	 * @param avoid		number to avoid to reduce potential stack overflow issue
	 * @param count		number of methods run, stop stack overflow issue
	 */
	private static void addNext(int avoid, int count) {
		if (count > 10) {
			return;
		}
		// Trying to minimise chance of stack overflow
		int itemNum = RAND.nextInt(MAX_NUM_ITEMS);
		while (itemNum == avoid) {
			itemNum = RAND.nextInt(MAX_NUM_ITEMS);
		}
		if (itemCounter[itemNum] == MAX_ITEM) {
			addNext(itemNum, count + 1);
		} else {
			itemCounter[itemNum]++;
			changeCounterNum(itemNum, itemCounter[itemNum]);
		}
	}
	
	/**
	 * Changes the number of items in the items
	 * 
	 * @param itemNum	item number to change counter
	 * @param num		number to change item number to
	 */
	private static void changeCounterNum(int itemNum, int num) {
		Platform.runLater(() -> GameGUI.setItemCounter(itemNum, num));
	}
	
	/**
	 * Converts ItemType to int.
	 * 
	 * @param it	ItemType to convert
	 * @return		number that the ItemType is in the order
	 */
	private static int toInt(ItemType it) {
		return IntStream.iterate(0, e -> e + 1)
			   .filter(i -> ItemType.values()[i] == it)
			   .limit(1)
			   .sum();
	}
}
