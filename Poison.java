/**
 * @author Andrew
 */
class Poison extends Item {
    private int itemHP = 1;

    public void itemAction (Rat rat) {
        RatController.killRat(rat);
        System.out.println("Rat killed by poison");
        itemUsed();
    }

    public int getItemHP () {
        return this.itemHP;
    }

    private void itemUsed () {
        this.itemHP -= 1;
    }
}
