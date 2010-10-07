package common.world;

import java.io.Serializable;

import common.network.NetworkObject;

public class WorldUpdate extends NetworkObject implements Serializable {
	private static final long serialVersionUID = 5044130946286460848L;

	public enum Type {
		NEW_TANK
	}
	
	Type type;
	Serializable object;
	
	public WorldUpdate(Type type, Serializable object) {
		this.type = type;
		this.object = object;
	}
	
	public void update(World world) {
		switch(type) {
			case NEW_TANK:
				world.addTank((Tank)object);
				break;
		}
	}
}
