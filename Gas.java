public class Gas {
    /**
     * Max amount of tiles gas will spread in each direction.
     */
    final private int gasMaxSpread = 3;

    /**
     * Current amount of tiles gas has spread to in each direction.
     */
    private int currentSpread = 0;

    final private int gasExpandTime = 1;

    /**
     * The max tile diameter of the gas cloud.
     */
    int gasDia = (gasMaxSpread * 2) + 1;

    int newGasDia = 1;
    int gasSpread = 0;

    int xOrigin;
    int yOrigin;

    Tile[][] board = Board.getBoard();

    public void itemAction(int x, int y) {
        xOrigin = x;
        yOrigin = y;
    }

    private void spreadGas(int x, int y) {
        newGasDia += 2;
        gasSpread += 1;
        for (int i = -(gasSpread); i <= gasSpread; i++) {
            for (int j = -(gasSpread); j <= gasSpread; j++) {
                //setpoison at (i, j)
            }
        }
    }
}
