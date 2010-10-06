public class WorldUpdate {
	enum Type {
		NEW_TANK
	}
	
	Type type;
	Object object;
	
	public WorldUpdate(Tank t) {
		this.type = Type.NEW_TANK;
	}
	
	public WorldUpdate(Type type, Object object) {
		this.type = type;
		this.object = object;
	}
}
