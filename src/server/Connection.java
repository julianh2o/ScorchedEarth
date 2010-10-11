package server;


import common.key.KeyEvent;
import common.key.KeyboardHandler;
import common.key.TankController;
import common.network.ChatMessage;
import common.network.ClientReady;
import common.network.NetworkEvent;
import common.network.NetworkEventListener;
import common.network.NetworkHandler;
import common.util.Log;
import common.world.Chunk;
import common.world.Tank;
import common.world.net.NewTank;
import common.world.net.WorldChunk;

// The connection class handles a single connection to the server. This class holds the
// means of sending objects to each client as well as tightly bound userdata.

public class Connection implements NetworkEventListener {
	private Server server;
	private NetworkHandler nh;
	
	private KeyboardHandler kb;
	private TankController tc;
	private Tank tank;
	
	public Connection(Server server, NetworkHandler nh) {
		kb = new KeyboardHandler();
		this.server = server;
		this.nh = nh;
		
		nh.addNetworkEventListener(this);
		
		tank = server.getWorld().addTank(-1);
		//if (nh.getSocket().getInetAddress().getHostAddress().equals("75.18.227.231")) {
		//if (nh.getSocket().getInetAddress().getHostAddress().equals("127.0.0.1")) {
			//tank.setModel(4);
		//}
		
		tc = new TankController(tank,kb);
		
		
		//TODO save and restore a logged-out tank's position.. (implement user persistence lol)
		Chunk c = server.getWorld().getChunk(5F, 5F);
		nh.send(new WorldChunk(c));
	}
	
	public void update() {
		if (tc != null) tc.update();
	}

	// This method is the entry point for all communication sent by the client
	public void networkEventReceived(NetworkEvent e) {
		Object o = (Object)e.object;
		if (o instanceof ChatMessage) {
			server.getChatServer().receivedChatMessage(this,(ChatMessage)o);
		} else if (o instanceof KeyEvent) {
			kb.update((KeyEvent)o);
		} else if (o instanceof ClientReady) {
			Log.p.out("broadcasting tank");
			NewTank nt = new NewTank(tank.getId(),tank.getModel(),false);
			server.broadcastExcept(this,nt);
			nt.setControl(true);
			nh.send(nt);
			
			
			for (Connection conn : server.getConnections()) {
				if (conn != this && conn.tank != null) {
					nh.send(new NewTank(conn.tank.getId(),conn.tank.getModel(),false));
				}
			}
		} else {
			Log.p.out("Unknown Object Received: "+o);
		}
	}
	
	public void finish() {
		server.getWorld().removeEntity(tank.getId());
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
