public class Entity {
	Vector2D position;
	Vector2D velocity;
	Model model;
	
	public Entity() {
		position = new Vector2D();
		velocity = new Vector2D(1.0,1.0);
		model = new Model();
	}
	
	void render(Window w) {
		if (model != null) {
			model.renderAt(w,getX(),getY());
		}
	}
	
	void update(long ms) {
		position.add(velocity);
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

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
}
