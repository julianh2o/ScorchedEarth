
public class TankController implements Controller {
	NetworkHandler nh;
	Tank tank;
	
	public TankController(NetworkHandler nh) {
		this.nh = nh;
//		this.tank = tank;
	}
	
	public void update(long ms) {
	}
	
	public Tank getTank() {
		return tank;
	}

	public void setTank(Tank tank) {
		this.tank = tank;
	}
	
}
