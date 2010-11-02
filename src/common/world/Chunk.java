package common.world;

import java.util.HashMap;
import java.util.Random;

import common.ResourceManager;
import common.util.Log;

import client.Model;
import client.Window;

public class Chunk {
	public static int CHUNK_SIZE = 50;
	public static float TILE_SIZE = 1F;
	
	public static HashMap<Byte,String> blockModels = new HashMap<Byte,String>();
	static {
		blockModels.put((byte)0, "dirt.model");;
		blockModels.put((byte)1, "grass.model");
		blockModels.put((byte)2, "sand.model");
	}
	
	int id;
	byte[] tiles;
	
	Chunk north,east,south,west;
	
	float x, y;
	
	public Chunk(float x, float y) {
		this.id = 0;
		this.x = x;
		this.y = y;
		
		tiles = new byte[getChunkLength()];
		
		generateChunk();
	}
	
	public void generateChunk() {
		Random rng = new Random();
		for (int i=0; i<getChunkLength(); i++) tiles[i] = 0;
		
		for (byte type = 1; type <= 2; type++) {
			for (int i=0; i<20; i++) {
				int x = rng.nextInt(CHUNK_SIZE);
				int y = rng.nextInt(CHUNK_SIZE);
				int size = 5 + rng.nextInt(25 - i);
				generateSplotch(rng,x,y,size,type);
			}
		}
	}
	
	public void generateSplotch(Random rng, int x, int y, int size, byte type) {
		if (size <= 0) return;
		if (!validIndex(x,y)) return;
		if (tiles[getIndex(x,y)] == type) return;
		
		tiles[getIndex(x,y)] = type;
		
		for (int i = -1; i<=1; i++) {
			for (int j = -1; j<=1; j++) {
				int dx = i;
				int dy = j;
				
				int newx = x + dx;
				int newy = y + dy;
				
				int newsize = size - rng.nextInt(5);
				
				generateSplotch(rng,newx,newy,newsize,type);
			}
		}
	}
	
	public int getChunkLength() {
		return CHUNK_SIZE*CHUNK_SIZE;
	}
	
	public int getIndex(int x, int y) {
		return x + CHUNK_SIZE*y;
	}
	
	public int[] getCoords(int i) {
		int x = i % CHUNK_SIZE;
		int y = i / CHUNK_SIZE; 
		return new int[] {x,y};
	}
	
	public Chunk(float x, float y, byte[] tiles) {
		this.tiles = tiles;
	}

	public void render(Window w) {
		for (int i=0; i<CHUNK_SIZE; i++) {
			for (int j=0; j<CHUNK_SIZE; j++) {
				renderTile(w,i,j);
			}
		}
	}
	
	public void renderTile(Window w, int x, int y) {
		byte tileValue = tiles[getIndex(x,y)];
		String model = blockModels.get(tileValue);
		Model m = ResourceManager.getInstance().getModel(model);
		m.renderAt(w, x*TILE_SIZE, y*TILE_SIZE, 0,0);
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

	public byte[] getTiles() {
		return tiles;
	}
	
	public boolean validIndex(int x, int y) {
		if (x < 0 || y < 0 || x >= CHUNK_SIZE || y >= CHUNK_SIZE) {
			return false;
		}
		return true;
	}

	public int getTileAt(double xx, double yy) {
		int relx = (int)((xx - x)/TILE_SIZE) + 1;
		int rely = (int)((yy - y)/TILE_SIZE) + 1;
		if (!validIndex(relx,rely)) return Tile.INVALID;
		
		return tiles[getIndex(relx,rely)];
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
	
	public byte[] getChunkData() {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
