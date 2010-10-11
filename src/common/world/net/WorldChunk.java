package common.world.net;

import common.world.Chunk;

public class WorldChunk extends Update {
	private static final long serialVersionUID = 7790518869048169309L;
	
	int[][] chunkData;
	float x, y;
	
	public WorldChunk(Chunk c) {
		super();
		this.chunkData = c.getChunkData();
		this.x = c.getX();
		this.y = c.getY();
	}
	
	public Chunk getChunk() {
		Chunk c = new Chunk(x,y,chunkData);
		return c;
	}
}
