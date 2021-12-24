import javafx.beans.property.SimpleStringProperty;

public class PlayerScore implements Comparable<PlayerScore> {

	/**
	 * Name of player.
	 */
	private String name1;
	
	private final SimpleStringProperty name;
	
	/**
	 * Score achieved by player.
	 */
	private final int score;
	
	/**
	 * Time it took to finish the game in seconds.
	 */
	private final int timeCompleted;
	
	/**
	 * Level completed for current score.
	 */
	private final int level;
	
	/**
	 * Constructs a {@code PlayerScore} based on inputs.
	 * 
	 * @param name			name of player
	 * @param score			score achieved by player
	 * @param timeCompleted	time it took to finish game
	 * @param level			level completed
	 */
	public PlayerScore(String name, int score, int timeCompleted, int level) {
		this.name1 = name;
		this.name = new SimpleStringProperty(name);
		this.score = score;
		this.timeCompleted = timeCompleted;
		this.level = level;
	}
	
	/**
	 * Constructs a {@code PlayerScore} based on String input.
	 * @param input
	 */
	public PlayerScore(String input) {
		String[] in = input.split(Main.FILE_SUB_SEPERATOR);
		this.name1 = in[0];
		this.name = new SimpleStringProperty(name1);
		this.score = Integer.valueOf(in[1]);
		this.timeCompleted = Integer.valueOf(in[2]);
		this.level = Integer.valueOf(in[3]);
	}
	
	public String getName() {
		return name.get();
	}
	
	public String getScore() {
		return String.valueOf(score);
	}
	
	public String getTime() {
		return String.valueOf(timeCompleted);
	}
	
	public String getLevel() {
		return String.valueOf(level);
	}
	
	/**
	 * Returns the score achieved by player.
	 * 
	 * @return score
	 */
	public int getScore1() {
		return score;
	}
	
	@Override
	public int compareTo(PlayerScore p2) {
		return Integer.compare(p2.getScore1(), getScore1());
	}
	
	@Override
	public String toString() {
		return name1 + Main.FILE_SUB_SEPERATOR + 
				score + Main.FILE_SUB_SEPERATOR + 
				timeCompleted + Main.FILE_SUB_SEPERATOR + 
				level;
	}
}
