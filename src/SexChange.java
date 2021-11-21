package src;
/*
@author Salim
 */
public class SexChange extends Item {
    /* @param rat a.getSex() takes as input the sex of the rat
     *if rat is not male and thus female, then make rat male. Otherwise keep male. Subtract one from item count
     */
    public void toMale(rat a.getSex()){
        if (a.isMale() == False) {
            a.isMale() = True;
        } else {
            a.isMale() = True;
        }
        count -=1;
    }
    /* @param rat a.getSex() takes as input the sex of the rat
     *if rat is male and thus not female, then make rat not male. Otherwise keep not male. Subtract one from item count
     */
    public void toFemale(rat a.getSex()){
        if (a.isMale() == True) {
            a.isMale() = False;
        } else {
            a.isMale() = False;
        }
        count -=1;
    }
}
