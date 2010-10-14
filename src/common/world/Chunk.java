package common.world;

import client.Model;
import client.Window;

public class Chunk {
	public static int CHUNK_SIZE = 50;
	public static float TILE_SIZE = 1F;
	
	short[][] tiles;
	
	Chunk north,east,south,west;
	
	float x, y;
	
	public Chunk(float x, float y) {
		this.x = x;
		this.y = y;
		
		tiles = new short[CHUNK_SIZE][CHUNK_SIZE];
		
		for (int i=0; i<CHUNK_SIZE; i++) {
			for (int j=0; j<CHUNK_SIZE; j++) {
				short rand = (short)(Math.random()*2);
				tiles[i][j] = rand;
			}
		}
	}
	
	public Chunk(float x, float y, short[][] tiles) {
		this.tiles = tiles;
	}

	public void render(Window w) {
		for (int i=0; i<CHUNK_SIZE; i++) {
			for (int j=0; j<CHUNK_SIZE; j++) {
				renderTile(w,i,j);
			}
		}
	}
	
	public void renderTile(Window w, int i, int j) {
		Model m = w.getModel(Tile.getModel(tiles[i][j]));
		m.renderAt(w, i*TILE_SIZE, j*TILE_SIZE, 0);
	}

	public Chunk getNorth() {
		return north;
	}

	public void setNorth(Chunk north) {
		this.north = north;
	}

	public Chunk getEast() {
		return east;
	}

	public void setEast(Chunk east) {
		this.east = east;
	}

	public Chunk getSouth() {
		return south;
	}

	public void setSouth(Chunk south) {
		this.south = south;
	}

	public Chunk getWest() {
		return west;
	}

	public void setWest(Chunk west) {
		this.west = west;
	}

	public short[][] getTiles() {
		return tiles;
	}
	
	public boolean validIndex(int x, int y) {
		if (x < 0 || y < 0 || x > CHUNK_SIZE || y > CHUNK_SIZE) {
			return false;
		}
		return true;
	}

	public int getTileAt(double xx, double yy) {
		int relx = (int)((xx - x)/TILE_SIZE) + 1;
		int rely = (int)((yy - y)/TILE_SIZE) + 1;
		if (!validIndex(relx,rely)) return Tile.INVALID;
		
		return tiles[relx][rely];
	}
	
	public boolean contains(float xx, float yy) {
		if (xx < x) return false;
		if (yy < y) return false;
		if (xx > x + getWidth()) return false;
		if (yy > y + getHeight()) return false;
		return true;
	}
	
	public float getWidth() {
		return CHUNK_SIZE * TILE_SIZE;
	}
	
	public float getHeight() {
		return CHUNK_SIZE * TILE_SIZE;
	}
	
	public short[][] getChunkData() {
		return tiles;
	}

	public float getX() {
		return x;
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
}
