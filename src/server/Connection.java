package server;

import client.TankController;

import common.key.KeyEvent;
import common.key.KeyboardHandler;
import common.network.ChatMessage;
import common.network.ClientMessage;
import common.network.NetworkEvent;
import common.network.NetworkEventListener;
import common.network.NetworkHandler;
import common.network.NetworkObject;
import common.util.Log;
import common.world.ClientUpdate;
import common.world.Tank;
import common.world.WorldUpdate;

// The connection class handles a single connection to the server. This class holds the
// means of sending objects to each client as well as tightly bound userdata.

public class Connection implements Runnable, NetworkEventListener {
	private Server server;
	private Thread t;
	private NetworkHandler nh;
	
	private KeyboardHandler kb;
	private TankController tc;
	private Tank tank;
	
	public Connection(Server server, NetworkHandler nh) {
		kb = new KeyboardHandler();
		this.server = server;
		this.nh = nh;
		
		nh.addNetworkEventListener(this);
		
		//tank = server.getWorld().addTank();
		//if (nh.getSocket().getInetAddress().getHostAddress().equals("75.18.227.231")) {
		//if (nh.getSocket().getInetAddress().getHostAddress().equals("127.0.0.1")) {
			//tank.setModel(4);
		//}
		
		//server.broadcastObject(new WorldUpdate(WorldUpdate.Type.NEW_TANK,tank));
		
		//tc = new TankController(tank,kb);
		//nh.send(server.getWorld());

		t = new Thread(this);
		t.start();
	}
	
	// TODO remove this if we're not using it
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void update(long ms) {
		//tc.update();
	}

	// This method is the entry point for all communication sent by the client
	public void networkEventReceived(NetworkEvent e) {
		NetworkObject o = (NetworkObject)e.object;
		o.setSender(getName());
		if (o instanceof ChatMessage) {
			server.getChatServer().receivedChatMessage(this,(ChatMessage)o);
		} else if (o instanceof KeyEvent) {
			kb.update((KeyEvent)o);
		} else if (o instanceof ClientMessage) {
			handleClientMessage((ClientMessage)o);
		} else {
			Log.p.out("Unknown Object Received: "+o);
		}
	}
	
	public void handleClientMessage(ClientMessage message) {
		switch(message.getType()) {
			case CLIENT_READY:
				//nh.send(new ClientUpdate(ClientUpdate.Type.GRANT_CONTROL,tank.getId()));
				break;
		}
	}
	
	public void finish() {
		//server.getWorld().removeEntity(tank.getId());
	}
	
	public boolean isClosed() {
		return nh.getSocket().isClosed();
	}

	public String getName() {
		return nh.getSocket().getInetAddress().toString();
	}

	public Server getServer() {
		return server;
	}

	public Tank getTank() {
		return tank;
	}

	public KeyboardHandler getKb() {
		return kb;
	}

	public NetworkHandler getNetworkHandler() {
		return nh;
	}
}
