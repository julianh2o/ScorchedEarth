package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

import common.key.KeyboardHandler;
import common.network.ChatMessage;
import common.network.NetworkEvent;
import common.network.NetworkEventListener;
import common.network.NetworkHandler;
import common.util.Log;
import common.util.TickTimer;
import common.world.EntityUpdate;
import common.world.World;
import common.world.WorldUpdate;

public class Client implements NetworkEventListener, Runnable {
	NetworkHandler nh;
	Thread t;
	World world;

	public static void main(String[] args) throws IOException {
		Properties p = new Properties();
		File props = new File("host.properties");
			
		try {
			p.load(new FileInputStream(props));
		} catch (IOException e) {
			p.setProperty("host", "localhost");
			p.store(new FileOutputStream(props),null);
		}
		String host = p.getProperty("host");
		
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
	}

	public void networkEventReceived(NetworkEvent e) {
		Object o = e.object;
		if (o instanceof ChatMessage) {
			ChatMessage message = (ChatMessage)o;
			Log.p.out("Got Message: " + message.toString());
		} else if (o instanceof World) {
			world = (World)o;
		} else if (o instanceof EntityUpdate) {
			EntityUpdate update = (EntityUpdate)o;
			update.update(world.getEntities());
		} else if (o instanceof WorldUpdate) {
			WorldUpdate update = (WorldUpdate)o;
			update.update(world);
		}
	}
}
