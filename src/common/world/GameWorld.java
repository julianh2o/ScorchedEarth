package common.world;

import java.util.LinkedList;
import java.util.List;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.MassData;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import common.util.Log;

import client.Window;

public class GameWorld {
	private static final int FRAME_STEP = (int)(1000.0/40.0);
	public int nextId;
	
	List<Chunk> chunks;
	
	World phys;
	Body body;
	
	public GameWorld() {
		// Step 1: Create Physics World Boundaries  
		AABB bb = new AABB();  
		bb.lowerBound.set(new Vec2((float) -100.0, (float) -100.0));  
		bb.upperBound.set(new Vec2((float) 100.0, (float) 100.0));  

		// Step 2: Create Physics World with Gravity  
		Vec2 gravity = new Vec2((float) 0.0, (float) -.05);
		boolean doSleep = true;  
		phys = new World(bb, gravity, doSleep);  

		// Step 3: Create Ground Box  
		BodyDef groundBodyDef = new BodyDef();  
		groundBodyDef.position.set(new Vec2((float) 0.0, (float) -10.0));  
		Body groundBody = phys.createBody(groundBodyDef);  
		PolygonDef groundShapeDef = new PolygonDef();  
		groundShapeDef.setAsBox((float) 50.0, (float) 10.0);  
		groundBody.createShape(groundShapeDef);  
		
		BodyDef bodyDef = new BodyDef();  
		bodyDef.position.set((float) 6.0, (float) 24.0);  
		body  = phys.createBody(bodyDef);

		// Create Shape with Properties  
		CircleDef circle = new CircleDef();  
		circle.radius = (float) 1.0;
		circle.density = (float) 1.0;
		
		BodyDef bodyDef1 = new BodyDef();  
		bodyDef.position.set((float) 5.5, (float) 29.0);  
		Body body2  = phys.createBody(bodyDef1);

		// Create Shape with Properties  
		CircleDef circle1 = new CircleDef();  
		circle.radius = (float) 1.0;
		circle.density = (float) 1.0;

		// Assign shape to Body  
		body.createShape(circle);  
		body.setMassFromShapes();  
		
		body2.createShape(circle1);  
		body2.setMassFromShapes();  
		
		addTank();
		
		chunks = new LinkedList<Chunk>();
		//chunks.add(new Chunk(0,0));
		
		nextId = 0;
	}

	public void update(long ms) {
		long left = ms;
//		while (left > 0) {
//			long render = Math.min(left,FRAME_STEP);
//			left -= render;
			smallUpdate(ms);
//		}
	}
	
	public void smallUpdate(long ms) {
		phys.step(1,5);
		
		if (body != null) {  
			Vec2 position = body.getPosition();  
			float angle = body.getAngle();  
			//Log.p.out("Pos: (" + position.x + ", " + position.y + "), Angle: " + angle);  
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
		
		Body b = phys.getBodyList();
		
		while(b != null) {
			Vec2 pos = b.getPosition();
			w.getModel(0).renderAt(w, 20+pos.x*10, 20+pos.y*10, b.getAngle());
			b = b.getNext();
		}
	}
	
	public Tank addTank() {
		Tank tank = new Tank(newId());
//		phys.add(tank);
		return tank;
	}
	
	public void addTank(Tank tank) {
//		phys.add(tank);
	}
	
	public int newId() {
		return nextId++;
	}

	public Entity findEntity(int id) {
//		BodyList bodies = phys.getBodies();
//		for (int i=0; i<bodies.size(); i++) {
//			Body b = bodies.get(i);
//			if (b instanceof Entity) {
//				Entity e = (Entity)b;
//				if (e.getId() == id) {
//					return e;
//				}
//			}
//		}
		return null;
	}

	public void removeEntity(int id) {
		//phys.remove(findEntity(id));
	}
}
