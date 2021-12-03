import java.util.ArrayList;

/**
 * 
 * @author 
 *
 */
abstract class Item {
	//Method
	boolean isAlive = true;
	
	public abstract void itemOnAction(ArrayList<Rat> rs);
	
}
