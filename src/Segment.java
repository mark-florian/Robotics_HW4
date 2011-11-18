
public class Segment {
	private double xPos1;
	private double yPos1;
	private double xPos2;
	private double yPos2;
	
	public Segment(Vertex v1, Vertex v2) {
		xPos1 = v1.getX();
		yPos1 = v1.getY();
		xPos2 = v2.getX();
		yPos2 = v2.getY();
	}
	
	public Segment(double x1, double y1, double x2, double y2) {
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
		
		Vertex a = new Vertex(this.getX1(), this.getY1());
		Vertex b = new Vertex(this.getX2(), this.getY2());
		Vertex c = new Vertex(s.getX2(), s.getY2());
		Vertex d = new Vertex(s.getX2(), s.getY2());
		
		return ccw(a,c,d) != ccw(b,c,d) && ccw(a,b,c) != ccw(a,b,d);
		

			
		
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
	private boolean ccw(Vertex a, Vertex b, Vertex c) {
		return (c.getY()-a.getY())*(b.getX()-a.getX()) > (b.getY()-a.getY())*(c.getX()-a.getX());
	}
}
