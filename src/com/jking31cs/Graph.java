package com.jking31cs;

import java.util.*;

public class Graph {
	
	public List<Point> points; //vertices
	
	List<Integer> v;      //half edge starting point indices
	List<Integer> n;      //half edge that follows the half edge at that index.
	List<Integer> o;      //opposite half edge index of given half edge index;
	List<Integer> p;      //half edge that precedes the half edge at that index.
	
	List<Integer> h;      //half edge on the face given
	List<Integer> f;      //face for given half edge index.
	
	List<CornerIndexInfo> corners; //corners on the graph.
	
	public Graph() {
		points = new ArrayList<>();
		v = new ArrayList<>();
		n = new ArrayList<>();
		f = new ArrayList<>();
		h = new ArrayList<>();
		o = new ArrayList<>();
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
			boolean startingPointFound = false;
			for (int j = 0; j < v.size(); j++) {
				if (j == nextIndex) continue;
				if (v.get(j) == v.get(nextIndex)) {
					
					//Check if already found given next index.
					if (v.get(j) == v.get(target)) {
						if (copy[j] == null || copy[o.get(j)] == null) {
							startingPointFound = true;
							continue;							
						}
					}
					
					nextArr[curIndex] = j;
					curIndex = j;
					copy[curIndex] = null;
					found = true;
					break;
				}
			}
			if (startingPointFound && !found) {
				nextArr[curIndex] = target;
				curIndex = target;
				copy[curIndex] = null;
			} else if (!found) {
				nextArr[curIndex] = nextIndex;
				curIndex = nextIndex;
				copy[curIndex] = null;
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
		 * We can find previous indices with the next array.
		 */
		p = new ArrayList<>(n.size());
		for (int searchIndex = 0; searchIndex < n.size(); searchIndex++) {
			for (int i = 0; i < n.size(); i++) {
				if (n.get(i) == searchIndex) {
					p.add(searchIndex, i);
					break;
				}				
			}
		}
		
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
		
		/*
		 * Find half edge on given face.
		 */
		Set<Integer> faceIndices = new HashSet<>(f);
		h = new ArrayList<>();
		for (int i = 0; i < f.size(); i++) {
			if (faceIndices.contains(f.get(i))) {
				h.add(f.get(i), i);
				faceIndices.remove(f.get(i));
			}
		}

		corners = new ArrayList<>();
		/*
		 * Find the corners
		 *
		 * 1.  For every edge there's a corner there.
		 */
		for (int j = 0; j < v.size(); j++) {
			CornerIndexInfo c = new CornerIndexInfo();
			c.v = j;
			corners.add(c);
		}
		/*
		 * 2.  Calculate next corners.
		 */
		for (CornerIndexInfo c1 : corners) {
			for (int i = 0; i < corners.size(); i++) {
				CornerIndexInfo c2 = corners.get(i);
				if (c1.v.equals(c2.v)) continue;
				if (n.get(c1.v).equals(c2.v)) {
					c1.n = i;
				}
			}
		}
		/*
		 * 3.  Calculate swing corners.  We do thing by finding all corners on same point, then
		 * just setting it in motion.  IE corners 0,1,2 are all point p.  The swing in order will
		 * be 1,2,0.
		 */
		Map<Integer, Set<Integer>> cornerPointMap = new HashMap<>();
		for (int i = 0; i < corners.size(); i++) {
			CornerIndexInfo c = corners.get(i);
			Set<Integer> set = cornerPointMap.get(v.get(c.v));
			if (set == null) {
				set = new HashSet<>();
			}
			set.add(c.v);
			cornerPointMap.put(v.get(c.v), set);
		}
		for (Set<Integer> cIndicies : cornerPointMap.values()) {
			Integer[] cIndexArr = cIndicies.toArray(new Integer[cIndicies.size()]);
			if (cIndexArr.length == 1) continue; //TODO dandling edge corners have no swing...what do we do?
			int startSwingIndex = 1;
			CornerIndexInfo c = corners.get(cIndexArr[0]);
			while (c.s == null) {
				c.s = cIndexArr[startSwingIndex % cIndexArr.length];
				c = corners.get(cIndexArr[startSwingIndex % cIndexArr.length]);
				startSwingIndex++;
			}
		}
	}

	/**
	 * Gets all edges by skipping over every other one, since we do opposite edges
	 * right next to the edge.
	 * 
	 * @return a list of edges half the size of v.
	 */
	public Set<Edge> getEdges() {
		Set<Edge> toRet = new HashSet<>();
		for (int i = 0; i < v.size(); i+=2) {
			Point p1 = points.get(v.get(i));
			Point p2 = points.get(v.get(n.get(i)));
			toRet.add(new Edge(p1,p2));
		}
		return toRet;
	}
	
	public List<Corner> getCorners() {
		List<Corner> convertedCorners = new ArrayList<>();
		for (int i = 0; i < corners.size(); i++) {
			CornerIndexInfo c = corners.get(i);
			Edge e1 = new Edge(points.get(v.get(c.v)), points.get(v.get(n.get(c.v))));
			Edge e2 = new Edge(points.get(v.get(c.v)), points.get(v.get(p.get(c.v))));
			convertedCorners.add(new Corner(e1, e2));
		}
		return convertedCorners;
	}
}
