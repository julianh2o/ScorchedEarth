package common.world;

public class Tile {
	public static final int INVALID = -1;
	public static final int GRASS = 0;
	public static final int DIRT = 1;
	public static final int BLOCK = 2;
	
	public static int getModel(int type) {
		return 1 + type;
	}
}
