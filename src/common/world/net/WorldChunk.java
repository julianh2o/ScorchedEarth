package common.world.net;

import java.io.IOException;

import com.google.protobuf.ByteString;
import common.network.NetworkProto.NetworkChunk;
import common.world.Chunk;

public class WorldChunk {
	int id;
	byte[] chunkData;
	float x, y;
	
	public WorldChunk(Chunk c) {
		super();
		this.id = c.getId();
		this.chunkData = c.getChunkData();
		this.x = c.getX();
		this.y = c.getY();
	}
	
	public WorldChunk(NetworkChunk nc) throws IOException {
		setX(nc.getX());
		setY(nc.getY());
		
		ByteString s = nc.getData();
		chunkData = s.toByteArray();
	}

	public byte[] getBytes() throws IOException {
		return NetworkChunk.newBuilder()
		.setId(id)
		.setX(getX())
		.setY(getY())
		.setData(ByteString.copyFrom(chunkData)).build().toByteArray();
	}
	
	public Chunk getChunk() {
		Chunk c = new Chunk(x,y,chunkData);
		return c;
	}

	public byte[] getChunkData() {
		return chunkData;
	}

	public void setChunkData(byte[] chunkData) {
		this.chunkData = chunkData;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
