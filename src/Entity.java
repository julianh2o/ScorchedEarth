import java.io.Serializable;

public class Entity extends NetworkObject implements Serializable {
	private static final long serialVersionUID = -6741321824807407435L;
	
	Vector2D position;
	Vector2D velocity;
	double angle;
	
	private transient boolean dirty;
	int model;
	
	public Entity() {
		position = new Vector2D(100,100);
		velocity = new Vector2D(0,0);
		angle = 0;
	}
	
	void render(Window w) {
		Model modelObject = w.getModel(model);
		if (modelObject != null) {
			modelObject.renderAt(w,getX(),getY(),angle);
		}
	}
	
	void update(long ms) {
		position = position.add(velocity);
	}
	
	// Getters and Setters
	
	public double getX() {
		return position.getX();
	}
	
	public void setX(double x) {
		position.setX(x);
		setDirty(true);
	}
	
	public double getY() {
		return position.getY();
	}
	
	public void setY(double y) {
		position.setY(y);
		setDirty(true);
	}

	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(Vector2D position) {
		this.position = position;
		setDirty(true);
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
		setDirty(true);
	}
	
	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
		setDirty(true);
	}

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
		setDirty(true);
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public boolean isDirty() {
		return dirty;
	}
}
