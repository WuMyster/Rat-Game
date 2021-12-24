
public class PlayerScore implements Comparable<PlayerScore> {

	/**
	 * Name of player.
	 */
	private final String name;
	
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
		this.name = name;
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
		this.name = in[0];
		this.score = Integer.valueOf(in[1]);
		this.timeCompleted = Integer.valueOf(in[2]);
		this.level = Integer.valueOf(in[3]);
	}
	
	/**
	 * Returns the score achieved by player.
	 * 
	 * @return score
	 */
	public int getScore() {
		return score;
	}
	
	@Override
	public int compareTo(PlayerScore p2) {
		return Integer.compare(getScore(), p2.getScore());
	}
	
	@Override
	public String toString() {
		return name + Main.FILE_SUB_SEPERATOR + 
				score + Main.FILE_SUB_SEPERATOR + 
				timeCompleted + Main.FILE_SUB_SEPERATOR + 
				level;
	}
}
