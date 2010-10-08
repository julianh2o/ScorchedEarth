package common.world;

import java.io.Serializable;

public class Tile implements Serializable {
	private static final long serialVersionUID = -1491668853156270449L;

	public static Type[] types = Type.values();
	enum Type {
		GRASS,
		DIRT
	}
	
	
	Type type;
	
	public Tile(Type type) {
		this.type = type;
	}

	public int getModel() {
		switch(type) {
			case GRASS:
				return 1;
		}
		return 0;
	}
	
}
