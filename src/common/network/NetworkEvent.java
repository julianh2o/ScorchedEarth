package common.network;

public class NetworkEvent {
	private int type;
	private byte[] data;
	
	public NetworkEvent(int type, byte[] data) {
		this.type = type;
		this.data = data;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
}
