package common.world;

import java.util.LinkedList;
import java.util.List;

import client.Window;

import common.network.NetworkObject;

public class World extends NetworkObject {
	private static final long serialVersionUID = 3294614897289442040L;
	
	private static final int FRAME_STEP = 10;
	public int nextId;
	
	List<Entity> entities;
	List<Tank> tanks;
	
	public World() {
		nextId = 0;
		entities = new LinkedList<Entity>();
		tanks = new LinkedList<Tank>();
	}
	
	public void update(long ms) {
		long left = ms;
		while (left > 0) {
			long render = Math.min(left,FRAME_STEP);
			left -= render;
			smallUpdate(render);
		}
	}
	
	public void smallUpdate(long ms) {
		for (Entity entity : entities) {
			entity.update(ms);
		}
		
	}
	
	public void render(Window w) {
		for (Entity entity : entities) {
			entity.render(w);
		}
	}
	
	public Tank addTank() {
		Tank tank = new Tank(newId());
		tanks.add(tank);
		entities.add(tank);
		return tank;
	}
	
	public void addTank(Tank tank) {
		tanks.add(tank);
		entities.add(tank);
	}
	
	public int newId() {
		return nextId++;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public List<Tank> getTanks() {
		return tanks;
	}
}
