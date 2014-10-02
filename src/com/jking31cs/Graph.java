package com.jking31cs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph {
	
	List<Point> points; //vertices
	
	List<Integer> v;    //half edge starting point indices
	List<Integer> n;    //half edge ending point indices
	List<Integer> o;    //opposite half edge index of given half edge index;

	List<Integer> h;    //half edge on the face given
	List<Integer> f;    //face for given half edge index.
	
	public Graph() {
		points = new ArrayList<>();
		v = new ArrayList<>();
		n = new ArrayList<>();
		f = new ArrayList<>();
		h = new ArrayList<>();
		o = new ArrayList<>();
	}
	
	public HalfEdge getHalfEdge(Integer index) {
		return new HalfEdge(points.get(v.get(index)), points.get(n.get(index)));
	}
	
	public HalfEdge getOppositeHalfEdge(Integer index) {
		return new HalfEdge(points.get(v.get(o.get(index))), points.get(n.get(o.get(index))));
	}
	
	public void addVertex(Point p) {
		points.add(p);
	}
	
	public void addEdge(Edge e) {
		
		if (!points.contains(e.p1) || !points.contains(e.p2)) {
			throw new IllegalArgumentException("Both points must be added to the graph");
		}
		
		/*
		 * 1.  Add half edges for given points.
		 */
		int index1 = points.indexOf(e.p1);
		int index2 = points.indexOf(e.p2);
		v.add(index1);
		int indexOfHE1 = v.size() - 1;
		v.add(index2);
		int indexOfHE2 = v.size() - 1;
		
		/*
		 * 2.  Update opposites
		 */
		o.add(indexOfHE1, indexOfHE2);
		o.add(indexOfHE2, indexOfHE1);
		
		/*
		 * 3.  Loop through to find next indicies.
		 * 
		 * ALGORITHM:  Find opposite index to point we're on.  
		 * 			   Look through all starting points that share an index with opposite.
		 *             If none exist, go with opposite, otherwise first one we encounter.
		 */
		Integer curIndex = 0;
		Integer target = 0;
		Integer nextIndex = null;
		Integer[] nextArr = new Integer[v.size()];
		Integer[] copy = v.toArray(new Integer[v.size()]);
		copy[curIndex] = null;
		while (true) {
			nextIndex = o.get(curIndex);
			boolean found = false;
			for (int j = 0; j < v.size(); j++) {
				if (j == nextIndex) continue;
				if (v.get(j) == v.get(nextIndex)) {
					nextArr[curIndex] = j;
					curIndex = j;
					copy[curIndex] = null;
					nextIndex = o.get(j);
					found = true;
					break;
				}
			}
			if (!found) {
				nextArr[curIndex] = nextIndex;
				curIndex = nextIndex;
				copy[curIndex] = null;
				nextIndex = o.get(nextIndex);
			}
			if (curIndex == target) {
				Integer nextPoint = null;
				while (nextPoint == null) {
					nextPoint = copy[curIndex];
					if (nextPoint == null) curIndex++;						
					if (curIndex >= v.size()) break;
				}
				if (nextPoint == null) break;
				target = curIndex;
			}
		}
		n = Arrays.asList(nextArr);
		
		/*
		 * Finding faces with following algorithm.
		 * 
		 * 1.  Go through half edges and find a loop,
		 * 2.  Remove those edges from list of half edges to look at
		 * 3.  Repeat until all half edges are gone.
		 */
		
		copy = v.toArray(new Integer[v.size()]);
		boolean done = false;
		int faceCount = 0;
		Integer[] faceArr = new Integer[v.size()];
		while (!done) {
			int index = 0;
			Integer startPoint = null;
			while (startPoint == null) {
				startPoint = copy[index];
				if (startPoint == null) index++;
				if (index >= copy.length) {
					done = true;
					break;
				}
			}
			if (startPoint == null) {
				break;
			}
			faceArr[index] = faceCount;
			copy[index] = null;
			nextIndex = n.get(index);
			while (nextIndex != index) {
				faceArr[nextIndex] = faceCount;
				copy[nextIndex] = null;
				nextIndex = n.get(nextIndex);
			}
			faceCount++;
		}
		f = Arrays.asList(faceArr);
	}
}
