
public enum ItemType {
	POISON {
		public void draw(int x, int y, int state) {
			Main.drawPoison(x, y);
		}

		public void add(int x, int y, int state) {
			Main.addPoison(x, y);
		}
	},
	STOPSIGN {
		public void draw(int x, int y, int state) {
			Main.drawStopSign(x, y, state);
		}

		public void add(int x, int y, int state) {
			Main.addStopSign(x, y, state);
		}
	},
	STERILISATION {
		public void draw(int x, int y, int state) {
			Main.drawSterilise(x, y);
		}

		public void add(int x, int y, int state) {
			Main.addSterilise(x, y);
		}
	},
	BOMB {
		public void draw(int x, int y, int state) {
			Main.drawBomb(x, y, state);
		}

		@Override
		public void add(int x, int y, int state) {
			Main.addBomb(x, y, state);
		}
	},
	SEX_TO_MALE {
		public void draw(int x, int y, int state) {
			Main.drawSexToMale(x, y);
		}

		public void add(int x, int y, int state) {
			Main.addSexToMale(x, y);
		}
	},
	SEX_TO_FEMALE {
		public void draw(int x, int y, int state) {
			Main.drawSexToFemale(x, y);
		}

		public void add(int x, int y, int state) {
			Main.addSexToFemale(x, y);
		}
	},
	GAS {
		public void draw(int x, int y, int state) {
			Main.drawGas(x, y);
		}

		public void add(int x, int y, int state) {
			Main.addGas(x, y);
		}
	};
	
	public abstract void draw(int x, int y, int state);
	
	public abstract void add(int x, int y, int state);
	
	public static ItemType fromItem(Item i) {
		if (i instanceof StopSign) {
			return STOPSIGN;
		} else if (i instanceof Bomb){
			return BOMB;
		} else if (i instanceof Sterilisation) {
			return STERILISATION;
		} else if (i instanceof SexChangeToFemale) {
			return SEX_TO_FEMALE;
		} else if (i instanceof SexChangeToMale) {
			return SEX_TO_MALE;
		} else if (i instanceof Poison) {
			return POISON;
		} else if (i instanceof Gas) {
			return GAS;
		}
		return null;
	}
}
