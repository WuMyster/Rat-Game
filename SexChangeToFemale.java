/**
@author Salim, Andrew
 */
public class SexChangeToFemale extends Item {
    /**
     * if rat is not male and thus female, then make rat male. Otherwise keep male. Subtract one from item count
     * @param a a Rat object.
     */
    public void itemAction(Rat a) {
        if (a.getIsMale() == true) {
            a.setIsMale(false);
        }


        // Need to see how inventory is implemented to implement this properly
        //count -=1;
    }
}
