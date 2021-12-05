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
    final private int damage = 20;

    /**
     * Max amount of tiles gas spread to in each direction.
     */
    final private int gasMaxSpread = 2;

    /**
     * The max tile diameter of the gas cloud.
     */
    int gasDia = (gasMaxSpread * 2) + 1;

    int newGasDia = 1;
    int gasSpread = 0;


    Tile[][] board = Board.getBoard();

    public void add(int x, int y) {
        new Thread(() -> {
            for (int i = 0; i < gasMaxSpread; i++) {
                spreadGas(x, y);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {}
            }
        }).start();

        //wait a bit and then remove items
    }

    public void itemAction(Rat rat) {
       // Ratcontroller.damageRat(damage)
    }

    private void spreadGas(int x, int y) {
        newGasDia += 2;
        gasSpread += 1;
        for (int i = -(gasSpread); i <= gasSpread; i++) {
            for (int j = -(gasSpread); j <= gasSpread; j++) {
                Tile t = board[y * Board.getExtraPadding()][x * Board.getExtraPadding()];
                Poison gas = new Poison();

                if (Board.isItemPlaceable(x - i, y - j)) {
                    t.setTileItem(gas, x - i, y - j);
                    Main.addGasPlace(x- i, y- j);
                }
                System.out.print("Gas" + i);
                System.out.println(j);
            }
        }
    }

	@Override
	public ArrayList<Rat> itemAction(ArrayList<Rat> r) {
		// Should damage all rats!
		return null;
	}
}
