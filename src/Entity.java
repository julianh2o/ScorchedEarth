public class Entity {
	Vector2D position;
	Vector2D velocity;
	double angle;
	
	Model model;
	
	public Entity() {
		position = new Vector2D(100,100);
		velocity = new Vector2D(0,0);
		angle = 0;
		model = new Model();
	}
	
	void render(Window w) {
		if (model != null) {
			model.renderAt(w,getX(),getY(),angle);
		}
	}
	
	void update(long ms) {
		position = position.add(velocity);
	}
	
	public double getX() {
		return position.getX();
	}
	
	public void setX(double x) {
		position.setX(x);
	}
	
	public double getY() {
		return position.getY();
	}
	
	public void setY(double y) {
		position.setY(y);
	}

	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(Vector2D position) {
		this.position = position;
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}
	
	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
}
