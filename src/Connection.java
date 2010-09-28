// The connection class handles a single connection to the server. This class holds the
// means of sending objects to each client as well as tightly bound userdata.

public class Connection implements Runnable, NetworkEventListener {
	Server server;
	NetworkHandler nh;
	Thread t;
	public Connection(Server server, NetworkHandler nh) {
		this.server = server;
		this.nh = nh;
		nh.addNetworkEventListener(this);

		t = new Thread(this);
		t.start();
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

	// This method is the entry point for all communication sent by the client
	public void networkEventReceived(NetworkEvent e) {
		NetworkObject o = (NetworkObject)e.object;
		o.setSender(getName());
		if (o instanceof ChatMessage) {
			server.getChatServer().receivedChatMessage(this,(ChatMessage)o);
		}
	}

	public String getName() {
		return nh.socket.getInetAddress().toString();
	}
}
