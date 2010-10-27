package client;

public class View {
	public final float PIXEL_RATIO = 40;
	
	public float x;
	public float y;
	public float zoom;
	public float windowWidth;
	public float windowHeight;
	
	public View(float x, float y, float zoom, float windowWidth, float windowHeight) {
		centerOn(x,y);
		this.zoom = zoom;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
	}
	
	public View(Window w) {
		this(0,0,1,w.mode.getWidth(),w.mode.getHeight());
	}
	
	public void centerOn(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getLeft() {
		return x - windowWidth/getPixelsPerUnit();
	}
	
	public float getRight() {
		return x + windowWidth/getPixelsPerUnit();
	}
	
	public float getTop() {
		return y + windowHeight/getPixelsPerUnit();
	}
	
	public float getBottom() {
		return y - windowHeight/getPixelsPerUnit();
	}
	
	public float getPixelsPerUnit() {
		return (PIXEL_RATIO * zoom);
	}
	
	public float screenXtoGame(int screenx) {
		return getLeft() + screenx * getPixelsPerUnit();
	}
	
	public float screenYtoGame(int screenx) {
		return getLeft() + screenx * getPixelsPerUnit();
	}
	
	public boolean canSee(float x, float y) {
		if (x > getLeft() && x < getRight() && y < getTop() && y > getBottom()) return true;
		return false;
	}

	public String toString() {
		return "{ "+x+", "+y+" } (zoom: "+zoom+")";
	}
}
