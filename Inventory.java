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
	private static int MAX_NUM_ITEMS = ItemType.values().length + 1;
	
	/**
	 * Current number of items for each item.
	 */
	private static int[] itemCounter = new int[MAX_NUM_ITEMS];
	
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
	private final static int TIME_STOP = 5000;
	
	/**
	 * Starts the inventory count down.
	 */
	public static void startInv() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				addNext();
			}
		}, 0, TIME_STOP);
	}
	
	/**
	 * Stops inventory count down.
	 */
	public static void stopInv() {
		timer.cancel();
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
	 */
	public static void removeDeathRatCounter() {
		int n = ItemType.values().length + 1;
		itemCounter[n]--;
		changeCounterNum(n, itemCounter[n]);
	}
	
	/**
	 * Adds extra counter to number of items available.
	 * ERROR -> all items are maxed out.
	 */
	private static void addNext() {
		int itemNum = RAND.nextInt(MAX_NUM_ITEMS);
		if (itemCounter[itemNum] == MAX_ITEM) {
			addNext();
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
