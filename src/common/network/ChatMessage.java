package common.network;

import common.world.net.Update;

public class ChatMessage extends Update {
	private static final long serialVersionUID = -8017727634450644526L;
	
	String sender;
	String text;

	public ChatMessage(String text) {
		super();
		this.text = text;
	}

	public ChatMessage(String sender, String text) {
		super();
		this.sender = sender;
		this.text = text;
	}

	public String toString() {
		return sender + ": "+text;
	}
	
	// Get/Set
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
