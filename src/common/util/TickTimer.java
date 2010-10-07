package common.util;

public class TickTimer {
	private long lastTick;
	
	public TickTimer() {
		update();
	}
	
	public void update() {
		lastTick = getTime();
	}
	
	public long tick() {
		long tick = 0;
		long time = getTime();
		
		if (lastTick <= 0) {
			tick = -1;
		} else {
			tick = time - lastTick;
		}
		
		lastTick = time;
		
		return tick;
	}
	
	
	public static long getTime() {
		return System.currentTimeMillis();
	}
}
