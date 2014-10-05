package com.jking31cs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {
	
	public List<Point> points; //vertices
	
	List<Integer> v;      //half edge starting point indices
	List<Integer> n;      //half edge that follows the half edge at that index.
	List<Integer> o;      //opposite half edge index of given half edge index;
	List<Integer> p;      //half edge that precedes the half edge at that index.
	
	List<Integer> h;      //half edge on the face given
	List<Integer> f;      //face for given half edge index.
	
	public List<CornerIndexInfo> corners; //corners on the graph.
	
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
		 * ALGORITHM:
		 * 	    1.  Find all valid next indices
		 *      2.  Find which valid next halfedge is the smallest angle (keeping on the sidewalk)
		 *      3.  Set that new halfedge as the next halfedge, set up next index.
		 *      4.  Repeat
		 */

		Integer[] copy = v.toArray(new Integer[v.size()]);
		Integer[] nextArr = new Integer[v.size()];
		Integer target = 0;
		Integer startIndex = target;
		while (notAllNull(copy)) {
			Integer nextPoint = v.get(o.get(startIndex));
			Set<Integer> validNextIndices = new HashSet<>();
			for (int i = 0; i < v.size(); i++) {
				if (v.get(i).equals(nextPoint)) {
					validNextIndices.add(i);
				}
			}
			double minAngle = Double.MAX_VALUE;
			Integer nextIndex = null;
			for (Integer j : validNextIndices) {
				Vector v1 = new Edge(points.get(v.get(startIndex)), points.get(v.get(j))).asVec().normalize();
				Vector v2 = new Edge(points.get(v.get(j)), points.get(v.get(o.get(j)))).asVec().normalize();
				double angleBetween = v1.angleBetween(v2);
				if (angleBetween < minAngle) {
					minAngle = angleBetween;
					nextIndex = j;
				}
			}
			nextArr[startIndex] = nextIndex;
			startIndex = nextIndex;
			copy[startIndex] = null;

			if (nextIndex.equals(target)) {
				for (int i = 0; i < copy.length; i++) {
					if (copy[i] == null) continue;
					startIndex = i;
					target = startIndex;
					break;
				}
			}
		}

		n = Arrays.asList(nextArr);

		/*
		 * We can find previous indices with the next array.
		 */
		p = new ArrayList<>(n.size());
		for (int searchIndex = 0; searchIndex < n.size(); searchIndex++) {
			for (int i = 0; i < n.size(); i++) {
				if (n.get(i) != null && n.get(i) == searchIndex) {
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
		Integer nextIndex;
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

	private boolean notAllNull(Integer[] copy) {
		for (Integer i : copy) {
			if (i != null) return true;
		}
		return false;
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
	
	/**
	 * Gets all corners as a pair of edges that share a point.
	 * @return
	 */
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
	
	/**
	 * Returns a set of size f that holds a valid path for each face.
	 * @return
	 */
	public Set<List<Edge>> getFacePaths() {
		Set<List<Edge>> toRet = new HashSet<>();
		for (Integer startIndex : h) {
			Integer curIndex = startIndex;
			Integer nextIndex = n.get(startIndex);
			List<Edge> list = new ArrayList<>();
			do {
				Point start = points.get(v.get(curIndex));
				Point end = points.get(v.get(nextIndex));
				list.add(new Edge(start, end));
				curIndex = nextIndex;
				nextIndex = n.get(curIndex);
			} while (curIndex != startIndex);
			toRet.add(list);
		}
		return toRet;
	}
}
