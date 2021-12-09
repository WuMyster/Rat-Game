
public enum ItemType {
	POISON {
		public void draw(int x, int y, int state) {
			Main.drawPoison(x, y);
		}
	};
//	STOPSIGN {
//		public void draw(int x, int y, int state) {
//			Main.drawStopSign(x, y, state);
//		}
//	};
	
	public abstract void draw(int x, int y, int state);
}
