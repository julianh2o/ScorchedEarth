package common.network;

public class ClientMessage extends NetworkObject {
	private static final long serialVersionUID = 8764396787591760074L;
	
	public enum Type {
		CLIENT_READY;
	}
	
	private Type type;

	public ClientMessage(Type type) {
		this.setType(type);
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}
}
