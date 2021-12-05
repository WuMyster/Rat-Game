/**
 * @author Salim, Andrew
 * */

public class SexChangeToMale extends Item {
    public void itemAction(Rat a) {
        a.setIsMale(true);
        System.out.println("toMale item used");


        // Need to see how inventory is implemented to implement this properly
        //count -=1;
    }
}
