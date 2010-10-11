package client;

public interface Screen {
	void enter();
	void update();
	void render(Window w);
	void leave();
}
