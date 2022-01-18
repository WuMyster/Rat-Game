import javafx.scene.image.Image;

public enum ItemType {
	POISON {

		@Override
		public Image getImage(int state) {
			return Poison.IMAGE;
		}
	},
	STOPSIGN {

		@Override
		public Image getImage(int state) {
			return StopSign.getImageState(state);
		}
	},
	STERILISATION {

		@Override
		public Image getImage(int state) {
			return Sterilisation.IMAGE;
		}
	},
	BOMB {

		@Override
		public Image getImage(int state) {
			return Bomb.getImage(state);
		}
	},
	SEX_TO_MALE {

		@Override
		public Image getImage(int state) {
			return SexChangeToMale.IMAGE;
		}
	},
	SEX_TO_FEMALE {

		@Override
		public Image getImage(int state) {
			return SexChangeToFemale.IMAGE;
		}
	},
	GAS {

		@Override
		public Image getImage(int state) {
			return Gas.IMAGE;
		}
	};
	
	/**
	 * Adds and draws the item onto the board.
	 * 
	 * @param x		x position of the item
	 * @param y		y position of the item
	 * @param state	state of the item on the board
	 */
	public void add(int x, int y, int state) {
		GameGUI.addItemToMap(this, x, y, state);
	}
	
	/**
	 * Draws the item onto the board.
	 * @param x		x position of the item
	 * @param y		y position of the item
	 * @param state	state of the item on the board
	 */
	public void draw(int x, int y, int state) {
		GameGUI.drawItemToMap(this, x, y, state);
	}
	
	public abstract Image getImage(int state);
	
	/**
	 * Converts an {@code Item} to an enum of its type.
	 * 
	 * @param i	item
	 * @return	enum of the item
	 */
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
