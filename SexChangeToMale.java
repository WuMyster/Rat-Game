/**
 * @author Salim, Andrew
 * */

public class SexChangeToMale extends Item {
    public void itemAction(Rat a) {
        if (a.getIsMale() == false) {
            a.setIsMale(true);
            System.out.println("Female to Male");
        }

        // Need to see how inventory is implemented to implement this properly
        //count -=1;
    }
}
