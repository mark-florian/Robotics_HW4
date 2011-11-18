
public class Segment {
	private double xPos1;
	private double yPos1;
	private double xPos2;
	private double yPos2;
	
	public Segment(Vertex[] v)
	{
		
	}
	
	public Segment(double x1, double y1, double x2, double y2)
	{
		xPos1 = x1;
		yPos1 = y1;
		xPos2 = x2;
		yPos2 = y2;
	}
	
	public double getX1()
	{
		return xPos1;
	}
	public double getY1()
	{
		return yPos1;
	}
	public double getX2()
	{
		return xPos2;
	}
	public double getY2()
	{
		return yPos2;
	}

}
