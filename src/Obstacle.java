
public class Obstacle {
	private Vertex[] vertices;
	private Vertex[] grownVertices;
	private Segment[] segments;
	private Segment[] grownSegments;

	public Obstacle(Vertex[] v)
	{
		vertices = v;
//		grownVertices = new Vertex[v.length];
		segments = new Segment[v.length];
//		grownSegments = new Segment[v.length];
		getSegments(v);
	}
	
	public void setGrownVerts(Vertex[] v)
	{
		grownVertices = v;
		grownSegments = new Segment[v.length];
		getGrownSegments(v);
	}
	
	/*
	 * Helper method to set segments for drawing
	 */
	private void getSegments(Vertex[] v)
	{
		for(int i=0; i<vertices.length-1; i++) {
			segments[i] = new Segment(v[i].getX(), v[i].getY(), v[i+1].getX(), v[i+1].getY());
		}
		segments[segments.length-1] = new Segment(v[v.length-1].getX(), v[v.length-1].getY(), v[0].getX(), v[0].getY());
	}
	private void getGrownSegments(Vertex[] v)
	{
		for(int i=0; i<grownVertices.length-1; i++) {
			grownSegments[i] = new Segment(v[i].getX(), v[i].getY(), v[i+1].getX(), v[i+1].getY());
		}
		grownSegments[grownSegments.length-1] = new Segment(v[v.length-1].getX(), v[v.length-1].getY(), v[0].getX(), v[0].getY());
	}
	
	public Vertex[] getVertices()
	{
		return vertices;
	}
	public Segment[] getSegments()
	{
		return segments;
	}
	public Segment[] getGrownSegments()
	{
		return grownSegments;
	}
	
//	public int getX()
//	{
//		return xPos;
//	}
//	public int getY()
//	{
//		return yPos;
//	}
//	public City getParent()
//	{
//		return parent;
//	}
//	public void setParent(City p)
//	{
//		this.parent = p;
//	}
}