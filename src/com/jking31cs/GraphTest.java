package com.jking31cs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;

public class GraphTest {

	@Test
	public void testAddVertices() {
		Graph g = new Graph();
		
		g.addVertex(new Point(10,10));
		g.addVertex(new Point(20,20));
		g.addVertex(new Point(15,15));
		
		assertEquals(3, g.points.size());
		
		assertEquals(new Point(10,10), g.points.get(0));
		assertEquals(new Point(20,20), g.points.get(1));
		assertEquals(new Point(15,15), g.points.get(2));
	}

	@Test
	public void testLoopNotOn0Index() {

		Graph g = new Graph();
		Point[] p = new Point[] {
				new Point(100, 100),
				new Point(50, 200),
				new Point(300, 200),
				new Point(350, 175),
				new Point(300, 100)
		};
		for (Point point : p) {
			g.addVertex(point);
		}
		g.addEdge(new Edge(p[0],p[1]));
		g.addEdge(new Edge(p[1],p[2]));
		g.addEdge(new Edge(p[2],p[3]));
		g.addEdge(new Edge(p[3],p[4]));
		g.addEdge(new Edge(p[4],p[0]));

		assertEquals(2, g.h.size());
		assertEquals(10, g.corners.size());

		g.addEdge(new Edge(p[1], p[4]));

		assertEquals(3, g.h.size());
		assertEquals(12, g.corners.size());
		
		assertEquals(3, g.getFacePaths().size());
	}
	
	@Test
	public void testDanglingEdge() {
		Graph g = new Graph();
		Point p1 = new Point(50,50);
		g.addVertex(p1);
		Point p2 = new Point(60,60);
		g.addVertex(p2);
		Point p3 = new Point(40,60);
		g.addVertex(p3);
		Point p4 = new Point(30,30);
		g.addVertex(p4);
		
		g.addEdge(new Edge(p1,p2));
		g.addEdge(new Edge(p2,p3));
		g.addEdge(new Edge(p3,p1));
		g.addEdge(new Edge(p1,p4));
		
		assertEquals(2, g.h.size());

        //Testing corners here.
        assertEquals(8, g.corners.size());

        assertEquals(new CornerIndexInfo(0,2,5), g.corners.get(0));
        assertEquals(new CornerIndexInfo(1,5,2), g.corners.get(1));
        assertEquals(new CornerIndexInfo(2,4,1), g.corners.get(2));
        assertEquals(new CornerIndexInfo(3,1,4), g.corners.get(3));
        assertEquals(new CornerIndexInfo(4,6,3), g.corners.get(4));
        assertEquals(new CornerIndexInfo(5,3,6), g.corners.get(5));
        assertEquals(new CornerIndexInfo(6,7,0), g.corners.get(6));
        assertEquals(new CornerIndexInfo(7,0,null), g.corners.get(7));

	}
	
	@Test
	public void testAddEdges() {
		Graph g = new Graph();
		Point p1 = new Point(50,50);
		g.addVertex(p1);
		Point p2 = new Point(60,60);
		g.addVertex(p2);
		Point p3 = new Point(40,60);
		g.addVertex(p3);
		
		g.addEdge(new Edge(p1,p2));
		g.addEdge(new Edge(p2,p3));
		
		assertEquals(4, g.v.size());
		assertEquals(4, g.n.size());
		assertEquals(4, g.o.size());
		assertEquals(4, g.f.size());
		
		assertEquals(g.n.get(0), (Integer) 2);
		assertEquals(g.n.get(1), (Integer) 0);
		assertEquals(g.n.get(2), (Integer) 3);
		assertEquals(g.n.get(3), (Integer) 1);
		
		assertEquals(g.f.get(0), (Integer) 0);
		assertEquals(g.f.get(1), (Integer) 0);
		assertEquals(g.f.get(2), (Integer) 0);
		assertEquals(g.f.get(3), (Integer) 0);
		
		g.addEdge(new Edge(p3, p1));
		
		assertEquals(6, g.f.size());
		
		assertTrue(g.f.get(0).equals(g.f.get(2)) && g.f.get(2).equals(g.f.get(4)));
		assertTrue(g.f.get(1).equals(g.f.get(3)) && g.f.get(3).equals(g.f.get(5)));
		assertFalse(g.f.get(0).equals(g.f.get(1)));
		
		assertEquals(2, g.h.size());

	}

	@Test
	public void testOuterFaceDetection() {
		Graph g = new Graph();
		Point p0 = new Point(225, 100);
		g.addVertex(p0);
		Point p1 = new Point(300, 350);
		g.addVertex(p1);
		Point p2 = new Point(150, 350);
		g.addVertex(p2);
		Point p3 = new Point(225, 150);
		g.addVertex(p3);
		Point p4 = new Point(275, 275);
		g.addVertex(p4);
		Point p5 = new Point(175, 275);
		g.addVertex(p5);

		g.addEdge(new Edge(p0, p1));
		g.addEdge(new Edge(p1, p2));
		g.addEdge(new Edge(p2, p0));
		g.addEdge(new Edge(p0, p3));
		g.addEdge(new Edge(p3, p4));
		g.addEdge(new Edge(p4, p5));
		g.addEdge(new Edge(p5, p3));

		assertEquals(3, g.getFacePaths().size());

		assertEquals((Integer) 0, g.getOuterFaceIndex());
	}

	@Test
	public void testAreas() {
		Point[] p = new Point[] {
			new Point(50,50),
			new Point(50,150),
			new Point(150,150)
		};

		Graph g = new Graph();
		g.addVertex(p[0]);
		g.addVertex(p[1]);
		g.addVertex(p[2]);

		g.addEdge(new Edge(p[0],p[1]));
		g.addEdge(new Edge(p[1],p[2]));
		g.addEdge(new Edge(p[2],p[0]));

		assertEquals(2, g.getFacePaths().size());
		assertEquals(2, g.faceAreas().size());

		assertTrue(g.faceAreas().get(1) == null);
		assertEquals(5000, g.faceAreas().get(0), 5);

		p = Arrays.copyOf(p, 4);
		p[3] = new Point(150,50);
		g.addVertex(p[3]);
		g.addEdge(new Edge(p[2],p[3]));
		g.addEdge(new Edge(p[3], p[0]));

		assertEquals(3, g.getFacePaths().size());
		assertEquals(3, g.faceAreas().size());

		for (int i = 0; i < 3; i++) {
			if (g.getOuterFaceIndex() == i) {
				assertTrue(g.faceAreas().get(i) == null);
			} else {
				assertEquals(5000, g.faceAreas().get(i), 5);
			}

		}
	}

}
