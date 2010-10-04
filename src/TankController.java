import org.lwjgl.input.Keyboard;

public class TankController implements Controller {
	Tank tank;
	
	public TankController(Tank tank) {
		this.tank = tank;
	}
	
	public void update(long ms) {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			Vector2D dvel = new Vector2D(tank.getAngle());
			dvel = dvel.scale(3);
			tank.setVelocity(tank.getVelocity().add(Util.timeScale(dvel, ms)));
			
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			tank.rotateLeft(.03);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			tank.rotateRight(.03);
		}
	}
	
	public Tank getTank() {
		return tank;
	}

	public void setTank(Tank tank) {
		this.tank = tank;
	}
	
}
