import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.PriorityQueue;

import javax.swing.JPanel;

public class ObjectPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private Obstacle[] obs = new Obstacle[PathPlan.NUM_OBS];
	private Vertex[] start = new Vertex[2];	// Start/end point
	private boolean grown = false;
	private ArrayList<Segment> segments = new ArrayList<Segment>();
		
	public ObjectPanel(Obstacle[] o, Vertex[] s, boolean g)
	{
		obs = o;
		start = s;
		setBackground(Color.white);
		setForeground(Color.black);
		grown = g;
		segments = null;
	}
	
	public ObjectPanel(Obstacle[] o, Vertex[] s, boolean g, ArrayList<Segment> segs)
	{
		obs = o;
		start = s;
		setBackground(Color.white);
		setForeground(Color.black);
		grown = g;
		segments = segs;
	}
	
	protected void paintComponent(Graphics g)
	{
		// Colors the window
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		// Set foreground colors
		g.setColor(getForeground());
		
		// Set font
		Font MyFont = new Font("SansSerif",Font.PLAIN,(int)(PathPlan.FRAME_WIDTH/100));
		g.setFont(MyFont);
		     
		// Call method to draw map
		this.drawMap(g);
		revalidate();
	}
	
	// Draws the map
	public void drawMap(Graphics g) 
	{
		// Declare constants
		double SCREEN_WIDTH = PathPlan.FRAME_WIDTH * .92; //875;
		double SCREEN_HEIGHT = PathPlan.FRAME_HEIGHT * .92;
		
		double X_SCALE = SCREEN_WIDTH / 15;
		double Y_SCALE = SCREEN_HEIGHT / 10;
		int OFFSET = 400;

		// Paint bare map
		double x1, y1, x2, y2;
		for(int i=0; i<obs.length; i++) {
			Obstacle o = obs[i];
			Segment[] s = o.getSegments();
			for(int j=0; j<s.length; j++) {
				x1 = s[j].getX1() * X_SCALE + OFFSET;
				y1 = s[j].getY1() * Y_SCALE + OFFSET;
				x2 = s[j].getX2() * X_SCALE + OFFSET;
				y2 = s[j].getY2() * Y_SCALE + OFFSET;
				g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
			}
		}
		
		// Start/Finish
		x1 = start[0].getX() * X_SCALE + OFFSET;
		y1 = start[0].getY() * X_SCALE + OFFSET;
		x2 = start[1].getX() * X_SCALE + OFFSET;
		y2 = start[1].getY() * X_SCALE + OFFSET;
		g.drawRect((int) x1, (int) y1, 10, 10);
		g.drawRect((int) x2, (int) y2, 10, 10);
		
		/* Draw grown set if it exists */
		if(grown == true)
		{
			g.setColor(Color.BLUE);
			for(int i=1; i<obs.length; i++) {
				Obstacle o = obs[i];
				Segment[] s = o.getGrownSegments();
				for(int j=0; j<s.length; j++) {
					x1 = s[j].getX1() * X_SCALE + OFFSET;
					y1 = s[j].getY1() * Y_SCALE + OFFSET;
					x2 = s[j].getX2() * X_SCALE + OFFSET;
					y2 = s[j].getY2() * Y_SCALE + OFFSET;
					g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
				}
			}
		}
		
		if(segments != null) {
			g.setColor(Color.RED);
			for(int i=0; i<segments.size(); i++) {
				x1 = segments.get(i).getX1() * X_SCALE + OFFSET;
				y1 = segments.get(i).getY1() * Y_SCALE + OFFSET;
				x2 = segments.get(i).getX2() * X_SCALE + OFFSET;
				y2 = segments.get(i).getY2() * Y_SCALE + OFFSET;
				g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
			}
		}
	}
}
