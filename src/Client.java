import java.net.Socket;

public class Client implements NetworkEventListener, Runnable {
	NetworkHandler nh;
	Thread t;

	public static void main(String[] args) {
		String host = "localhost";
		if (args.length > 0) {
			host = args[0];
		} else {
			System.out.println("No hostname given, assuming localhost");
		}
		new Client(host,7331);
	}

	public Client(String host, int port) {
		Log.setPrimary(Log.CLIENT);
		Socket s = NetworkHandler.getClientSocket(host,port);
		nh = new NetworkHandler(s);
		nh.addNetworkEventListener(this);
		t = new Thread(this);
		Log.p.out("Starting client thread");
		t.start();
	}

	public void run() {
		Window window = new Window();
		
		while(!window.shouldExit()) {
			window.update();
		}
		window.cleanup();

		
//		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
//		String line;
//		try {
//			while((line = stdin.readLine()) != null) {
//				ChatMessage message = null;
//				if (line.startsWith("/")) {
//					Log.p.out("Sending chat Command");
//					message = new ChatCommand(line);
//				} else {
//					message = new ChatMessage(line);
//				}
//				nh.send(message);
//			}
//		} catch (IOException e) {
//			Log.p.error("Error reading from user",e);
//		}
	}

	public void networkEventReceived(NetworkEvent e) {
		Object o = e.object;
		if (o instanceof ChatMessage) {
			ChatMessage message = (ChatMessage)o;
			Log.p.out("Got Message: " + message.toString());
		}
	}
}
