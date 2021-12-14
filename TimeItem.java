import java.util.Timer;

public abstract class TimeItem extends Item {
	
	protected Timer timer = new Timer();

	public abstract void itemAction();
}
