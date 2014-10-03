GraphDatastructure
==================

Purpose:  To design a Graph datastructure that can work in either 2D or 3D that calculates all loops, faces, and corners dynamically while editing the graph.

Requires: Apache Ant

Build Instructions:
1.  check out this repo or download zip
2.  in new directory, run ant MyApplet to launch applet.

How it works:

The Graph Object holds a ton of information.  We represent one edge as 2 directed half edges, and for every edge that gets added to the graph, we re-calculate the loops, face counts, and corners in case something has changed. We tested this using a JUnit test and a hand drawn example with expected output.  The test is a triangle and a triangle with a dangling edge.

