package src;
/*
@author Salim
 */
public class Sterilisation extends Item {
    //not sterile by default
    private boolean sterile = false;

    public void isSterile() {
        sterile = true;
        count -=1;
    }
}
