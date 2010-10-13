package common.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

import common.world.net.Update;

public class ClientReady implements Update, Serializable {
	private static final long serialVersionUID = 8764396787591760074L;
	
	public ClientReady() {
		
	}

	@Override
	public void write(DataOutputStream o) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
