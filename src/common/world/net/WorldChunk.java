package common.world.net;

import java.io.DataOutputStream;
import java.io.IOException;

import common.world.Chunk;

public class WorldChunk implements Update {
	private static final long serialVersionUID = 7790518869048169309L;
	
	short[][] chunkData;
	float x, y;
	
	public WorldChunk(Chunk c) {
		super();
		this.chunkData = c.getChunkData();
		this.x = c.getX();
		this.y = c.getY();
	}
	
	@Override
	public void write(DataOutputStream o) throws IOException {
		for (int i = 0; i < chunkData.length; i++) {
			for (int j = 0; j < chunkData[i].length; j++) {
				o.writeShort(chunkData[i][j]);
			}
		}
		o.writeFloat(x);
		o.writeFloat(y);
	}
	
	public Chunk getChunk() {
		Chunk c = new Chunk(x,y,chunkData);
		return c;
	}

	public short[][] getChunkData() {
		return chunkData;
	}

	public void setChunkData(short[][] chunkData) {
		this.chunkData = chunkData;
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
