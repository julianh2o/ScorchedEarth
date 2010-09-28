import java.io.IOException;
import java.io.Serializable;
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

	public static void main(String[] args) {
		int port = 7331;
		new Server(port);
	}
	
	public Server(int port) {
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
			t = new Thread(this);
			t.start();
		}
	}
	
	public void run() {
		while(true) {
			Socket s;
			try {
				s = server.accept();
				Log.p.out("Accepted Connection");
				NetworkHandler nh = new NetworkHandler(s);
				Connection conn = new Connection(this,nh);
				addConnection(conn);
			} catch (IOException e) {
				Log.p.error("Failed to accept connection",e);
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

	public void broadcastObject(Serializable o) {
		for (Connection conn : connections) {
			conn.nh.send(o);
		}
	}

	public ChatServer getChatServer() {
		return chatServer;
	}
}
