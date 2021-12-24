
public class PlayerScore implements Comparable<PlayerScore> {

	private final String name;
	private final int score;
	private final int timeCompleted;
	private final int level;
	
	public PlayerScore(String name, int score, int timeCompleted, int level) {
		this.name = name;
		this.score = score;
		this.timeCompleted = timeCompleted;
		this.level = level;
	}
	
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
