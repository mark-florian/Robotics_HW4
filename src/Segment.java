import java.awt.geom.Line2D;

public class Segment {
	private Vertex v1;
	private Vertex v2;
	private double xPos1;
	private double yPos1;
	private double xPos2;
	private double yPos2;
	
	public Segment(Vertex ver1, Vertex ver2) {
		v1 = ver1;
		v2 = ver2;
		xPos1 = v1.getX();
		yPos1 = v1.getY();
		xPos2 = v2.getX();
		yPos2 = v2.getY();
	}
	
	public Segment(double x1, double y1, double x2, double y2) {
		v1 = new Vertex(x1, y1, 0);
		v2 = new Vertex(x2, y2, 0);
		xPos1 = x1;
		yPos1 = y1;
		xPos2 = x2;
		yPos2 = y2;
	}
	
	public double getX1() {
		return xPos1;
	}
	public double getY1() {
		return yPos1;
	}
	public double getX2() {
		return xPos2;
	}
	public double getY2() {
		return yPos2;
	}
	public Vertex getV1() {
		return v1;
	}
	public Vertex getV2() {
		return v2;
	}
	
	public double getSlope() {
		return (yPos2-yPos1) / (xPos2-xPos1);
	}
	public double getB() {
		return yPos1 - (this.getSlope() * xPos1);
	}
	
	
	public boolean intersects(Segment s) {
//		double LHS = this.getSlope() - s.getSlope();
//		double RHS = s.getB() - this.getB();
//		double x = RHS / LHS;
//		double y = (this.getSlope() * x) + this.getB();
		Line2D seg1 = new Line2D.Double(this.getX1(), this.getY1(), this.getX2(), this.getY2());
		Line2D seg2 = new Line2D.Double(s.getX1(), s.getY1(), s.getX2(), s.getY2());
		
		if(seg1.intersectsLine(seg2)) {
			if(onlyOnePointIntersects(this, s))
				return false;
			return true;
		}
		return false;
		
//		Vertex a = new Vertex(this.getX1(), this.getY1(), 0);
//		Vertex b = new Vertex(this.getX2(), this.getY2(), 0);
//		Vertex c = new Vertex(s.getX2(), s.getY2(), 0);
//		Vertex d = new Vertex(s.getX2(), s.getY2(), 0);
//		
//		return (ccw(a,c,d) != ccw(b,c,d)) && (ccw(a,b,c) != ccw(a,b,d));
		

//		return CCW(a, c, d) = CCW(b, c, d) ∧ CCW(a, b, c) = CCW(a, b, d)

		
//		if(xPos1>=x && xPos2<=x && yPos1>=y && yPos2<=y)
//			return true;
//		return false;
//		
//		if(xPos1<x)
//			return false;
//		if(xPos2>x)
//			return false;
//		if(yPos1<y)
//			return false;
//		if(yPos2>y)
//			return false;
//		
//		return true;
		/*
		#!/usr/bin/python

		class Point:
			def __init__(self,x,y):
				self.x = x
				self.y = y

		def ccw(A,B,C):
			return (C.y-A.y)*(B.x-A.x) > (B.y-A.y)*(C.x-A.x)

		def intersect(A,B,C,D):
			return ccw(A,C,D) != ccw(B,C,D) and ccw(A,B,C) != ccw(A,B,D)


		a = Point(0,0)
		b = Point(0,1)
		c = Point(1,1)
		d = Point(1,0)


		print intersect(a,b,c,d)
		print intersect(a,c,b,d)
		print intersect(a,d,b,c)
		*/
	}
	private boolean onlyOnePointIntersects(Segment seg1, Segment seg2) {
		if(seg1.getV1().equals(seg2.getV1()))
			return true;
		if(seg1.getV1().equals(seg2.getV2()))
			return true;
		if(seg1.getV2().equals(seg2.getV1()))
			return true;
		if(seg1.getV2().equals(seg2.getV2()))
			return true;
		return false;
	}
	private boolean ccw(Vertex a, Vertex b, Vertex c) {
		return (c.getY()-a.getY())*(b.getX()-a.getX()) > (b.getY()-a.getY())*(c.getX()-a.getX());
	}
}
