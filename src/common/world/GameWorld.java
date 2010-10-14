package common.world;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import common.world.Entity.Type;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
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

	public void generate() {
		for (int i=0; i<20; i++) {
			int xx = (int)(Math.random()*Chunk.CHUNK_SIZE);
			int yy = (int)(Math.random()*Chunk.CHUNK_SIZE);
			int len = (int)(Math.random()*20);
			for (int l=0; l<len; l++) {
				xx += (int)(Math.random()*3 - 1);
				yy += (int)(Math.random()*3 - 1);

				if (xx < 0 || xx > Chunk.CHUNK_SIZE-1 || yy < 0 || yy > Chunk.CHUNK_SIZE-1) continue;
				Entity e = new Entity(this, newId(),Entity.Type.BLOCK);
				addEntity(e,xx*Chunk.TILE_SIZE, yy*Chunk.TILE_SIZE);
			}
		}
	}
	
	public void addEntity(Entity e, float x, float y) {
		Body body = null;
		switch(e.getType()) {
		case TANK:
			body = new Body(new Box(1.0F,1.0F), 1.0F);
			body.setRestitution(1.0F);
			body.setDamping(.05F);
			body.setRotDamping(.05F);
			break;
		case BLOCK:
			body = new StaticBody(new Box(1.0F,1.0F));
			body.setRestitution(1.0F);
			break;
		}
		
		if (body != null) {
			body.setPosition(x,y);
			ebmap.put(e,body);
			phys.add(body);
		}
	}

	//please call me 60 times per second
	public void update() {
		phys.step();
		
		for (Entity e : getEntities()) {
			e.update();
		}
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

	public Entity newEntity(Type type,float x,float y) {
		Entity e = new Entity(this,newId(),type);
		addEntity(e,x,y);
		return e;
	}
}
