package common.world.net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class GrantTankControl implements Update, Serializable {
	private static final long serialVersionUID = 3724160903399942477L;
	
	int id;
	
	public GrantTankControl(int id) {
		super();
		this.id = id;
	}
	
	@Override
	public void write(DataOutputStream o) throws IOException {
		o.write(id);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}