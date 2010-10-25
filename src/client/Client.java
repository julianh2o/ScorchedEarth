package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

import org.lwjgl.input.Keyboard;

import com.google.protobuf.InvalidProtocolBufferException;
import common.input.KeyEvent;
import common.input.KeyListener;
import common.input.KeyboardHandler;
import common.input.MouseEvent;
import common.input.MouseHandler;
import common.input.MouseListener;
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

public class Client implements MouseListener, KeyListener, NetworkEventListener, Runnable {
	private static final long NETWORK_INTERVAL = 50;
	
	private NetworkHandler nh;
	private GameWorld world;
	private Window window;
	private KeyboardHandler kb;
	private MouseHandler mh;
	private GameScreen screen;
	boolean halt;
	
	private long lastNetworkUpdate;

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
		
		Log.setPrimary(Log.CLIENT);
		new Client(host,7331);
	}

	public Client(String host, int port) {
		world = new GameWorld();
		
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
		Log.p.out(window.getView().toString());
		
		kb = new KeyboardHandler();
		mh = new MouseHandler(window.getView());
		kb.addKeyListener(this);
		mh.addMouseListener(this);
		
		screen = new GameScreen(world,nh,kb,mh);
		
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
				
				lastNetworkUpdate += ms;
				if (lastNetworkUpdate > NETWORK_INTERVAL) {
					networkUpdate();
					lastNetworkUpdate = 0;
				}
				
				kb.handle();
				mh.handle();
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

	private void networkUpdate() {
		if (screen.getTank() != null) nh.send(NetworkHandler.ENTITY_UPDATE, screen.getTank().getBytes());
	}

	public void networkEventReceived(NetworkEvent e) {
		int type = e.getType();
		switch(type) {
		case NetworkHandler.CHUNK:
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
			//Log.p.out("got entity update:"+ne.getId());
			if (entity == null) {
				entity = world.newEntity(Entity.Type.values()[ne.getType()], ne.getX(), ne.getY(), ne.getId());
			}
			entity.updateWith(ne);
			break;
		case NetworkHandler.MESSAGE:
			handleNetworkMessage(e);
			break;
		}
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
				((GameScreen)screen).controlTank(nm.getTarget());
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

	@Override
	public void mouseMoved(MouseEvent e) {
//		Log.p.out("x:"+e.x);
//		Log.p.out("y:"+e.y);
//		Log.p.out("w"+e.wheel);
//		Log.p.out("b:"+e.button);
//		Log.p.out("state:"+(e.state?"true":"false"));
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseWheel(MouseEvent e) {
	}
}
