/**
 * Superclass for items.
 * @author Andrew
 *
 */
public class Item {
    int itemHP;
    int itemCD;

    public void reduceItemHP() {
        itemHP -= 1;
    }

}
