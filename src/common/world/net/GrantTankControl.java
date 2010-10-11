package common.world.net;

import java.io.Serializable;

public class GrantTankControl extends Update implements Serializable {
	private static final long serialVersionUID = 3724160903399942477L;
	
	int id;
	
	public GrantTankControl(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}