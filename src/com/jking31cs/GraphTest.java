package com.jking31cs;

import static org.junit.Assert.*;

import org.junit.Test;

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
		
		assertEquals((Integer) g.n.get(0), (Integer) 2);
		assertEquals((Integer) g.n.get(1), (Integer) 0);
		assertEquals((Integer) g.n.get(2), (Integer) 3);
		assertEquals((Integer) g.n.get(3), (Integer) 1);
		
		assertEquals((Integer) g.f.get(0), (Integer) 0);
		assertEquals((Integer) g.f.get(1), (Integer) 0);
		assertEquals((Integer) g.f.get(2), (Integer) 0);
		assertEquals((Integer) g.f.get(3), (Integer) 0);
		
		g.addEdge(new Edge(p3, p1));
		
		assertEquals(6, g.f.size());
		
		assertTrue(g.f.get(0) == g.f.get(2) && g.f.get(2) == g.f.get(4));
		assertTrue(g.f.get(1) == g.f.get(3) && g.f.get(3) == g.f.get(5));
		assertFalse(g.f.get(0) == g.f.get(1));
		
		assertEquals(2, g.h.size());

	}

}
