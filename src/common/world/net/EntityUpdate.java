package common.world.net;

import net.phys2d.raw.Body;
import common.world.Entity;

public class EntityUpdate extends Update {
	private static final long serialVersionUID = -6281198949891263824L;
	
	private int id;
	private float x,y,xvel,yvel,r,rvel;
	
	public EntityUpdate(Entity e, Body b) {
		this.id = e.getId();
		this.x = b.getPosition().getX();
		this.y = b.getPosition().getY();
		this.xvel = b.getVelocity().getX();
		this.yvel = b.getVelocity().getY();
		this.r = b.getRotation();
		this.rvel = b.getAngularVelocity();
	}
	
	public void update(Entity e, Body b) {
		b.setPosition(x, y);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getXvel() {
		return xvel;
	}
	public void setXvel(float xvel) {
		this.xvel = xvel;
	}
	public float getYvel() {
		return yvel;
	}
	public void setYvel(float yvel) {
		this.yvel = yvel;
	}
	public float getR() {
		return r;
	}
	public void setR(float r) {
		this.r = r;
	}
	public float getRvel() {
		return rvel;
	}
	public void setRvel(float rvel) {
		this.rvel = rvel;
	}
}
