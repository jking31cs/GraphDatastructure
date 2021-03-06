package com.jking31cs;

public class Corner {
	public final Edge e1;
	public final Edge e2;
	
	public Corner(Edge e1, Edge e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	
	public Point getCommonPoint() {
		if (e1.p1.equals(e2.p1)) {
			return new Point(e1.p1.x, e1.p1.y, e1.p1.z);
			
		}
		
		if (e1.p1.equals(e2.p2)) {
			return new Point(e1.p1.x, e1.p1.y, e1.p1.z);
		}
		
		if (e1.p2.equals(e2.p1)) {
			return new Point(e1.p2.x, e1.p2.y, e1.p2.z);
		}
		
		if (e1.p2.equals(e2.p2)) {
			return new Point(e1.p2.x, e1.p2.y, e1.p2.z);
		}
		
		throw new IllegalStateException("Edges in corner must share a point");
	}
	
	/*public Edge bisector() {
		
		if (e1.p1.equals(e2.p1)) {
			Point commonPoint = new Point(e1.p1.x, e1.p1.y);
			Vector v1 = e1.asVec();
			Vector v2 = e2.asVec();
			Vector bi = v1.add(v2);
			return new Edge(commonPoint, new Point(commonPoint.x + bi.x, commonPoint.y + bi.y));
		}
		
		if (e1.p1.equals(e2.p2)) {
			Point commonPoint = new Point(e1.p1.x, e1.p1.y);
			Vector v1 = e1.asVec();
			Vector v2 = new Vector(e2.p1.x - e2.p2.x, e2.p1.y - e2.p2.y);
			Vector bi = v1.add(v2);
			return new Edge(commonPoint, new Point(commonPoint.x + bi.x, commonPoint.y + bi.y));
		}
		
		if (e1.p2.equals(e2.p1)) {
			Point commonPoint = new Point(e1.p2.x, e1.p2.y);
			Vector v1 = e1.asVec().mul(-1f);
			Vector v2 = e2.asVec();
			Vector bi = v1.add(v2);
			return new Edge(commonPoint, new Point(commonPoint.x + bi.x, commonPoint.y + bi.y));
		}
		
		if (e1.p2.equals(e2.p2)) {
			Point commonPoint = new Point(e1.p2.x, e1.p2.y);
			Vector v1 = e1.asVec().mul(-1f);
			Vector v2 = e2.asVec().mul(-1f);
			Vector bi = v1.add(v2);
			return new Edge(commonPoint, new Point(commonPoint.x + bi.x, commonPoint.y + bi.y));
		}
		
		throw new IllegalStateException("Edges in corner must share a point");
		
	}
	
	/**
	 * Reworked bisector() to return the bisector as a Vector instead of an edge,
	 * also changed the bisector formula to ||v1||*v2 + ||v2||*v1 + more HAX please read e-mail
	 */
	
	public Vector bisector()
	{
		if (e1.p2.equals(e2.p1)){
			Vector v1 = new Vector(e1.p1.x - e1.p2.x, e1.p1.y - e1.p2.y, e1.p1.z - e1.p2.z).normalize();
			Vector v2 = e2.asVec();
			Vector bi = v1.normalize().add(v2.normalize());
			return bi.normalize();
		}
		throw new IllegalStateException("Edges in corner must share a point");
	}
	
	/**
	 * Uses the new bisector function and returns the position(at a distance of offset) of the corner as a vector.
	 */
	public Vector getPosition(float offset)
	{
		//Special case for dangling edge:
		Vector v1 = new Vector(e1.p1.x - e1.p2.x, e1.p1.y - e1.p2.y, e1.p1.z - e1.p2.z).normalize();
		Vector v2 = e2.asVec().normalize();
		Vector commonPointVector = new Vector(getCommonPoint().x, getCommonPoint().y, getCommonPoint().z);
		if (Math.abs(v1.determinant2(v2)) < .005) {
			if (getCommonPoint().equals(e1.p1))
				return commonPointVector.add(e2.asVec().normalize().mul(offset));
			else
				return commonPointVector.add(e1.asVec().normalize().mul(offset));
		}

		Vector commonPoint= commonPointVector;
		
		Vector bisectorVector=bisector().normalize().mul(offset).rotate(Math.PI, v1.crossProd(v2).normalize());
		
		return commonPoint.add(bisectorVector);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
		result = prime * result + ((e2 == null) ? 0 : e2.hashCode());
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
		Corner other = (Corner) obj;
		if (e1 == null) {
			if (other.e1 != null)
				return false;
		} else if (!e1.equals(other.e1))
			return false;
		if (e2 == null) {
			if (other.e2 != null)
				return false;
		} else if (!e2.equals(other.e2))
			return false;
		return true;
	}
	
	
	
}
