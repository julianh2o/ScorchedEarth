import org.lwjgl.input.Keyboard;

// The connection class handles a single connection to the server. This class holds the
// means of sending objects to each client as well as tightly bound userdata.

public class Connection implements Runnable, NetworkEventListener {
	private static final long NETWORK_INTERVAL = 500;
	
	private Server server;
	private NetworkHandler nh;
	private Thread t;
	private Tank tank;
	private KeyboardHandler kb;
	private long lastNetworkUpdate;
	
	public Connection(Server server, NetworkHandler nh) {
		kb = new KeyboardHandler();
		
		this.server = server;
		this.nh = nh;
		nh.addNetworkEventListener(this);
		Log.p.out("Sending world..");
		tank = server.getWorld().addTank();
		nh.send(server.getWorld());

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
	
	public void update(long ms) {
		if (kb.isDown(Keyboard.KEY_W)) {
			Vector2D dvel = new Vector2D(tank.getAngle());
			dvel = dvel.scale(3);
			tank.setVelocity(tank.getVelocity().add(Util.timeScale(dvel, ms)));
		}
		
		if (kb.isDown(Keyboard.KEY_A)) {
			tank.rotateLeft(.03);
		}
		if (kb.isDown(Keyboard.KEY_D)) {
			tank.rotateRight(.03);
		}
		
		tank.update(ms);
		
		lastNetworkUpdate += ms;
		if (lastNetworkUpdate > NETWORK_INTERVAL) {
			networkUpdate();
			lastNetworkUpdate = 0;
		}
	}
	
	public void networkUpdate() {
		if (nh == null) return;
		EntityUpdate update = new EntityUpdate(tank);
		nh.send(update);
	}

	// This method is the entry point for all communication sent by the client
	public void networkEventReceived(NetworkEvent e) {
		NetworkObject o = (NetworkObject)e.object;
		o.setSender(getName());
		if (o instanceof ChatMessage) {
			server.getChatServer().receivedChatMessage(this,(ChatMessage)o);
		} else if (o instanceof KeyEvent) {
			Log.p.out("got key event");
			kb.update((KeyEvent)o);
		} else {
			Log.p.out("Unknown Object Received: "+o);
		}
	}
	
	public boolean isClosed() {
		return nh.getSocket().isClosed();
	}

	public String getName() {
		return nh.socket.getInetAddress().toString();
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
