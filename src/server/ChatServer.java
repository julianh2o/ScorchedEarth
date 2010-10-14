package server;

import common.network.ChatCommand;
import common.network.ChatMessage;
import common.util.Log;

public class ChatServer {
	Server server;
	public ChatServer(Server server) {
		this.server = server;
	}
	
	public void receivedChatMessage(Connection source, ChatMessage message) {
		//TODO fix
//		if (message instanceof ChatCommand) {
//			String response = "";
//			response += "Connected Users:";
//			for (Connection ch : server.getConnections()) {
//				response += "IP: "+ch.getNetworkHandler().getSocket().getInetAddress().toString();
//			}
//			source.getNetworkHandler().send(new ChatMessage("Server",response));
//		} else {
//			Log.p.out("Chatserver got message: "+message.toString());
//			server.broadcastObject(message);
//		}
	}
}
