package common.input;

public class MouseEvent {
	public int x;
	public int y;
	public int button;
	public int wheel;
	public boolean state;
	public long time;
	
	public MouseEvent(int x, int y, int button, int wheel, boolean state, long time) {
		this.x = x;
		this.y = y;
		this.button = button;
		this.wheel = wheel;
		this.state = state;
		this.time = time;
	}
}
