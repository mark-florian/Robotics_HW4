import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Utilities {
	public static Vertex[] getConvexHull(ArrayList<Vertex> set)
	{
		// Find rightmost, lowest point
		Vertex v0 = set.get(0);
		for(int i=1; i<set.size(); i++) {
			Vertex vi = set.get(i);
			
			if(vi.getY() >= v0.getY())
				if(vi.getY() == v0.getY()) {
					if(vi.getX() > v0.getX())
						v0 = vi;
				}else
					v0 = set.get(i);
		}
		
		// Find angle between each point and p0
		for(int i=0; i<set.size(); i++)
			set.get(i).setAngle(getAngle(v0, set.get(i)));
		
		// Sort points by angle (Comparable defined in Vertex)
		Collections.sort(set);
		
		// Check for points with same angle, if found, give priority to closer point
		for(int i=1; i<set.size()-1; i++) {
			if(set.get(i).getAngle() == set.get(i+1).getAngle())
				if(getDist(set.get(0),set.get(i)) < getDist(set.get(0),set.get(i)))
				{
					// Swap points
					Vertex v1 = set.get(i);
					Vertex v2 = set.get(i+1);
					
					set.set(i, v2);
					set.set(i+1, v1);
				}
		}
		
		/* Create new stack to get new edges */
		Stack<Vertex> stack = new Stack<Vertex>();
		stack.push(set.get(set.size()-1));
		stack.push(set.get(0));
		
		int k=1;
		while(k<set.size())
		{
			// If P(i) is strictly left, push onto stack, otherwise discard
			Vertex v1 = stack.pop();
			Vertex v2 = stack.pop();
			boolean isLeft = stricktlyLeft(v1, v2, set.get(k));
			
			if(isLeft)
			{
				stack.push(v2);
				stack.push(v1);
				stack.push(set.get(k));
				k++;
			}		
			else
				stack.push(v2);
		}
		
		// Add all vertex to grown set
		Vertex[] newVerts = new Vertex[stack.size()];
		for(int i=0; i<stack.size(); i++)
			newVerts[i] = stack.get(i);
		
		return newVerts;
	}
	
	/*
	 * Method to calculate angle between two points
	 * @param a first point
	 * @param b second point
	 * @return double representation of angle between points with reference to horizontal plane
	 */
	private static double getAngle(Vertex a, Vertex b)
	{
		double x1 = a.getX();
		double y1 = a.getY();
		double x2 = b.getX();
		double y2 = b.getY();
		double angle = Math.atan2(y2 - y1, x2 - x1) * 180 / Math.PI;
		return Math.abs(angle);
	}
	
	/*
	 * Helper method to calculate distance between two points
	 * @param a first point
	 * @param b second point
	 * @return double representing distance between points
	 */
	private static double getDist(Vertex a, Vertex b)
	{
		double x1 = a.getX();
		double y1 = a.getY();
		double x2 = b.getX();
		double y2 = b.getY();
		
		double x = x2 - x1;
		double y = y2 - y1;
		x *= x;
		y *= y;
		double d = x + y;
		
		double dist = Math.sqrt(d);
		
		return dist;
	}
	
	/*
	 * Helper method to determine if test point is stricktly left of two other points
	 * Calculates cross product of three points
	 * @param first point in hull array
	 * @param second point in hull array
	 * @param test point under consideration
	 * @return boolean stating whether cross product is positive or negative (strictly left of not)
	 */
	private static boolean stricktlyLeft(Vertex first, Vertex second, Vertex test)
	{
		double x1 = first.getX();
		double y1 = first.getY();
		double x2 = second.getX();
		double y2 = second.getY();
		double x3 = test.getX();
		double y3 = test.getY();
		
		double cross = (x2-x1)*(y3-y1)-(x3-x1)*(y2-y1);
		if(cross <= 0)
			return false;
		return true;
	}
}