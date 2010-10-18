package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

import org.lwjgl.input.Keyboard;

import com.google.protobuf.InvalidProtocolBufferException;
import common.key.KeyEvent;
import common.key.KeyListener;
import common.key.KeyboardHandler;
import common.network.NetworkEvent;
import common.network.NetworkEventListener;
import common.network.NetworkHandler;
import common.network.NetworkProto.NetworkChunk;
import common.network.NetworkProto.NetworkEntity;
import common.network.NetworkProto.NetworkMessage;
import common.network.NetworkProto.NetworkMessage.Type;
import common.util.Log;
import common.util.TickTimer;
import common.world.Entity;
import common.world.GameWorld;
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
		world = new GameWorld();
		
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
		
		nh.send(NetworkHandler.MESSAGE,NetworkMessage.newBuilder().setType(Type.CLIENT_READY).build().toByteArray());

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
		int type = e.getType();
		Log.p.out("Client has event: "+type);
		switch(type) {
		case NetworkHandler.CHUNK:
			Log.p.out("Hey yo, i gots a chunk");
			try {
				NetworkChunk nc = NetworkChunk.parseFrom(e.getData());
				WorldChunk wc = new WorldChunk(nc);
				world.addChunk(wc.getChunk());
			} catch (Exception e2) {
				//IO or protobuf
				e2.printStackTrace();
			}
			break;
		case NetworkHandler.ENTITY_UPDATE:
			NetworkEntity ne;
			try {
				ne = NetworkEntity.parseFrom(e.getData());
			} catch (InvalidProtocolBufferException e1) {
				e1.printStackTrace();
				return;
			}
			Entity entity = world.findEntity(ne.getId());
			if (entity == null) {
				Log.p.out("adding entity");
				entity = world.newEntity(Entity.Type.values()[ne.getType()], 0, 0, ne.getId());
			}
			//entity.updateWith(ne);
			break;
		case NetworkHandler.MESSAGE:
			handleNetworkMessage(e);
			break;
		}
		
//		} else if (o instanceof WorldChunk) {
//			WorldChunk update = (WorldChunk)o;
//			if (world == null) world = new GameWorld();
//			world.addChunk(update.getChunk());
//		}
	}

	private void handleNetworkMessage(NetworkEvent e) {
		NetworkMessage nm = null;
		try {
			nm = NetworkMessage.parseFrom(e.getData());
		} catch (InvalidProtocolBufferException e1) {
			e1.printStackTrace();
			return;
		}
		
		switch(nm.getType()) {
		case GRANT_CONTROL:
			if (screen instanceof GameScreen) {
				((GameScreen)screen).controlTank(nm.getTarget());
			}
			break;
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
