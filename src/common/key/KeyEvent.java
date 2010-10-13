package common.key;

import java.io.DataOutputStream;
import java.io.IOException;

import common.world.net.Update;

public class KeyEvent implements Update {
	private static final long serialVersionUID = 2013332506792378711L;
	
	private int key;
	private boolean down;
	private long time;
	
	public KeyEvent(int key, boolean down) {
		setKey(key);
		setDown(down);
		setTime(System.currentTimeMillis());
	}

	@Override
	public void write(DataOutputStream o) throws IOException {
		o.write(key);
		o.writeBoolean(down);
		o.writeLong(time);
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
