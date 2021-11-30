/**
 * @author Andrew
 */
class Poison extends Item {
    private int itemHP = 1;

    public void itemAction (Rat rat) {
        //RatController rc = new RatController();
        //rc.killRat(rat);

        //Test
        System.out.println("Poison");
        itemUsed();
    }

    public int getItemHP () {
        return this.itemHP;
    }

    private void itemUsed () {
        this.itemHP -= 1;
    }
}
