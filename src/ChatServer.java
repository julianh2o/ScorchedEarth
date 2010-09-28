public class ChatServer {
	Server server;
	public ChatServer(Server server) {
		this.server = server;
	}
	
	public void receivedChatMessage(Connection source, ChatMessage message) {
		Log.p.out("GOT A CHAT MESSAGE IN THE message server");
		if (message instanceof ChatCommand) {
			String response = "";
			response += "Connected Users:";
			for (Connection ch : server.getConnections()) {
				response += "IP: "+ch.nh.socket.getInetAddress().toString();
			}
			source.nh.send(new ChatMessage("Server",response));
		} else {
			Log.p.out(message.toString());
			server.broadcastObject(new ChatMessage(source.getName(),message.text));
		}
	}
}
