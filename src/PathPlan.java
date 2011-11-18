import java.io.*;
import java.util.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PathPlan extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	public static int NUM_OBS;
	private static Obstacle[] obstacles;
	private static Obstacle[] grownObs;
	private static ArrayList<Vertex> set = new ArrayList<Vertex>();
	private static Vertex[] start = new Vertex[2];
	public static double FRAME_WIDTH;
	public static double FRAME_HEIGHT;
	
	// Robot is octagon - ref is top-left-center
	private double oEdge = .1473;	// Size of one edge of octagon (meters)
	private double oSmall = oEdge / Math.sqrt(2);
	private double oMed = oSmall + oEdge;
	private double oLarge = oSmall + oEdge + oSmall;
//	private double oct1 = oe2;
//	private double oct2 = oe1 + oe2;
//	private double oct3 = oe1 + (2 * oe2);
//	private double octW1 = 
//	private double octW2
//	private double octW3
	
//	robHeight = .1;
//	private double robWidth = .1;	
//	private int robVerts = 4;
	
	// Create frame, panel, and textField
	private JFrame frame = new JFrame();
	private JPanel panel = new ObjectPanel(obstacles, start, false);
	
	// Create buttons
	private static JButton drawSet = new JButton("Obstacle Set");
	private static JButton growSet = new JButton("Grown Set");
	private static JButton visPath = new JButton("Visibility Graph");
	private static JButton safePath = new JButton("Safe Path");
	
	public PathPlan()
	{
		// Add elements to panel
		panel.add(drawSet);
		panel.add(growSet);
		panel.add(visPath);
		panel.add(safePath);
		
		JPanel buttons = new JPanel();
		buttons.add(drawSet);
		buttons.add(growSet);
		buttons.add(visPath);
		buttons.add(safePath);
		
		Dimension size = new Dimension((int)FRAME_WIDTH,(int)FRAME_HEIGHT);
		panel.setPreferredSize(size);
		frame.add(panel);

		frame.setLayout(new BorderLayout());
		frame.add(buttons, BorderLayout.NORTH);
		frame.add(panel, BorderLayout.CENTER);
	}
	
	public void launchFrame()
	{
		// Display Frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args)
	{	       
        // Make sure we can get screen size
        boolean headless = GraphicsEnvironment.isHeadless();
        
        if(headless == false)
        {
        	// Get screen size
    		Toolkit toolkit =  Toolkit.getDefaultToolkit();
            Dimension dim = toolkit.getScreenSize();
            
            // Use native screen size to set paint variables
        	System.out.printf("Native system has screen size %d x %d\nSetting frame size accordingly...\n", dim.width, dim.height);
        	FRAME_WIDTH = dim.width - 15;
            FRAME_HEIGHT = dim.height - 80;
        }
        else
        {
        	System.out.println("Native screen size not availeble\nSetting frame size to default 800 x 600");
        	FRAME_WIDTH = 800;
        	FRAME_HEIGHT = 600;
        }
        
		// Read in file
		try
		{
			FileInputStream fis = new FileInputStream("obstacles.txt");
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			
			String line;
			NUM_OBS = Integer.parseInt(br.readLine().trim());
			obstacles = new Obstacle[NUM_OBS];
			grownObs = new Obstacle[NUM_OBS];
			String regex = "[ ]+";
			int i=0;
			
			while((line = br.readLine()) != null) {
				int vertices = Integer.parseInt(line.trim());
				Vertex[] verts = new Vertex[vertices];
				
				for(int j=0; j<vertices; j++) {
					String[] point = br.readLine().split(regex);
					verts[j] = new Vertex(Double.parseDouble(point[0]), Double.parseDouble(point[1]));
				}
				obstacles[i] = new Obstacle(verts);
				i++;
			}
			
			fis.close();
			dis.close();
			br.close();
						
		} catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
			System.exit(0);
		}
		
		// Read in starting point
		try
		{
			FileInputStream fis = new FileInputStream("start.txt");
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			
			String regex = "[ ]+";
			String[] v1 = br.readLine().split(regex);
			String[] v2 = br.readLine().split(regex);
			
			start[0] = new Vertex(Double.parseDouble(v1[0]), Double.parseDouble(v1[1]));
			start[1] = new Vertex(Double.parseDouble(v2[0]), Double.parseDouble(v2[1]));
			
			fis.close();
			dis.close();
			br.close();
			
		} catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
			System.exit(0);
		}
		
		PathPlan map = new PathPlan();
		map.launchFrame();
		
		// Add action listeners to buttons
		drawSet.addActionListener(map);
		growSet.addActionListener(map);
		visPath.addActionListener(map);
		safePath.addActionListener(map);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		
		if(e.getSource().equals(drawSet)) {
			// Draw empty map
			JPanel barePanel = new ObjectPanel(obstacles, start, false); // add everything to panel
			frame.add(barePanel, BorderLayout.CENTER);
			frame.setVisible(true);
		}
		if(e.getSource().equals(growSet)) {
			// Get new data set
			for(int i=1; i<obstacles.length; i++) {
				Obstacle o = obstacles[i];
				Vertex[] v = o.getVertices();
//				Vertex[] newSet = new Vertex[robVerts * v.length];
//				ArrayList<Vertex> newSet = new ArrayList<Vertex>();
				set.clear();
				
				for(int j=0; j<v.length; j++) {
					set.add(v[j]);
					set.add(new Vertex(v[j].getX()-oEdge, v[j].getY()));
					set.add(new Vertex(v[j].getX()-oMed, v[j].getY()-oSmall));
					set.add(new Vertex(v[j].getX()-oMed, v[j].getY()-oMed));
					set.add(new Vertex(v[j].getX()-oEdge, v[j].getY()-oLarge));
					set.add(new Vertex(v[j].getX(), v[j].getY()-oLarge));
					set.add(new Vertex(v[j].getX()+oSmall, v[j].getY()-oMed));
					set.add(new Vertex(v[j].getX()+oSmall, v[j].getY()-oSmall));
					
//					set.add(new Vertex(v[j].getX()-robWidth, v[j].getY()));
//					set.add(new Vertex(v[j].getX(), v[j].getY()-robHeight));
//					set.add(new Vertex(v[j].getX()-robWidth, v[j].getY()-robHeight));
				}
				
				/* Now we have a new set of points for one obstacle
				 * Now, get convex hull!
				 */
				obstacles[i].setGrownVerts(getConvexHull());
			}
			
			// Draw connected map
			JPanel grownPanel = new ObjectPanel(obstacles, start, true);
			frame.add(grownPanel, BorderLayout.CENTER);
			frame.setVisible(true);
		}
		if (e.getSource().equals(visPath))
		{
//			// Draw minimum spanning tree
//			JPanel mstPanel = new CityPanel(cities, MST); // add everything to panel
//			frame.add(mstPanel);
//			frame.add(mstPanel, BorderLayout.CENTER);
//			frame.setVisible(true);
		}
	}
	
	private Vertex[] getConvexHull()
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
		
		// Sort points by angle using quicksort
		quickSort(0, set.size()-1);
		
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
			{
				stack.push(v2);
				
				// Store point in list containing non-hull points
				// This will be useful when implementing cheapest insertion
//				nonHull.add(v1);?
			}
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
	public double getAngle(Vertex a, Vertex b)
	{
		double x1 = a.getX();
		double y1 = a.getY();
		double x2 = b.getX();
		double y2 = b.getY();
		double angle = Math.atan2(y2 - y1, x2 - x1) * 180 / Math.PI;
		return Math.abs(angle);
	}
	
	/*
	 * Method used to sort an array of angles
	 * Operates on static variable 'points'
	 * @param left the left index to compare from
	 * @param right the right index to compare from
	 */
	public void quickSort(int left, int right)
	{
		int l = left;
		int r = right;
		
		// Use middle as pivot
		double pivot = set.get(left+((right-left)/2)).getAngle();
		
		// Divide into two lists
		while(l <= r)
		{
			// If current value from left < right, get next left
			while(set.get(l).getAngle() < pivot)
				l++;
			
			// If current value from right > left, get next right
			while(set.get(r).getAngle() > pivot)
				r--;
			
			// If we find a value in left > pivot and
			// value in right < pivot, swap them
			if(l <= r)
			{
				swap(l, r);
				l++;
				r--;
			}
		}
		
		// Recursive call
		if(left < r)
			quickSort(left, r);
		if(l < right)
			quickSort(l, right);
	}
	
	/*
	 * Helper method for quickSort()
	 * Swaps values of given indices
	 * @param left left index
	 * @param right right index
	 */
	public void swap(int left, int right)
	{
		Vertex p1 = set.get(left);
		Vertex p2 = set.get(right);
		set.set(left, p2);
		set.set(right, p1);
	}
	
	/*
	 * Helper method to calculate distance between two points
	 * @param a first point
	 * @param b second point
	 * @return double representing distance between points
	 */
	public double getDist(Vertex a, Vertex b)
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
	public boolean stricktlyLeft(Vertex first, Vertex second, Vertex test)
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
	
	private static double getDist(int x1, int y1, int x2, int y2)
	{
		double x = x2 - x1;
		double y = y2 - y1;
		x *= x;
		y *= y;
		double d = x + y;
		
		double dist = Math.sqrt(d);
		
		return dist;
	}
}