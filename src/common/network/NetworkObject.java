package common.network;

import java.io.Serializable;

public class NetworkObject implements Serializable {
	private static final long serialVersionUID = -5243527754777553364L;
	String sender;

	public NetworkObject() {

	}

	public NetworkObject(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
}
