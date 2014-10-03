import java.util.List;

import com.jking31cs.Corner;
import com.jking31cs.Edge;
import com.jking31cs.Graph;
import com.jking31cs.Point;
import com.jking31cs.Vector;

import processing.core.PApplet;


public class MyApplet extends PApplet {
	
	Graph g;
	
	@Override
	public void setup() {
		
		size(600,600);
		background(255);
		
		g = new Graph();
		Point p1 = new Point(100,100);
		g.addVertex(p1);
		Point p2 = new Point(300,300);
		g.addVertex(p2);
		Point p3 = new Point(100,200);
		g.addVertex(p3);
		Point p4 = new Point(30,75);
		g.addVertex(p4);
		
		g.addEdge(new Edge(p1,p2));
		g.addEdge(new Edge(p2,p3));
		g.addEdge(new Edge(p3,p1));
		g.addEdge(new Edge(p1,p4));
		
		drawGraph();
	}
	
	private void drawGraph() {
		int i = 0;
		
		//Draw points.
		for (Point p : g.points) {
			
			stroke(0);
			fill(255);
			ellipse(p.x, p.y, 25, 25);
			
			//Labels inside points
			fill(0);
			text("P" + Integer.toString(i), p.x - 5, p.y +5);
			i++;
		}
		
		//Draw Edges
		for (Edge e : g.getEdges()) {
			stroke(0);
			line(e.p1.x, e.p1.y, e.p2.x, e.p2.y);
		}		
		
	}

	public static void main(String[] args) {
		PApplet.main("MyApplet");
	}
}
