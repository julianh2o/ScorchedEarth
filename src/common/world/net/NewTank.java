package common.world.net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class NewTank implements Update, Serializable {
	private static final long serialVersionUID = 5044130946286460848L;

	int id;
	int model;
	boolean control;

	public NewTank(int id, int model, boolean control) {
		super();
		this.id = id;
		this.model = model;
		this.control = control;
	}
	
	@Override
	public void write(DataOutputStream o) throws IOException {
		o.write(id);
		o.write(model);
		o.writeBoolean(control);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public boolean isControl() {
		return control;
	}

	public void setControl(boolean control) {
		this.control = control;
	}
}
