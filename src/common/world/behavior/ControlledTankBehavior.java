package common.world.behavior;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import common.input.KeyboardHandler;
import common.input.MouseHandler;
import common.util.Log;
import common.util.VectorUtil;
public class ControlledTankBehavior extends TankBehavior {
	KeyboardHandler kb;
	MouseHandler mh;
	
	public ControlledTankBehavior(KeyboardHandler kb, MouseHandler mh) {
		this.kb = kb;
		this.mh = mh;
	}

	@Override
	public void update() {
		if (kb.isDown(Keyboard.KEY_W)) {
			forward();
		}
		if (kb.isDown(Keyboard.KEY_S)) {
			backward();
		}
		
		if (kb.isDown(Keyboard.KEY_A)) {
			turnLeft();
		}
		if (kb.isDown(Keyboard.KEY_D)) {
			turnRight();
		}
		
		
		if (mh != null) {
			Vector2f aim = new Vector2f(mh.getX(),mh.getY());
//			Log.p.out("MH: "+aim.toString());
			aim = aim.translate(-entity.getX(), -entity.getY());
//			Log.p.out("AIM: "+aim.toString());
			entity.setAim(VectorUtil.getAngle(aim));
		}
		
		super.update();
	}
}
