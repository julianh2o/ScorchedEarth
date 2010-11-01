package client;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
import common.util.Log;
import common.world.Entity;
import common.world.GameWorld;
import common.world.behavior.ControlledTankBehavior;
import common.world.net.WorldChunk;

public class GameScreen implements Screen, KeyListener, MouseListener, NetworkEventListener {
	private View view;
	private NetworkHandler nh;
	private GameWorld world;
	
	private Entity tank;
	
	private KeyboardHandler kb;
	private MouseHandler mh;
	
	ReadWriteLock eventQueueLock;
	Queue<NetworkEvent> unhandledEvents;
	public GameScreen(GameWorld world, NetworkHandler nh, KeyboardHandler kb, MouseHandler mh, View view) {
		eventQueueLock = new ReentrantReadWriteLock();
		unhandledEvents = new LinkedList<NetworkEvent>();
		
		this.nh = nh;
		this.view = view;
		
		if (world != null) {
			this.world = world;
		} else {
			this.world = new GameWorld();
		}
		
		this.kb = kb;
		this.mh = mh;
		
		nh.addNetworkEventListener(this);
		kb.addKeyListener(this);
		mh.addMouseListener(this);
	}

	public void enter() {
		//TODO request world
	}
	
	public void controlTank(int id) {
		Log.p.out("Trying to control: "+id);
		Entity tank = world.findEntity(id);
		if (tank == null) {
			Log.p.out("ERORR: TANK NOT FOUND");
			return;
		}
		this.setTank(tank);
		tank.setBehavior(new ControlledTankBehavior(this));
		Log.p.out("Controlling tank: "+id);
	}

	public void update() {
		if (tank != null) {
			view.centerOn(tank.getX(), tank.getY());
		}
		
		handleAllEvents();
		
		if (world == null) return;
		world.update();
	}
	
	public void networkEventReceived(NetworkEvent e) {
		eventQueueLock.writeLock().lock();
		unhandledEvents.offer(e);
		eventQueueLock.writeLock().unlock();
	}
	
	public void handleAllEvents() {
		NetworkEvent event;
		eventQueueLock.writeLock().lock();
		while(true) {
			if (unhandledEvents.isEmpty()) {
				eventQueueLock.writeLock().unlock();
				return;
			}
			
			event = unhandledEvents.poll();
			
			handleNetworkEvent(event);
		}
	}
	
	public void handleNetworkEvent(NetworkEvent e) {
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
			if (entity == null) {
				entity = world.newEntity(ne.getType(), ne.getX(), ne.getY(), ne.getId());
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
				controlTank(nm.getData(0).getInt());
			break;
		}
	}
	
	public void networkUpdate() {
		if (tank != null) nh.send(NetworkHandler.ENTITY_UPDATE, tank.getBytes());
	}
	
	public void render(Window w) {
		glClear(GL_COLOR_BUFFER_BIT);
		
		handleCamera(w);
		world.render(w);
		
//		float x = view.screenXtoGame(mh.getX());
//		float y = view.screenYtoGame(mh.getY());
//		Renderer.renderLine(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
//		Renderer.renderLine(view.getRight(),view.getTop(),view.getLeft(),view.getBottom());
//		Renderer.renderVector(new Vector2f(x,y));
	}
	
	public void handleCamera(Window w) {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluOrtho2D(view.getLeft(), view.getRight(), view.getBottom(), view.getTop());
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glViewport(0, 0, w.mode.getWidth(), w.mode.getHeight());
	}

	public void leave() {
		//TODO send leave message
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void setTank(Entity tank) {
		this.tank = tank;
	}

	public Entity getTank() {
		return tank;
	}

	public View getView() {
		return view;
	}

	public NetworkHandler nh() {
		return nh;
	}

	public GameWorld getWorld() {
		return world;
	}

	public KeyboardHandler kb() {
		return kb;
	}

	public MouseHandler mh() {
		return mh;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseWheel(MouseEvent e) {
		if (e.wheel < 0) {
			view.zoom *= .8;
		} else {
			view.zoom *= 1.2;
		}
	}

	public float getMouseX() {
		return view.screenXtoGame(mh.getX());
	}
	
	public float getMouseY() {
		return view.screenYtoGame(mh.getY());
	}
}
