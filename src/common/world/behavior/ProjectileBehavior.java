package common.world.behavior;

public class ProjectileBehavior extends Behavior {
	public void update() {
		entity.damage(1);
	}
}
