package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import common.network.NetworkHandler;
import common.util.Log;
import common.util.TickTimer;
import common.world.Chunk;
import common.world.Entity;
import common.world.GameWorld;
import common.world.net.EntityUpdate;
import common.world.net.Update;

public class Server implements Runnable {
	private static final long NETWORK_INTERVAL = 50;
	private long lastNetworkUpdate;
	
	ReadWriteLock connectionsLock;
	LinkedList<Connection> connections;
	NetworkHandler ni;
	ServerSocket server;
	Thread t;
	ChatServer chatServer;
	GameWorld world;
	SocketAcceptor acceptor;

	public static void main(String[] args) {
		int port = 7331;
		Log.setPrimary(Log.SERVER);
		new Server(port);
	}
	
	public Server(int port) {
		world = new GameWorld();
		world.addChunk(new Chunk(0,0));
		
		connectionsLock = new ReentrantReadWriteLock();
		chatServer = new ChatServer(this);
		Log.p.out("Server Starting");
		server = NetworkHandler.getServerSocket(port);
		
		connections = new LinkedList<Connection>();
		
		if (server == null) {
			Log.p.out("Error Connecting Socket");
			System.exit(-1);
		} else {
			Log.p.out("Listening on port: "+port);
			acceptor = new SocketAcceptor(server,this);
			
			t = new Thread(this);
			t.start();
		}
	}
	
	private class SocketAcceptor implements Runnable {
		private ServerSocket server;
		private Server parent;
		private Thread t;
		private boolean halt;

		public SocketAcceptor(ServerSocket server, Server parent) {
			this.server = server;
			this.parent = parent;
			halt = false;
			
			t = new Thread(this);
			t.start();
		}
		
		@Override
		public void run() {
			while(!halt) {
				Socket s;
				try {
					s = server.accept();
					Log.p.out("Accepted Connection: "+s.getInetAddress().getHostAddress());
					NetworkHandler nh = new NetworkHandler(s);
					Connection conn = new Connection(parent,nh);
					parent.addConnection(conn);
				} catch (IOException e) {
					Log.p.error("Failed to accept connection",e);
				}
			}
		}
	}
	
	@Override
	public void run() {
		TickTimer timer = new TickTimer();
		
		int TICK_TIME = (int)(1000F/60F);
		long remain = 0;
		while(true) {
			long ms = timer.tick();
			remain += ms;
			
			while(remain > TICK_TIME) {
				world.update();
				updateConnections();
				remain -= TICK_TIME;
			}
			
			lastNetworkUpdate += ms;
			if (lastNetworkUpdate > NETWORK_INTERVAL) {
				networkUpdate();
				lastNetworkUpdate = 0;
			}
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Log.p.out("Thread sleep interrupted");
			}
		}
	}
	
	private void updateConnections() {
		connectionsLock.readLock().lock();
		Iterator<Connection> it = connections.iterator();
		while(it.hasNext()) {
			Connection conn = it.next();
			if (conn.isClosed()) {
				conn.finish();
				it.remove();
				Log.p.out("Removed connection");
				Log.p.out("Remaining Connections: "+connections.size());
				continue;
			}
			
			conn.update();
		}
		connectionsLock.readLock().unlock();
	}
	
	public void networkUpdate() {
		for (Entity entity : world.getEntities()) {
			EntityUpdate update = new EntityUpdate(entity);
			broadcastObject(update);
		}
	}
	
	public void addConnection(Connection conn) {
		connectionsLock.writeLock().lock();
		try {
			connections.add(conn);
		} finally {
			connectionsLock.writeLock().unlock();
		}
	}
	
	public void removeConnection(Connection conn) {
		connectionsLock.writeLock().lock();
		try {
			connections.remove(conn);
		} finally {
			connectionsLock.writeLock().unlock();
		}
	}

	public LinkedList<Connection> getConnections() {
		return connections;
	}

	public void broadcastObject(Update o) {
		broadcastExcept(null,o);
	}

	public ChatServer getChatServer() {
		return chatServer;
	}

	public GameWorld getWorld() {
		return world;
	}

	public void broadcastExcept(Connection except, Update o) {
		for (Connection conn : connections) {
			if (conn != except) {
				conn.getNetworkHandler().send(o);
			}
		}
	}
}
