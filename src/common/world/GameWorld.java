package common.world;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import common.util.Log;
import common.world.entity.EntityType;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.CollisionEvent;
import net.phys2d.raw.CollisionListener;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import client.Renderer;
import client.Window;

public class GameWorld implements CollisionListener {
	public int nextId;
	
	List<Chunk> chunks;
	
	World phys;
	Hashtable<Entity,Body> ebmap;

	private List<EntityRemovalListener> removalListeners;
	
	public GameWorld() {
		removalListeners = new LinkedList<EntityRemovalListener>();
		
		ebmap = new Hashtable<Entity,Body>();
		phys = new World(new Vector2f(0F,0F),5);
		phys.addListener(this);
		
		chunks = new LinkedList<Chunk>();
		
		nextId = 0;
	}

	public void generate() {
		boolean[] hasBlock = new boolean[Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE];
		for (int i=0; i<30; i++) {
			int xx = (int)(Math.random()*Chunk.CHUNK_SIZE);
			int yy = (int)(Math.random()*Chunk.CHUNK_SIZE);
			int len = (int)(Math.random()*10);
			for (int l=0; l<len; l++) {
				xx += (int)(Math.random()*3 - 1);
				yy += (int)(Math.random()*3 - 1);

				if (xx < 0 || xx > Chunk.CHUNK_SIZE-1 || yy < 0 || yy > Chunk.CHUNK_SIZE-1) continue;
				int bindex = xx + yy * Chunk.CHUNK_SIZE;
				if (hasBlock[bindex]) continue;
				hasBlock[bindex] = true;
				addBlock(xx,yy);
			}
		}
		
		for (int i=0; i<Chunk.CHUNK_SIZE; i++) addBlock(0,i);
		for (int i=0; i<Chunk.CHUNK_SIZE; i++) addBlock(Chunk.CHUNK_SIZE-1,i);
		for (int i=0; i<Chunk.CHUNK_SIZE; i++) addBlock(i,0);
		for (int i=0; i<Chunk.CHUNK_SIZE; i++) addBlock(i,Chunk.CHUNK_SIZE-1);
	}
	
	private void addBlock(int x, int y) {
		Entity e = new Entity(newId(),"block.entity");
		addEntity(e,x*Chunk.TILE_SIZE, y*Chunk.TILE_SIZE);
	}
	
	public void addEntity(Entity e, float x, float y) {
		Body body = null;
		
		EntityType type = e.getType();
		
		body = new Body(new Box(type.getWidth(),type.getHeight()), type.getMass());
		if (type.getFixed()) body = new StaticBody(new Box(type.getWidth(),type.getHeight()));
		body.setRestitution(type.getRestitution());
		body.setDamping(type.getDamping());
		body.setRotDamping(type.getRotationalDamping());
		body.setRotatable(type.getRotatable());
		
		if (type.getPath().equals("projectile.entity")) {
			Entity owner = findEntity(e.getOwner());
			body.addExcludedBody(owner.getBody());
		}
		
		
		body.setUserData(e);
		if (e.getId() < 0) e.setId(newId());
		e.setWorld(this);
		body.setPosition(x,y);
		ebmap.put(e,body);
		phys.add(body);
	}

	//please call me 60 times per second
	public void update() {
		phys.step();
		
		Entity[] entityList = getEntities().toArray(new Entity[0]);
		for (Entity e : entityList) {
			e.update();
			if (e.isDead()) {
				Log.p.out("removing entity, its dead");
				removeEntity(e);
			}
		}
	}
	
	@Override
	public void collisionOccured(CollisionEvent event) {
		Body ba = event.getBodyA();
		Entity a = (Entity)ba.getUserData();
		
		Body bb = event.getBodyB();
		Entity b = (Entity)bb.getUserData();
//		Log.p.out("Collision: "+b.getType().getPath()+ "    and     "+ a.getType().getPath());
//		Log.p.out("\t\t: "+b.getId()+ "    and     "+ a.getId());
		
		boolean projA = "projectile.entity".equals(a.getType().getPath());
		boolean projB = "projectile.entity".equals(b.getType().getPath());
		
		if (projA || projB) {
			if (projA && projB) {
				return;
			} else if (projA) {
				Log.p.out("removing entity "+a.getId()+"due to collision");
				removeEntity(a);
				b.damage(10);
			} else if (projB) {
				Log.p.out("removing entity "+b.getId()+"due to collision");
				removeEntity(b);
				a.damage(10);
			}
		}
	}
	
	public void removeEntity(Entity e) {
		broadcastEntityRemoval(e);
		Body b = getBody(e);
		phys.remove(b);
		ebmap.remove(e);
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
		if (e == null) return;
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

	public Entity newEntity(String type, float x, float y, int id) {
		if (id == -1) {
			id = newId();
		}
			
		Entity e = new Entity(id,type);
		addEntity(e,x,y);
		return e;
	}
		
	public Entity newEntity(String type,float x,float y) {
		return newEntity(type,x,y,-1);
	}
	
	
	/// Entity Removal Listener methods
	private void broadcastEntityRemoval(Entity e) {
		for (EntityRemovalListener rl : removalListeners) {
			rl.entityRemoved(e);
		}
	}
	
	public void addEntityRemovalListener(EntityRemovalListener l) {
		removalListeners.add(l);
	}
	
	public void removeEntityRemovalListener(EntityRemovalListener l) {
		removalListeners.remove(l);
	}
}
