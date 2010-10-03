public interface Screen {
	void enter();
	void update(long ms);
	void render(Window w);
	void leave();
}
