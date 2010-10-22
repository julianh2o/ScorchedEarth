package client;

public class View {
	public float x;
	public float y;
	public float width;
	public float height;
	public float windowWidth;
	public float windowHeight;
	
	public View(float x, float y, float width, float height, float windowWidth, float windowHeight) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
	}


	public String toString() {
		return "{"+x+","+y+","+width+","+height+"}";
	}
}
