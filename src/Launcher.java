public class Launcher {
	public static void main(String[] args) {
		new Server(7331);
		new Client("localhost",7331);
	}
}
