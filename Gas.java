import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;

public class Gas extends Item {
    /*
    Set on tile
    itemAction()
    have a radius
    one step, set tiles in all directions once
    when its repeated for radius number, stop and disappear after a while
     */

    /**
     * Damage gas does to a rat.
     */
    private final int DAMAGE = 100;

    /**
     * Max amount of tiles gas spread to in each direction.
     */
    private final int gasMaxRadius = 3;

    private static final int GAS_EXPAND_TIME = 700; // milliseconds

    public static int getGAS_EXPAND_TIME() {
        return GAS_EXPAND_TIME;
    }

    public int getGasMaxRadius() {
        return gasMaxRadius;
    }

    /**
     * The max tile diameter of the gas cloud.
     */
    int gasDia = (gasMaxRadius * 2) + 1;

    int newGasDia = 1;
    int gasSpread = 0;

    public int getGasSpread() {
        return gasSpread;
    }



    Tile[][] board = Board.getBoard();


    public void add(int x, int y) {
        new Thread(() -> {
            for (int i = 0; i < gasMaxRadius; i++) {
                spreadGas(x, y);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {}
            }
        }).start();

        //wait a bit and then remove items
    }

    public void gasCloud(Item item, int x, int y, int r, int gasSpread) {
        for (int i = -(gasSpread); i <= gasSpread; i++) {
            for (int j = -(gasSpread); j <= gasSpread; j++) {
                if (Board.isItemPlaceable(x - i, y - j)) {
                    Tile t = Board.getTile((x-i), (y-j));
                    //Tile t = board[(y-j) * EXTRA_PADDING][(x-i) * EXTRA_PADDING];

                    if (item instanceof Gas) {
                        t.setTileItem(new Gas(), x, y);
                        // Main.addGasPlace(x-i, y-j);
                        System.out.print((i));
                        System.out.println(j);
                    }




                }
            }
        }
    }

    public void spreadGasCloud(Gas gas, int x, int y, int r) {
        new Thread(() -> {
            for (int i = 0; i < r; i++) {
                //System.out.println(gasSpread);
                gasCloud(new Gas(), x, y, 2, gasSpread++);
                try {
                    Thread.sleep(Gas.getGAS_EXPAND_TIME());
                } catch (InterruptedException ex) {}
            }

            for (int i = r; i > 0; i--) {
                Main.getGasPlace().clear();
                //System.out.println(gasSpread);
                gasCloud(null, x, y, 2, gasSpread--);
                try {
                    Thread.sleep(Gas.getGAS_EXPAND_TIME());
                } catch (InterruptedException ex) {}
            }

        }).start();
    }





















    public void itemAction(Rat rat) {
       // Ratcontroller.damageRat(damage)
    }

    private void spreadGas(int x, int y) {
        gasSpread += 1;
        for (int i = -(gasSpread); i <= gasSpread; i++) {
            for (int j = -(gasSpread); j <= gasSpread; j++) {
                Tile t = board[y * Board.getExtraPadding()][x * Board.getExtraPadding()];
                Poison gas = new Poison();

                if (Board.isItemPlaceable(x - i, y - j)) {
                    t.setTileItem(gas, x - i, y - j);
                    Main.addGasPlace(x- i, y- j);
                }
                //System.out.print("Gas" + i);
                //System.out.println(j);
            }
        }
    }

	@Override
	public ArrayList<Rat> itemAction(ArrayList<Rat> r) {
		if (!r.isEmpty()) {
            for (int i = 0; i < r.size(); i++) {
                r.get(i).damageRat(DAMAGE);
            }
        }
		return r;
	}
	
	@Override
	public String toString() {
		String out = "Gas deprecated";
		return out;
	}
}
