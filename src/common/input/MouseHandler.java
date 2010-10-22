package common.input;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;

import client.View;

public class MouseHandler {
	View view;
	MouseEvent lastEvent;
	List<MouseListener> listeners;
	
	public MouseHandler(View view) {
		this.view = view;
		listeners = new LinkedList<MouseListener>();
	}

	public void handle() {
		while (Mouse.next()) {
			int x = Mouse.getEventX();
			int y = Mouse.getEventY();
			int button = Mouse.getEventButton();
			int wheel = Mouse.getEventDWheel();
			boolean state = Mouse.getEventButtonState();
			
			MouseEvent event = new MouseEvent(x,y,button,wheel,state,System.currentTimeMillis());
			
			if (Mouse.getEventDX() != 0 || Mouse.getEventDY() != 0) {
				broadcastMovedEvent(event);
			}
			
			if (button != -1) {
				if (state) {
					broadcastPressedEvent(event);
				} else {
					broadcastReleasedEvent(event);
				}
			}
			
			if (wheel != 0) {
				broadcastWheelEvent(event);
			}
			
			lastEvent = event;
		}
	}
	
	public MouseEvent getLastEvent() {
		return lastEvent;
	}
	
	public float getX() {
		if (lastEvent == null) return -1;
		return view.x + view.width*(lastEvent.x/view.windowWidth);
	}
	
	public float getY() {
		if (lastEvent == null) return -1;
		return view.y + view.height*(lastEvent.y/view.windowHeight);
	}
	
	public void addMouseListener(MouseListener m) {
		listeners.add(m);
	}
	
	public void removeKeyListener(MouseListener m) {
		listeners.remove(m);
	}
	
	private void broadcastPressedEvent(MouseEvent e) {
		for(MouseListener m : listeners) {
			m.mousePressed(e);
		}
	}
	
	private void broadcastReleasedEvent(MouseEvent e) {
		for(MouseListener m : listeners) {
			m.mouseReleased(e);
		}
	}
	
	private void broadcastMovedEvent(MouseEvent e) {
		for(MouseListener m : listeners) {
			m.mouseMoved(e);
		}
	}
	
	private void broadcastWheelEvent(MouseEvent e) {
		for(MouseListener m : listeners) {
			m.mouseWheel(e);
		}
	}
}

