import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server implements Runnable {
	ReadWriteLock connectionsLock;
	LinkedList<Connection> connections;
	NetworkHandler ni;
	ServerSocket server;
	Thread t;
	ChatServer chatServer;
	World world;
	SocketAcceptor acceptor;

	public static void main(String[] args) {
		int port = 7331;
		new Server(port);
	}
	
	public Server(int port) {
		world = new World();
		
		connectionsLock = new ReentrantReadWriteLock();
		chatServer = new ChatServer(this);
		Log.setPrimary(Log.SERVER);
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

		public SocketAcceptor(ServerSocket server, Server parent) {
			this.server = server;
			this.parent = parent;
			
			t = new Thread(this);
			t.start();
		}
		
		public void run() {
			Socket s;
			try {
				s = server.accept();
				Log.p.out("Accepted Connection");
				NetworkHandler nh = new NetworkHandler(s);
				Connection conn = new Connection(parent,nh);
				parent.addConnection(conn);
			} catch (IOException e) {
				Log.p.error("Failed to accept connection",e);
			}
		}
	}
	
	public void run() {
		TickTimer timer = new TickTimer();
		while(true) {
			long delta = timer.tick();
			
			connectionsLock.readLock().lock();
			for (Connection conn : connections) {
				conn.update(delta);
			}
			connectionsLock.readLock().unlock();
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				Log.p.out("Thread sleep interrupted");
			}
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

	public void broadcastObject(NetworkObject o) {
		for (Connection conn : connections) {
			conn.getNetworkHandler().send(o);
		}
	}

	public ChatServer getChatServer() {
		return chatServer;
	}

	public World getWorld() {
		return world;
	}
}
