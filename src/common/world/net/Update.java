package common.world.net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public interface Update extends Serializable {
	public abstract void write(DataOutputStream o) throws IOException;
}
