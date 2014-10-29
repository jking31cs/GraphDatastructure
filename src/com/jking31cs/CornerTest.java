package com.jking31cs;

import static org.junit.Assert.*;

import org.junit.Test;

public class CornerTest {

	@Test
	public void test() {
		Edge e1 = new Edge(new Point(50,100), new Point(50,50));
		Edge e2 = new Edge(new Point (50,50), new Point (100, 50));
		
		Corner c = new Corner(e1,e2);
		
		Vector position = c.getPosition((float) Math.sqrt(2)*5);
		assertEquals(45, position.x, .005);
		assertEquals(45, position.y, .005);
	}

}
