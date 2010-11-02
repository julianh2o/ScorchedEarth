package common.network;

//This class handles the sockets, it provides a listener and event interface to the physical layer.

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import common.util.Log;

// This network handler is designed to handle both the client and server sockets

// to create the sockets required by the constructor.
public class NetworkHandler implements Runnable {
	public static final int INVALID = -1;
	public static final int ENTITY_UPDATE = 0;
	public static final int MESSAGE = 1;
	public static final int CHUNK = 2;
	public static final int KEY_EVENT = 3;
	
	Vector<NetworkEventListener> listeners;
	Socket socket;
	OutputStream out;
	InputStream in;
	
	DataOutputStream dos;
	DataInputStream dis;
	
	
	ReadWriteLock eventsLock;
	Queue<NetworkEvent> events;


	Thread t;
	volatile boolean halt;
	
	public NetworkHandler(Socket socket) {
		eventsLock = new ReentrantReadWriteLock();
		events = new LinkedList<NetworkEvent>();
		listeners = new Vector<NetworkEventListener>();
		this.socket = socket;
		
		
		try {
			out = socket.getOutputStream();
			in = socket.getInputStream();
			
			dos = new DataOutputStream(out);
			dis = new DataInputStream(in);
		} catch (IOException e) {
			System.err.println("Could not set up stream IO");
		}
		t = new Thread(this);
		t.start();
	}
	
	// this method is used to instantiate a server socket, this socket starts a server on the specified port
	// Once this socket is created, call accept() to start generating normal socket objects
	// These sockets should then be fed into the constructor of this class (NetworkHandler)
	public static ServerSocket getServerSocket(int port) {
		try {
			return new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not create server socket");
			return null;
		}
	}

	// The socket created by this method should just be passed directly back into the
	// constructor for the NetworkHandler object.	
	public static Socket getClientSocket(String target, int port) {
		try {
			return new Socket(target,port);
		} catch (IOException e) {
			System.err.println("Could not create socket");
			e.printStackTrace();
			return null;
		}
	}
	
	public void send(int type, byte[] bytes) {
		try {
			dos.writeInt(type);
			dos.writeInt(bytes.length);
			dos.write(bytes);
		} catch (SocketException e) {
			halt = true;
			return;
		} catch (IOException e) {
			Log.p.error("Error sending object",e);
		}
	}

	// This thread constantly reads objects off the network and generates errors
	// according to the success that it has. When an object comes in, it is broadcasted
	// to its listeners	
	public void run() {
		while(!halt) {
			int type = INVALID;
			int size = -1;
			byte[] bytes = null;
			try {
				type = dis.readInt();
				size = dis.readInt();
				bytes = new byte[size];
				
				int bytesRead = 0;
				while(bytesRead < size) {
					bytesRead += dis.read(bytes, bytesRead, size-bytesRead);
				}
				
				if (bytesRead < size) {
					Log.p.out("SHORT READ");
					continue;
				}
			} catch (EOFException e) {
				Log.p.out("Connection closed by peer");
				break;
			} catch (SocketException e) {
				Log.p.out("Connection reset");
				break;
			} catch (IOException e) {
				if (halt == true) continue;
				Log.p.error("Could not read from stream",e);
			}
			if (type == INVALID) continue;
			
			eventsLock.writeLock().lock();
			events.add(new NetworkEvent(type,bytes));
			eventsLock.writeLock().unlock();
		}

		try {
			if (!socket.isClosed()) socket.close();
		} catch (IOException e) {
			System.out.println("Could not close connection");
		}
	}
	
	public void close() {
		halt = true;
		try {
			if (!socket.isClosed()) socket.close();
			t.join();
		} catch (InterruptedException e) {
			System.out.println("thread join interrupted!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error closing socket");
			e.printStackTrace();
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void update() {
		eventsLock.writeLock().lock();
		NetworkEvent[] eventList = events.toArray(new NetworkEvent[0]);
		events.clear();
		eventsLock.writeLock().unlock();
		
		for (NetworkEvent event : eventList) {
			broadcastEvent(event);
		}
	}

	// Listener methods
	// Adds a network listener to be called whenever an object is received over the network
	public void addNetworkEventListener(NetworkEventListener l) {
		listeners.add(l);
	}
	
	public void removeNetworkEventListener(NetworkEventListener l) {
		listeners.remove(l);
	}
	
	private void broadcastEvent(NetworkEvent e) {
		for (int i=0; i<listeners.size(); i++) {
			listeners.get(i).networkEventReceived(e);
		}
	}
}
