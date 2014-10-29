package com.jking31cs;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by bobby on 10/28/14.
 */
public class Model3D {

	public final Graph mainGraph;
	public final Graph z_offset_graph;

	public Model3D() {
		this.mainGraph = new Graph();
		this.z_offset_graph = new Graph();
	}


	public void addPoint(Point p) {
		mainGraph.addVertex(p);
		Point offset = new Point(p.x, p.y, 50);
		z_offset_graph.addVertex(offset);
		System.out.println("Added Points: " + p + ", " + offset);
	}

	public void addEdge(Edge e) {
		mainGraph.addEdge(e);

		Point p1 = z_offset_graph.points.get(mainGraph.points.indexOf(e.p1));
		Point p2 = z_offset_graph.points.get(mainGraph.points.indexOf(e.p2));

		z_offset_graph.addEdge(new Edge(p1,p2));
	}

	public Set<Edge> verticalEdges() {
		Set<Edge> edges = new HashSet<>();

		for (int i = 0; i < mainGraph.points.size(); i++) {
			edges.add(new Edge(mainGraph.points.get(i), z_offset_graph.points.get(i)));
		}

		return edges;
	}

}
