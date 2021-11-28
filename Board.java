public class Board {

	public static final int WIDTH = 1270, HEIGHT = 960;
	public static final int TILE_SIZE = 64;
	public Tile[][] map;
	private int tilesWide, tilesHigh;


	//So there were some methods on the CRC Cards and UML that I thought they might not fit in the board.
	//So these methods can be probably taken out and placed on other functions.
	//But I'm not entirely sure of course.

	public Board() {
		this.tilesWide = 20;
		this.tilesHigh = 15;
		map = new Tile[tilesWide][tilesHigh];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = new Tile(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE, TileType.Grass); //You can pretty much change it to whatever graphic tile we want.
			}
		}
	}

	public Board(int[][] newMap) {
		this.tilesWide = newMap[0].length;
		this.tilesHigh = newMap.length;
		map = new Tile[tilesWide][tilesHigh];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				switch (newMap[j][i]) {
				case 0:
					map[i][j] = new Tile(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE, TileType.Dirt);  //TileType Function can also be named as Environmental Tile or Simply any other type it includes.
					break;
				case 1:
					map[i][j] = new Tile(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE, TileType.Water);
					break;
				case 2:
					map[i][j] = new Tile(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE, TileType.Grass);
					break;
				}
			}
		}
	}

	public void setTile(int xCoord, int yCoord, TileType type) {
		map[xCoord][yCoord] = new Tile(xCoord * TILE_SIZE, yCoord * TILE_SIZE, TILE_SIZE, TILE_SIZE, type);
	}

	public Tile getTile(int xPlace, int yPlace) {
		if (xPlace < tilesWide && yPlace < tilesHigh && xPlace > -1 && yPlace > -1)
			return map[xPlace][yPlace];
		else
			return new Tile(0, 0, 0, 0, TileType.NULL);
	}


	public void draw() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j].draw();
			}
		}
	}

	public int getTilesWide() {
		return tilesWide;
	}

	public void setTilesWide(int tilesWide) {
		this.tilesWide = tilesWide;
	}

	public int getTilesHigh() {
		return tilesHigh;
	}

	public void setTilesHigh(int tilesHigh) {
		this.tilesHigh = tilesHigh;
	}
}