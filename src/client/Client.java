package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

import org.lwjgl.input.Keyboard;

import common.ResourceManager;
import common.input.KeyEvent;
import common.input.KeyListener;
import common.input.KeyboardHandler;
import common.input.MouseEvent;
import common.input.MouseHandler;
import common.input.MouseListener;
import common.network.NetworkEvent;
import common.network.NetworkEventListener;
import common.network.NetworkHandler;
import common.network.NetworkProto.NetworkMessage;
import common.network.NetworkProto.NetworkMessage.Type;
import common.util.Log;
import common.util.TickTimer;

public class Client implements MouseListener, KeyListener, NetworkEventListener, Runnable {
	private static final long NETWORK_INTERVAL = 50;
	
	private NetworkHandler nh;
	private Window window;
	private KeyboardHandler kb;
	private MouseHandler mh;
	private GameScreen screen;
	boolean halt;
	
	private long lastNetworkUpdate;

	public static void main(String[] args) throws IOException {
		Log.setPrimary(Log.CLIENT);
		
		try {
			OSUtil.setLibraryPath();
		} catch (Exception e) {
			Log.p.out("Unable to set native library path! (Try running me in a different way)");
			System.exit(1);
		}
		
		Properties p = new Properties();
		File props = new File("host.properties");
			
		try {
			p.load(new FileInputStream(props));
		} catch (IOException e) {
			p.setProperty("host", "titancolony.net");
			p.store(new FileOutputStream(props),null);
		}
		String host = p.getProperty("host");
		
		new Client(host,7331);
	}

	public Client(String host, int port) {
		ResourceManager.getInstance();
		
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
	
	public boolean clientShouldExit() {
		return window.shouldExit() || halt || nh.getSocket().isClosed();
	}

	public void run() {
		halt = false;
		
		window = new Window();
		kb = new KeyboardHandler();
		mh = new MouseHandler();
		
		kb.addKeyListener(this);
		mh.addMouseListener(this);
		
		screen = new GameScreen(null,nh,kb,mh,new View(window));
		nh.send(NetworkHandler.MESSAGE,NetworkMessage.newBuilder().setType(Type.CLIENT_READY).build().toByteArray());

		TickTimer tick = new TickTimer();
		int TICK_TIME = (int)(1000F/60F);
		long remain = 0;
		
		while(!clientShouldExit()) {
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
				nh.update();
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
	
	public void networkUpdate() {
		if (screen != null) screen.networkUpdate();
	}
	
	@Override
	public void networkEventReceived(NetworkEvent e) {
	}
	
	public NetworkHandler getNetworkHandler() {
		return nh;
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

	@Override
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
