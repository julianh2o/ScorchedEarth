package server;


import java.io.IOException;

import com.google.protobuf.InvalidProtocolBufferException;

import common.input.KeyEvent;
import common.input.KeyboardHandler;
import common.network.NetworkEvent;
import common.network.NetworkEventListener;
import common.network.NetworkHandler;
import common.network.NetworkProto.NetworkEntity;
import common.network.NetworkProto.NetworkKeyEvent;
import common.network.NetworkProto.NetworkMessage;
import common.network.NetworkProto.NetworkMessage.Type;
import common.util.Log;
import common.world.Chunk;
import common.world.Entity;
import common.world.behavior.ControlledTankBehavior;
import common.world.net.WorldChunk;

// The connection class handles a single connection to the server. This class holds the
// means of sending objects to each client as well as tightly bound userdata.

public class Connection implements NetworkEventListener {
	private Server server;
	private NetworkHandler nh;
	private KeyboardHandler kb;
	private Entity tank;
	
	private boolean initilized;
	
	public Connection(Server server, NetworkHandler nh) {
		initilized = false;
		
		kb = new KeyboardHandler();
		this.server = server;
		this.nh = nh;
		
		nh.addNetworkEventListener(this);
		
		tank = server.getWorld().newEntity(Entity.Type.TANK,5F,5F);
		
		if (nh.getSocket().getInetAddress().getHostAddress().equals("75.18.227.231")) {
//		if (nh.getSocket().getInetAddress().getHostAddress().equals("67.161.1.188")) {
			Log.p.out("Pink Ranger!");
			tank.setModel(4);
		}
		
		//TODO save and restore a logged-out tank's position.. (implement user persistence lol)
		Chunk c = server.getWorld().getChunk(5F, 5F);
		try {
			nh.send(NetworkHandler.CHUNK,new WorldChunk(c).getBytes());
		} catch (IOException e) {
			Log.p.error("Error sending chunk",e);
		}
	}
	
	public void update() {
		
	}
	
	public void networkUpdate() {
		for (Entity e : server.getWorld().getEntities()) {
			if (!initilized || e.isDirty()) {
				if (e != tank) {
					nh.send(NetworkHandler.ENTITY_UPDATE, e.getBytes());
				}
			}
		}
	}

	// This method is the entry point for all communication sent by the client
	public void networkEventReceived(NetworkEvent event) {
		try {
			handleNetworkEvent(event);
		} catch (Exception e) {
			Log.p.error("Error handling network event",e);
			return; 
		}
	}
	
	//TODO change the exception on this
	public void handleNetworkEvent(NetworkEvent e) throws Exception {
		int type = e.getType();
		switch (type) {
		case NetworkHandler.KEY_EVENT:
			NetworkKeyEvent nke;
			nke = NetworkKeyEvent.parseFrom(e.getData());
			kb.update(new KeyEvent(nke));
			break;
		case NetworkHandler.MESSAGE:
			handleNetworkMessage(e);
			break;
		case NetworkHandler.ENTITY_UPDATE:
			NetworkEntity ne = NetworkEntity.parseFrom(e.getData());
			if (tank.getId() == ne.getId()) {
				tank.updateWith(ne);
			}
			break;
		}
	}
	
	private void handleNetworkMessage(NetworkEvent e) {
		NetworkMessage nm;
		try {
			nm = NetworkMessage.parseFrom(e.getData());
		} catch (InvalidProtocolBufferException e1) {
			e1.printStackTrace();
			return;
		}
		
		switch (nm.getType()) {
		case CLIENT_READY:
			Log.p.out("broadcasting tank");
			server.broadcastBytes(NetworkHandler.ENTITY_UPDATE,tank.getBytes());
			
			//TODO streamline this or at least put it on its own line
			tank.setBehavior(new ControlledTankBehavior(kb,null));
			nh.send(NetworkHandler.MESSAGE, NetworkMessage.newBuilder().setType(Type.GRANT_CONTROL).setTarget(tank.getId()).build().toByteArray());
			break;
		}
	}

	public void finish() {
		server.getWorld().removeEntity(tank.getId());
	}
	
	public boolean isClosed() {
		return nh.getSocket().isClosed();
	}

	public String getName() {
		return nh.getSocket().getInetAddress().toString();
	}

	public Server getServer() {
		return server;
	}

	public Entity getTank() {
		return tank;
	}

	public KeyboardHandler getKb() {
		return kb;
	}

	public NetworkHandler getNetworkHandler() {
		return nh;
	}

}
