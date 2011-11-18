
public class Vertex {
	private double xPos;
	private double yPos;
	private double angle;
	
	public Vertex(double x, double y)
	{
		xPos = x;
		yPos = y;
	}
	
	public double getX()
	{
		return xPos;
	}
	public double getY()
	{
		return yPos;
	}
	
	public void setAngle(double a)
	{
		angle = a;
	}
	public double getAngle()
	{
		return angle;
	}
}