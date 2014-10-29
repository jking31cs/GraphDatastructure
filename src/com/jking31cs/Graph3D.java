package com.jking31cs;

import java.util.ArrayList;
import java.util.List;


public class Graph3D extends Graph {
	List<Integer> oppV = new ArrayList<Integer>();
	
	@Override
	public void addVertex(Point p) {
		super.addVertex(p);
		Point otherPoint = new Point (p.x, p.y, p.z + 100);
		super.addVertex(otherPoint);
		oppV.add(oppV.size() + 1);
		oppV.add(oppV.size() - 1);
		super.addEdge(new Edge(p, otherPoint));
	}
	
	@Override
	public void addEdge(Edge e) {
		super.addEdge(e);
		Edge skewEdge = new Edge(
			this.points.get(oppV.get(points.indexOf(e.p1))),
			this.points.get(oppV.get(points.indexOf(e.p2)))
		);
		super.addEdge(skewEdge);
	}
}
