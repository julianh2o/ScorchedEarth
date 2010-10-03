//This class handles the sockets, it provides a listener and event interface to the physical layer.

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

// This network handler is designed to handle both the client and server sockets
// Two static methods provide easy socket creation and should almost always be used
// to create the sockets required by the constructor.
public class NetworkHandler implements Runnable {
	Vector<NetworkEventListener> listeners;
	Socket socket;
	OutputStream out;
	InputStream in;

	ObjectOutputStream oos;
	ObjectInputStream ois;

	Thread t;
	volatile boolean halt;
	
	public NetworkHandler(Socket socket) {
		listeners = new Vector<NetworkEventListener>();
		this.socket = socket;
		try {
			out = socket.getOutputStream();
			oos = new ObjectOutputStream(out);
			in = socket.getInputStream();
			ois = new ObjectInputStream(in);
		} catch (IOException e) {
			System.err.println("Could not set up stream IO");
		}
		t = new Thread(this);
		t.start();
	}
	
	// this method is used to enerate a server socket, this socket starts a server on the specified port
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
	
	// All network activity is sent through serializable objects
	// The behavior of the client/server monitoring this network handler should
	// check the type/nature of the object and distribute it accordingly
	public void send(Serializable s) {
		try {
			oos.writeObject(s);
		} catch (IOException e) {
			Log.p.error("Error sending object",e);
		}
	}

	// This thread constantly reads objects off the network and generates errors
	// according to the success that it has. When an object comes in, it is broadcasted
	// to its listeners	
	public void run() {
		while(!halt) {
			Object o = null;
			try {
				if (ois.available() > 0) {
					o = ois.readObject();
				}
			} catch (ClassNotFoundException e) {
				Log.p.error("Class not found",e);
			} catch (EOFException e) {
				Log.p.out("Connection closed by peer");
				break;
			} catch (IOException e) {
				Log.p.error("Could not read from stream",e);
			}
			if (o == null) {
				continue;
			}
			broadcastEvent(new NetworkEvent(o));

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Could not close connection");
		}
	}
	
	public void close() {
		halt = true;
		try {
			t.join();
		} catch (InterruptedException e) {
			System.out.println("thread join interrupted!");
			e.printStackTrace();
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
