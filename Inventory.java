import java.util.Random;
import java.util.stream.IntStream;

/**
 * Number of available items that can be used.
 * 
 * @author J
 *
 */
public class Inventory {
	
	/**
	 * Max number of items. #IDE complain that this variable must be before itemCounter.#
	 */
	private static int MAX_NUM_ITEMS = ItemType.values().length;
	
	/**
	 * Current number of items for each item.
	 */
	public static int[] itemCounter = new int[MAX_NUM_ITEMS];
	
	/**
	 * Max number of items.
	 */
	private static final int MAX_ITEM = 5;

	/**
	 * Random number generator.
	 */
	private static final Random RAND = new Random();
	
	/**
	 * Adds extra counter to number of items available.
	 */
	public static void addNext() {
		int itemNum = RAND.nextInt(MAX_NUM_ITEMS);
		if (itemCounter[itemNum] == MAX_ITEM) {
			addNext();
		} else {
			itemCounter[itemNum]++;
			changeCounterNum(ItemType.values()[itemNum], itemCounter[itemNum]);
		}
	}
	
	/**
	 * Removes a counter for an item.
	 * @param it 	Item Type to remove
	 */
	public static void removeItem(ItemType it) {
		int n = IntStream.iterate(0, e -> e + 1)
		.filter(i -> ItemType.values()[i] == it)
		.limit(1)
		.sum();

		itemCounter[n]--;
		changeCounterNum(it, itemCounter[n]);
	}
	
	private static void changeCounterNum(ItemType it, int num) {
		// {@code Button}.setDisable(num == 0); // can be clicked or not
		// Update number on counter
	}
}
