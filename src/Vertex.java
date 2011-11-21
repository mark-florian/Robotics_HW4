
public class Vertex implements Comparable <Vertex> {
	private double xPos;
	private double yPos;
	private double angle;
	private int set;
	
	public Vertex(double x, double y, int s) {
		xPos = x;
		yPos = y;
		set = s;
	}
	
	public double getX() {
		return xPos;
	}
	public double getY() {
		return yPos;
	}
	
	public void setAngle(double a) {
		angle = a;
	}
	public double getAngle() {
		return angle;
	}
	
	public int getSet() {
		return set;
	}
	
	public boolean equals(Object o) {
		Vertex other = (Vertex) o;
		if(xPos == other.getX() && yPos == other.getY())
			return true;
		return false;
	}

	public int compareTo(Vertex v) {
		return (int) (this.angle - v.angle);
	}
}
