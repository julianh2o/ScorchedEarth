package common.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import common.util.Log;

public class UDPNetworkHandler {
	public static final int BUFFER_SIZE = 2000;
	
	int port;
	byte[] buffer;
	DatagramSocket socket;
	
	public UDPNetworkHandler(int port) throws SocketException {
		this.port = port;
		socket = new DatagramSocket(port);
	}
	
	public DatagramPacket next() {
		buffer = new byte[2000];
		DatagramPacket p = new DatagramPacket(buffer,buffer.length);
		try {
			socket.receive(p);
			return p;
		} catch (IOException e) {
			Log.p.error("Error reading packet", e);
			return null;
		}
	}
}
