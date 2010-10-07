package common.world;

import java.io.Serializable;

import client.Client;
import client.GameScreen;
import client.Screen;
import common.network.NetworkObject;
import common.util.Log;

public class ClientUpdate extends NetworkObject implements Serializable {
	private static final long serialVersionUID = 3724160903399942477L;
	
	public enum Type {
		GRANT_CONTROL
	}
	
	Type type;
	Serializable object;
	
	public ClientUpdate(Type type, Serializable object) {
		this.type = type;
		this.object = object;
	}
	
	public void update(Client client) {
		switch(type) {
			case GRANT_CONTROL:
				Screen screen = client.getScreen();
				Log.p.out("Granting control");
				if (screen instanceof GameScreen) {
					GameScreen gs = (GameScreen)screen;
					gs.controlTank((Integer)object);
				} else {
					Log.p.out("Failed screen check");
				}
				break;
		}
	}

}
