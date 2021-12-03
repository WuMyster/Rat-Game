/**
 * @author Andrew
 */
class Poison extends Item {
    enum name {
        POISON
    }

    private int itemHP = 1;

    public void itemAction (Rat rat) {
        // Waiting for ratcontroller kill() to be implemented
        //RatController rc = new RatController();
        //rc.killRat(rat);

        itemUsed();
    }

    public int getItemHP () {
        return this.itemHP;
    }

    private void itemUsed () {
        this.itemHP -= 1;
    }
}
