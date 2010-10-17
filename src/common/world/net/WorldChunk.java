package common.world.net;

//TODO proto this class
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.protobuf.ByteString;

import common.network.NetworkProto.NetworkChunk;
import common.world.Chunk;

public class WorldChunk {
	int id;
	short[][] chunkData;
	float x, y;
	
	public WorldChunk(Chunk c) {
		super();
		this.id = c.getId();
		this.chunkData = c.getChunkData();
		this.x = c.getX();
		this.y = c.getY();
	}
	
	public WorldChunk(NetworkChunk nc) {
		setX(nc.getX());
		setY(nc.getY());
		
		//TODO parse chunk data from bytes
	}

	public byte[] getChunkDataBytes() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream o = new DataOutputStream(baos);
		for (int i = 0; i < chunkData.length; i++) {
			for (int j = 0; j < chunkData[i].length; j++) {
				o.writeShort(chunkData[i][j]);
			}
		}
		return baos.toByteArray();
	}
	
	public byte[] getBytes() throws IOException {
		return NetworkChunk.newBuilder()
		.setId(id)
		.setX(getX())
		.setY(getY())
		.setData(ByteString.copyFrom(getChunkDataBytes())).build().toByteArray();
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
