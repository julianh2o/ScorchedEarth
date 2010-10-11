package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

import org.lwjgl.input.Keyboard;

import common.key.KeyEvent;
import common.key.KeyListener;
import common.key.KeyboardHandler;
import common.network.ChatMessage;
import common.network.ClientReady;
import common.network.NetworkEvent;
import common.network.NetworkEventListener;
import common.network.NetworkHandler;
import common.util.Log;
import common.util.TickTimer;
import common.world.Entity;
import common.world.GameWorld;
import common.world.Tank;
import common.world.net.EntityUpdate;
import common.world.net.GrantTankControl;
import common.world.net.NewTank;
import common.world.net.WorldChunk;

public class Client implements KeyListener, NetworkEventListener, Runnable {
	private NetworkHandler nh;
	private GameWorld world;
	private Window window;
	private KeyboardHandler kb;
	private Screen screen;
	boolean halt;

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
		Log.p.out("Waiting for chunks...");
		halt = false;
		
		while(world == null && !halt) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				Log.p.error("Interrupted sleep",e);
			}
		}
		
		window = new Window();
		kb = new KeyboardHandler();
		kb.addKeyListener(this);
		screen = new GameScreen(world,nh,kb);
		
		nh.send(new ClientReady());

		TickTimer tick = new TickTimer();
		
		int TICK_TIME = (int)(1000F/60F);
		long remain = 0;
		//TODO Clean this up
		while(!(window.shouldExit() || halt || nh.getSocket().isClosed())) {
			long ms = tick.tick();
			remain += ms;
			
			while(remain > TICK_TIME && !halt) {
				screen.update();
				window.doRender(screen);
				remain -= TICK_TIME;
				
				kb.handle();
			}
			
			
			try {
				Thread.sleep(17);
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
		} else if (o instanceof GameWorld) {
			world = (GameWorld)o;
		} else if (o instanceof EntityUpdate) {
			EntityUpdate update = (EntityUpdate)o;
			Entity entity = world.findEntity(update.getId());
			if (entity != null) update.update(entity);
		} else if (o instanceof NewTank) {
			Log.p.out("adding new tank");
			NewTank update = (NewTank)o;
			Tank t = world.addTank(update.getId());
			t.setModel(update.getModel());
			if (update.isControl()) {
				Log.p.out("Controlling new tank");
				((GameScreen)screen).controlTank(update.getId());
			}
		} else if (o instanceof GrantTankControl) {
			GrantTankControl update = (GrantTankControl)o;
			if (screen instanceof GameScreen) {
				((GameScreen)screen).controlTank(update.getId());
			}
		} else if (o instanceof WorldChunk) {
			WorldChunk update = (WorldChunk)o;
			if (world == null) world = new GameWorld();
			world.addChunk(update.getChunk());
		}
	}

	public NetworkHandler getNetworkHandler() {
		return nh;
	}

	public GameWorld getWorld() {
		return world;
	}

	public Window getWindow() {
		return window;
	}

	public KeyboardHandler getKeyboardHandler() {
		return kb;
	}

	public Screen getScreen() {
		return screen;
	}

	public void keyPressed(KeyEvent k) {
		if (k.getKey() == Keyboard.KEY_ESCAPE) {
			halt = true;
		}
	}

	public void keyReleased(KeyEvent k) {
	}
}
