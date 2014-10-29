package com.jking31cs;

public class Edge {
	public final Point p1;
	public final Point p2;
	
	public Edge(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public double length() {
		return asVec().getMag();
	}
	
	public Vector asVec() {
		return new Vector(p2.x-p1.x, p2.y-p1.y, p2.z - p1.z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
		result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (p1 == null) {
			if (other.p1 != null)
				return false;
		} else if (!p1.equals(other.p1))
			return false;
		if (p2 == null) {
			if (other.p2 != null)
				return false;
		} else if (!p2.equals(other.p2))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Edge [p1=" + p1 + ", p2=" + p2 + "]";
	}
	
	
}
