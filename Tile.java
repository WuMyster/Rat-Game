
public class Tile {

	private float x, y;
	private int width, height;
	private Texture texture; //If the tiles will have a sort of texture imported later.
	private TileType type; //If we're going to use a TileType Class.
	private boolean occupied;
	
	public Tile(float x, float y, int width, int height, TileType type) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
		this.texture = QuickLoad(type.textureName); //This can be also taken out if we're not importing Textures.
		if (type.buildable)
			occupied = false;
		else
			occupied = true;
	}
	
	public void draw() {
		DrawQuadTex(texture, x, y, width, height); //Same as above. Everything that contains texture can be removed.
	}

	public float getX() {
		return x;
	}
	
	public int getXPlace() {
		return (int) x / TILE_SIZE;
	}
	
	public int getYPlace() {
		return (int) y / TILE_SIZE;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public TileType getType() {
		return type;
	}

	public void setType(TileType type) {
		this.type = type;
	}
	
	public boolean getOccupied() {
		return occupied;
	}
	
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
}
