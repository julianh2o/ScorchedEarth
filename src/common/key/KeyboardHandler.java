package common.key;

import java.util.HashMap;
import java.util.Vector;
import org.lwjgl.input.Keyboard;

import common.util.Log;

public class KeyboardHandler {
	private Vector<KeyListener> listeners;
	private HashMap<Integer,KeyEvent> keyStates;
	
	public KeyboardHandler() {
		listeners = new Vector<KeyListener>();
		keyStates = new HashMap<Integer,KeyEvent>();
		
	}
	
	public void handle() {
		while (Keyboard.next()) { 
			int key = Keyboard.getEventKey();
			boolean down = Keyboard.getEventKeyState();
			Log.p.out("got char: "+key+" ("+new Boolean(down).toString()+")");
			KeyEvent e = new KeyEvent(key,down);
			update(e);
			if (down) {
				broadcastPressedEvent(e);
			} else {
				broadcastReleasedEvent(e);
			}
		}
	}
	
	public void update(KeyEvent e) {
		keyStates.put(e.getKey(),e);
	}
	
	public KeyEvent getLastEvent(int key) {
		return keyStates.get(new Integer(key));
	}
	
	public boolean isDown(int key) {
		KeyEvent e = getLastEvent(key);
		if (e == null) return false;
		return e.isDown();
	}
	
	public void addKeyListener(KeyListener k) {
		listeners.add(k);
	}
	
	public void removeKeyListener(KeyListener k) {
		listeners.remove(k);
	}
	
	private void broadcastPressedEvent(KeyEvent e) {
		for(KeyListener k : listeners) {
			k.keyPressed(e);
		}
	}
	
	private void broadcastReleasedEvent(KeyEvent e) {
		for(KeyListener k : listeners) {
			k.keyReleased(e);
		}
	}
}
