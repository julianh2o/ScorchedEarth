package common.world;

import java.io.Serializable;

import common.network.NetworkObject;

import client.Window;

public class Chunk extends NetworkObject implements Serializable {
	private static final long serialVersionUID = 2232483208271116535L;
	
	public static int CHUNK_SIZE = 50;
	public static int TILE_SIZE = 16;
	
	Tile[][] tiles;
	
	Chunk north,east,south,west;
	
	int x,y;
	
	public Chunk(int x, int y) {
		this.x = x;
		this.y = y;
		tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
		
		for (int i=0; i<CHUNK_SIZE; i++) {
			for (int j=0; j<CHUNK_SIZE; j++) {
				int rand = (int)(Math.random()*2);
				tiles[i][j] = new Tile(Tile.types[rand]);
			}
		}
		
		for (int i=0; i<20; i++) {
			int xx = (int)(Math.random()*CHUNK_SIZE);
			int yy = (int)(Math.random()*CHUNK_SIZE);
			int len = (int)(Math.random()*20);
			for (int l=0; l<len; l++) {
				xx += (int)(Math.random()*3 - 1);
				yy += (int)(Math.random()*3 - 1);
				
				if (xx < 0 || xx > CHUNK_SIZE-1 || yy < 0 || yy > CHUNK_SIZE-1) continue;
				tiles[xx][yy] = new Tile(Tile.Type.BLOCK);
			}
		}
	}
	
	public void render(Window w) {
		for (int i=0; i<CHUNK_SIZE; i++) {
			for (int j=0; j<CHUNK_SIZE; j++) {
				renderTile(w,i,j);
			}
		}
	}
	
	public void renderTile(Window w, int i, int j) {
		Tile t = tiles[i][j];
		w.getModel(t.getModel()).renderAt(w, i*TILE_SIZE, j*TILE_SIZE, 0);
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

	public Tile[][] getTiles() {
		return tiles;
	}
}
