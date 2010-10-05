import java.net.Socket;

public class Client implements NetworkEventListener, Runnable {
	NetworkHandler nh;
	Thread t;
	World world;

	public static void main(String[] args) {
		String host = "localhost";
		if (args.length > 0) {
			host = args[0];
		} else {
			System.out.println("No hostname given, assuming localhost");
		}
		new Client(host,7331);
	}

	public Client(String host, int port) {
		world = null;
		
		Log.setPrimary(Log.CLIENT);
		Socket s = NetworkHandler.getClientSocket(host,port);
		if (s != null) {
			nh = new NetworkHandler(s);
			nh.addNetworkEventListener(this);
		} else {
			nh = null;
		}
			
		Log.p.out("Starting client thread");
		run();
	}

	public void run() {
		Log.p.out("Waiting for world...");
		while(world == null) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				Log.p.error("Interrupted sleep",e);
			}
		}
		
		Window window = new Window();
		GameScreen screen = new GameScreen(world,nh);
		
		KeyboardHandler kb = new KeyboardHandler();
		kb.addKeyListener(screen);

		TickTimer tick = new TickTimer();
		while(!window.shouldExit()) {
			long delta = tick.tick();
			
			screen.update(delta);
			window.doRender(screen);
			
			kb.handle();
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				
			}
		}
		window.cleanup();
		if (nh != null) nh.close();
		
		Log.p.out("Exiting cleanly");
		System.exit(0);

		
//		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
//		String line;
//		try {
//			while((line = stdin.readLine()) != null) {
//				ChatMessage message = null;
//				if (line.startsWith("/")) {
//					Log.p.out("Sending chat Command");
//					message = new ChatCommand(line);
//				} else {
//					message = new ChatMessage(line);
//				}
//				nh.send(message);
//			}
//		} catch (IOException e) {
//			Log.p.error("Error reading from user",e);
//		}
	}

	public void networkEventReceived(NetworkEvent e) {
		Object o = e.object;
		if (o instanceof ChatMessage) {
			ChatMessage message = (ChatMessage)o;
			Log.p.out("Got Message: " + message.toString());
		} else if (o instanceof World) {
			world = (World)o;
		} else if (o instanceof Tank) {
			Log.p.out("GOT TANK OBJECT");
			Tank in = (Tank)o;
			Log.p.out(""+in.getPosition().toString());
//			Tank t = world.tanks.get(0);
//			t.setPosition(in.getPosition());
//			t.setVelocity(in.getVelocity());
		}
	}
}
