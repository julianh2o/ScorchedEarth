package common.world;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;

import client.Renderer;
import client.Window;

public class GameWorld {
	public int nextId;
	
	List<Chunk> chunks;
	
	World phys;
	HashMap<Entity,Body> ebmap;
	
	public GameWorld() {
		ebmap = new HashMap<Entity,Body>();
		phys = new World(new Vector2f(0F,0F),5);
		
		chunks = new LinkedList<Chunk>();
		
		nextId = 0;
	}
	
	public void addEntity(Entity e) {
		Body body = null;
		if (e instanceof Tank) {
			body = new Body(new Box(1.0F,1.0F), 1.0F);
			body.setRestitution(1.0F);
			body.setDamping(.05F);
			body.setRotDamping(.05F);
		} else {
			body = new Body(new Box(1.0F,1.0F), 1.0F);
			body.setRestitution(1.0F);
		}
		
		if (body != null) {
		//TODO set this to something useful
			body.setPosition(15.0F, 15.0F);
			ebmap.put(e,body);
			phys.add(body);
		}
	}

	//please call me 60 times per second
	public void update() {
		phys.step();
	}
	
	public int getTileAt(double x, double y) {
		for (Chunk c : chunks) {
			int t = c.getTileAt(x,y);
			if (t != Tile.INVALID) return t;
		}
		return Tile.INVALID;
	}
	
	public void render(Window w) {
		for (Chunk chunk : chunks) {
			chunk.render(w);
		}
		
		for (Entity e : ebmap.keySet()) {
			Body b = ebmap.get(e);
			Renderer.render(w,b,e);
		}
	}
	
	public Tank addTank(int id) {
		if (id == -1) id = newId();
		Tank tank = new Tank(this,id);
		addEntity(tank);
		return tank;
	}
	
	public int newId() {
		return nextId++;
	}

	public Entity findEntity(int id) {
		for (Entity e : ebmap.keySet()) {
			if (id == e.getId()) {
				return e;
			}
		}
		return null;
	}

	public void removeEntity(int id) {
		Entity e = findEntity(id);
		Body b = ebmap.get(e);
		
		ebmap.remove(e);
		phys.remove(b);
	}
	
	// get the chunk that covers the given x.y coordinates
	public Chunk getChunk(float x, float y) {
		for (Chunk c : chunks) {
			if (c.contains(x, y)) return c;
		}
		return null;
	}
	
	public int getChunkCount() {
		return chunks.size();
	}

	public void addChunk(Chunk chunk) {
		chunks.add(chunk);
	}
	
	public Set<Entity> getEntities() {
		return ebmap.keySet();
	}
	
	public Body getBody(Entity e) {
		return ebmap.get(e);
	}
}
