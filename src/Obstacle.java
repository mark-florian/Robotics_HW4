
public class Obstacle {
	private Vertex[] vertices;
	private Vertex[] grownVertices;
	private Segment[] segments;
	private Segment[] grownSegments;
	private int set;	// This distinguished obstacles from others (used for disjoint set)

	public Obstacle(Vertex[] v)
	{
		vertices = v;
//		grownVertices = new Vertex[v.length];
		segments = new Segment[v.length];
//		grownSegments = new Segment[v.length];
		setSegments(v);
	}
	
	public void setGrownVerts(Vertex[] v, int s)
	{
		grownVertices = v;
		grownSegments = new Segment[v.length];
		setGrownSegments(v);
		set = s;
	}
	
	/*
	 * Helper method to set segments for drawing
	 */
	private void setSegments(Vertex[] v)
	{
		for(int i=0; i<vertices.length-1; i++) {
			segments[i] = new Segment(v[i].getX(), v[i].getY(), v[i+1].getX(), v[i+1].getY());
		}
		segments[segments.length-1] = new Segment(v[v.length-1].getX(), v[v.length-1].getY(), v[0].getX(), v[0].getY());
	}
	private void setGrownSegments(Vertex[] v)
	{
		for(int i=0; i<grownVertices.length-1; i++) {
			grownSegments[i] = new Segment(v[i].getX(), v[i].getY(), v[i+1].getX(), v[i+1].getY());
		}
		grownSegments[grownSegments.length-1] = new Segment(v[v.length-1].getX(), v[v.length-1].getY(), v[0].getX(), v[0].getY());
	}
	
	public Vertex[] getVertices() {
		return vertices;
	}
	public Vertex[] getGrownVertices() {
		return grownVertices;
	}
	public Segment[] getSegments() {
		return segments;
	}
	public Segment[] getGrownSegments() {
		return grownSegments;
	}
}
