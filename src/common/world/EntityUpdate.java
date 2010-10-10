package common.world;

import common.network.NetworkObject;

public class EntityUpdate extends NetworkObject {
	private static final long serialVersionUID = -6281198949891263824L;
	
	private int id;
	private float x,y,xvel,yvel,r,rvel;
	
	public EntityUpdate(Entity e) {
		this.id = e.getId();
//		this.x = e.getPosition().getX();
//		this.y = e.getPosition().getY();
//		this.xvel = e.getVelocity().getX();
//		this.yvel = e.getVelocity().getY();
//		this.r = e.getRotation();
//		this.rvel = e.getAngularVelocity();
	}
	
	public void update(Entity e) {
		//e.move(x, y);
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
