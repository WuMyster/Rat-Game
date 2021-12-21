import java.util.Timer;

/**
 * Abstract class for timed items. Has itemAction() which is called when
 * item is first placed.
 * 
 * @author Jing
 *
 */
public abstract class TimeItem extends Item {
	
	/**
	 * Timer for timed events.
	 */
	protected Timer timer = new Timer();

	/**
	 * Specific timed event start for this item. Is called when 
	 * item is first placed.
	 */
	public abstract void itemAction();
}
