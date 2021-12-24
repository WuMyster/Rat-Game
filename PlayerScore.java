
/**
 * A class for games played containing information about the player and the
 * game they played.
 * 
 * @author Jing
 *
 */
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
	 * Returns name of player.
	 * @return name of player
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns score achieved by player.
	 * @return score achieved by player
	 */
	public Integer getScore() {
		return score;
	}
	
	/**
	 * Returns time taken by player to finish game.
	 * @return time taken by player to finish game
	 */
	public Integer getTime() {
		return timeCompleted;
	}
	
	/**
	 * Returns level achieved for points earned.
	 * @return level achieved for points earned
	 */
	public Integer getLevel() {
		return level;
	}
	
	@Override
	public int compareTo(PlayerScore p2) {
		return Integer.compare(p2.getScore(), getScore());
	}
}
