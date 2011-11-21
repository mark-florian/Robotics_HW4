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
	private static Vertex[] startFinish = new Vertex[2];
	private static ArrayList<Vertex> nodes = new ArrayList<Vertex>();
	private static ArrayList<Vertex> unvisited = new ArrayList<Vertex>();
	
	public static double FRAME_WIDTH;
	public static double FRAME_HEIGHT;
	
	// Robot is octagon - ref is top-left-center
	private double oEdge = .1473;	// Size of one edge of octagon (meters)
	private double oSmall = oEdge / Math.sqrt(2);
	private double oMed = oSmall + oEdge;
	private double oLarge = oSmall + oEdge + oSmall;
	
	// Create frame, panel, and textField
	private JFrame frame = new JFrame();
	private JPanel panel = new ObjectPanel(obstacles, startFinish, false);
	
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
	
	public static void start()
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
			FileInputStream fis = new FileInputStream("test.txt");
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			
			String line;
			NUM_OBS = Integer.parseInt(br.readLine().trim());
			obstacles = new Obstacle[NUM_OBS];
			String regex = "[ ]+";
			int i=0;
			
			while((line = br.readLine()) != null) {
				int vertices = Integer.parseInt(line.trim());
				Vertex[] verts = new Vertex[vertices];
				
				for(int j=0; j<vertices; j++) {
					String[] point = br.readLine().split(regex);
					verts[j] = new Vertex(Double.parseDouble(point[0]), Double.parseDouble(point[1]), i);
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
			
			startFinish[0] = new Vertex(Double.parseDouble(v1[0]), Double.parseDouble(v1[1]), -1);
			startFinish[1] = new Vertex(Double.parseDouble(v2[0]), Double.parseDouble(v2[1]), -2);
			
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
		if(e.getSource().equals(drawSet)) {
			// Draw empty map
			JPanel barePanel = new ObjectPanel(obstacles, startFinish, false); // add everything to panel
			frame.add(barePanel, BorderLayout.CENTER);
			frame.setVisible(true);
		}
		if(e.getSource().equals(growSet)) {
			// Get new data set
			for(int i=1; i<obstacles.length; i++) {
				Obstacle o = obstacles[i];
				Vertex[] v = o.getVertices();
				ArrayList<Vertex> set = new ArrayList<Vertex>();
				
				for(int j=0; j<v.length; j++) {
					set.add(v[j]);
					set.add(new Vertex(v[j].getX()-oEdge, v[j].getY(), i));
					set.add(new Vertex(v[j].getX()-oMed, v[j].getY()-oSmall, i));
					set.add(new Vertex(v[j].getX()-oMed, v[j].getY()-oMed, i));
					set.add(new Vertex(v[j].getX()-oEdge, v[j].getY()-oLarge, i));
					set.add(new Vertex(v[j].getX(), v[j].getY()-oLarge, i));
					set.add(new Vertex(v[j].getX()+oSmall, v[j].getY()-oMed, i));
					set.add(new Vertex(v[j].getX()+oSmall, v[j].getY()-oSmall, i));
				}
				
				/* Now we have a new set of points for one obstacle
				 * Now, get convex hull!
				 */
				obstacles[i].setGrownVerts(Utilities.getConvexHull(set), i);
			}
			
			// Draw connected map
			JPanel grownPanel = new ObjectPanel(obstacles, startFinish, true);
			frame.add(grownPanel, BorderLayout.CENTER);
			frame.setVisible(true);
		}
		if (e.getSource().equals(visPath)) {
			// Get all vertices and grown obstacle segments
			ArrayList<Vertex> vertices = new ArrayList<Vertex>();
			vertices.add(startFinish[0]);
			vertices.add(startFinish[1]);
			ArrayList<Segment> segments = new ArrayList<Segment>();
			for(int i=1; i<obstacles.length; i++) {
				Vertex[] verts = obstacles[i].getGrownVertices();
				Segment[] segs = obstacles[i].getGrownSegments();
				for(Vertex v : verts)
					vertices.add(v);
				for(Segment s : segs)
					segments.add(s);
			}
			// We have to also add boundaries to intersecting segments
			for(Segment s : obstacles[0].getSegments())
				segments.add(s);
			
			ArrayList<Segment> safePath = new ArrayList<Segment>();
			
			// For every vertex pair, check for intersection
			for(int i=0; i<vertices.size(); i++) {
				Vertex node = vertices.get(i);
				for(int j=i+1; j<vertices.size(); j++) {
					Vertex otherNode = vertices.get(j);
//					if(vertices.get(i).getSet() != vertices.get(j).getSet() || vertices.get(i).sameLine(vertices.get(j))) {
					if(node.getSet() != otherNode.getSet() || node.sameLine(otherNode)) {
						boolean intersects = false;
						Segment pointSeg = new Segment(node, otherNode);
						for(int k=0; k<segments.size(); k++) {
							if(pointSeg.intersects(segments.get(k))) {
								intersects = true;
								break;
							}
						}
						if (!intersects) {
							safePath.add(pointSeg);
							node.addAdjacentSeg(pointSeg);
							node.addNeighbor(otherNode);
							otherNode.addNeighbor(node);
						}
					}
				}
				nodes.add(node);
			}
			
			// Draw visibility map
			JPanel grownPanel = new ObjectPanel(obstacles, startFinish, true, safePath);
			frame.add(grownPanel, BorderLayout.CENTER);
			frame.setVisible(true);
		}
		if (e.getSource().equals(safePath)) {
			Vertex current = nodes.get(0);
			current.setVisited(true);
			current.setMinDistance(0);
			ArrayList<Vertex> sourceNeighbors = current.getNeighbors();
			for(Vertex v : sourceNeighbors) {
				v.setMinDistance(getDist(current, v));
				v.setMinNode(current);
			}
			
			Dijkstra(current);
			
			for(Vertex v : nodes)
				System.out.println(v.getMinDistance());
			
			Vertex start = nodes.get(0);	// Start node
			Vertex v = nodes.get(1);	// Goal node
			ArrayList<Segment> bestPath = new ArrayList<Segment>();
			System.out.println("before segs");
			while(v != start) {
				bestPath.add(new Segment(v, v.getMinNode()));
				v = v.getMinNode();
			}
			System.out.println("before segs");
			
			// Draw best path
			JPanel grownPanel = new ObjectPanel(obstacles, startFinish, true, null, bestPath);
			frame.add(grownPanel, BorderLayout.CENTER);
			frame.setVisible(true);
		}
	}
	
	private void Dijkstra(Vertex source) {
		while(nodesAreUnvisited()) {
			// Find closest vertex that is unvisited
			Vertex current = getClosestUnvisited();
			current.setVisited(true);
			ArrayList<Vertex> neighbors = current.getUnvisitedNeighbors();
			for(Vertex v : neighbors) {
				double distSV = current.getMinDistance();
//				double distSV = getDist(source, current);
				double distVW = getDist(current, v);
				double distSW = getDist(source, v);
				
				if((distSV + distVW) < distSW) {
					System.out.println(distSV + distVW);
					v.setMinDistance(distSV + distVW);
					v.setMinNode(current);
				}
			}
		}
	}
	
	private boolean nodesAreUnvisited() {
		for(Vertex v : nodes)
			if(!v.isVisited())
				return true;
		return false;
	}
	
	private double getDist(Vertex v1, Vertex v2) {
		if(!v1.hasNeighbor(v2))
			return 9999999;
		return Math.sqrt(Math.pow(v2.getX()-v1.getX(),2) + Math.pow(v2.getY()-v1.getY(),2));
	}
	
	private Vertex getClosestUnvisited() {
		double dist = 9999999;
		Vertex ret = null;
		for(Vertex v : nodes) {
			if(!v.isVisited()) {
				if(v.getMinDistance() < dist) {
					ret = v;
					dist = v.getMinDistance();
				}
			}
		}
		return ret;
	}
	
	private Vertex getMinFromUnvisited() {
		double min = 9999999;
		Vertex ret = null;
		for(Vertex v : unvisited)
			if(v.getMinDistance() < min) {
				min = v.getMinDistance();
				ret = v;
			}
		return ret;
	}
}