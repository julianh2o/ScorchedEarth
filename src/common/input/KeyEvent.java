package common.input;

public class KeyEvent {
	private int key;
	private boolean down;
	private long time;
	
	public KeyEvent(int key, boolean down) {
		setKey(key);
		setDown(down);
		setTime(System.currentTimeMillis());
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isDown() {
		return down;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTime() {
		return time;
	}
}
