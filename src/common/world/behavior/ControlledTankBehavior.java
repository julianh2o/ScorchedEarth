package common.world.behavior;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import client.GameScreen;

import common.network.NetworkHandler;
import common.network.NetworkProto.NetworkMessage;
import common.network.NetworkProto.NetworkMessageData;
import common.network.NetworkProto.NetworkMessage.Builder;
import common.util.VectorUtil;

public class ControlledTankBehavior extends TankBehavior {
	GameScreen screen;
	long lastShot;
	
	public ControlledTankBehavior(GameScreen screen) {
		this.screen = screen;
		lastShot = System.currentTimeMillis();
	}

	@Override
	public void update() {
		if (screen.kb().isDown(Keyboard.KEY_W)) {
			forward();
		}
		if (screen.kb().isDown(Keyboard.KEY_S)) {
			backward();
		}
		
		if (screen.kb().isDown(Keyboard.KEY_A)) {
			turnLeft();
		}
		if (screen.kb().isDown(Keyboard.KEY_D)) {
			turnRight();
		}
		
		
		if (screen.mh() != null) {
			Vector2f aim = new Vector2f(screen.getMouseX(),screen.getMouseY());
			aim = aim.translate(-entity.getX(), -entity.getY());
			entity.setAim(VectorUtil.getAngle(aim));
		}
		
		if (screen.kb().isDown(Keyboard.KEY_SPACE)) {
			long since = (System.currentTimeMillis() - lastShot);
			if (since > 250) {
				fire();
			}
		}
		
		super.update();
	}
	
	private void fire() {
		lastShot = System.currentTimeMillis();
		
		Builder nm = NetworkMessage.newBuilder().setType(NetworkMessage.Type.FIRE).addData(NetworkMessageData.newBuilder().setInt(entity.getId()).build());
		screen.nh().send(NetworkHandler.MESSAGE, nm.build().toByteArray());
	}
}
