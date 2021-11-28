/**
@author Salim, Andrew
 Note: fixed item. No need to check if rat is not male, else statement takes care of it
 */
public class SexChange extends Item {
    /** @param Rat a.getSex() takes as input the sex of the rat
     *if rat is not male and thus female, then make rat male. Otherwise keep male. Subtract one from item count
     */
    public void itemAction(Rat a) {
        if (a.getIsMale() == false) {
            a.setIsMale(true);
        } else {
            a.setIsMale(false);
        }

        // Need to see how inventory is implemented to implement this properly
        count -=1;
    }
}
