public class ChatMessage extends NetworkObject {
	private static final long serialVersionUID = -8017727634450644526L;
	String text;

	public ChatMessage(String text) {
		super();
		this.text = text;
	}

	public ChatMessage(String sender, String text) {
		super(sender);
		this.text = text;
	}

	public String toString() {
		return getSender() + ": "+text;
	}
}
