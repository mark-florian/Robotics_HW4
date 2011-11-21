import java.util.*;

public class Vertex implements Comparable <Vertex> {
	private double xPos;
	private double yPos;
	private double angle;
	private int set;
	private ArrayList<Segment> adjacentSegs;
	private ArrayList<Vertex> neighbors;
	private boolean visited = false;
	private double minDistance = 9999999;
	private Vertex minNode;
	
	public Vertex(double x, double y, int s) {
		xPos = x;
		yPos = y;
		set = s;
		adjacentSegs = new ArrayList<Segment>();
		neighbors = new ArrayList<Vertex>();
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
	public void addNeighbor(Vertex v) {
		neighbors.add(v);
	}
	public ArrayList<Vertex> getNeighbors() {
		return neighbors;
	}
	public ArrayList<Vertex> getUnvisitedNeighbors() {
		ArrayList<Vertex> ret = new ArrayList<Vertex>();
		for(Vertex v : neighbors)
			if(!v.isVisited())
				ret.add(v);
		return ret;
	}
	public boolean hasNeighbor(Vertex other) {
		for(Vertex v : neighbors)
			if(v == other)
				return true;
		return false;
	}
	public void setMinNode(Vertex v) {
		minNode = v;
	}
	public Vertex getMinNode() {
		return minNode;
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
