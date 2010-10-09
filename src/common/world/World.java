package common.world;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import client.Window;

import common.network.NetworkObject;
import common.util.Log;
import common.world.Tile.Type;

public class World extends NetworkObject {
	private static final long serialVersionUID = 3294614897289442040L;
	
	private static final int FRAME_STEP = 10;
	public int nextId;
	
	List<Entity> entities;
	List<Tank> tanks;
	List<Chunk> chunks;
	
	public World() {
		chunks = new LinkedList<Chunk>();
		chunks.add(new Chunk(0,0));
		
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
			
			Tile tile = getTileAt(entity.getX(),entity.getY());
			if (tile != null && tile.type == Type.BLOCK) {
				entity.collide(ms);
			}
		}
	}
	
	public Tile getTileAt(double x, double y) {
		for (Chunk c : chunks) {
			Tile t = c.getTileAt(x,y);
			if (t != null) return t;
		}
		return null;
	}
	
	public void render(Window w) {
		for (Chunk chunk : chunks) {
			chunk.render(w);
		}
		
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

	public Entity findEntity(int id) {
		for (Entity e : entities) {
			if (e.getId() == id) {
				return e;
			}
		}
		return null;
	}

	public void removeEntity(int id) {
		Iterator<Entity> it = entities.iterator();
		while (it.hasNext()) {
			if (it.next().getId() == id) {
				it.remove();
			}
		}
	}
}
