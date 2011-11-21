import java.util.*;

public class Vertex implements Comparable <Vertex> {
	private double xPos;
	private double yPos;
	private double angle;
	private int set;
	private ArrayList<Segment> adjacentSegs;
	private boolean visited = false;
	private double minDistance = 999999;
	
	public Vertex(double x, double y, int s) {
		xPos = x;
		yPos = y;
		set = s;
		adjacentSegs = new ArrayList<Segment>();
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
	
	public void addAdjacentSeg(Segment s) {
		adjacentSegs.add(s);
	}
	public ArrayList<Segment> getAdjacentSegs() {
		return adjacentSegs;
	}
	public void setVisited(boolean v) {
		visited = v;
	}
	public void setMinDistance(double d) {
		minDistance = d;
	}
	public double getMinDistance() {
		return minDistance;
	}
	public boolean isVisited() {
		return visited;
	}
	
	public boolean sameLine(Vertex other) {
		if(xPos == other.getX() || yPos == other.getY())
			return true;
		return false;
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
