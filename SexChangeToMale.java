/**
 * @author Salim
 * */

public class SexChangeToMale extends Item {
    public void itemAction(Rat a) {
        if (a.getIsMale() == false) {
            a.setIsMale(true);
        }

        // Need to see how inventory is implemented to implement this properly
        count -=1;
    }
}
