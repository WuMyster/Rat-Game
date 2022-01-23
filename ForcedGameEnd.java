
/**
 * Invoked when the game should end but has not yet ended.
 * Not really necessary but wanted to play around with exceptions.
 * 
 * @author J
 *
 */
public class ForcedGameEnd extends IllegalStateException {

	@java.io.Serial
	private static final long serialVersionUID = 6446061450557793586L;
	
	/**
	 * Exception with no message passed in.
	 */
	public ForcedGameEnd() {
		super();
	}
	
	/**
	 * Exception with message passed in.
	 * @param string 	message
	 */
	public ForcedGameEnd(String string) {
		super(string);
	}

	
}
