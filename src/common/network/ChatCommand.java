package common.network;

public class ChatCommand extends ChatMessage {
	private static final long serialVersionUID = 1206188439832292526L;

	public ChatCommand(String text) {
		super(text);
		this.text = text.substring(1);
	}

	public String getCommand() {
		return getArguments()[0];
	}

	public String[] getArguments() {
		String[] split = this.text.split(" ");
		return split;
	}
}
