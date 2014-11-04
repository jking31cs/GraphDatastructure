package com.jking31cs;

public class CornerIndexInfo {
	public Integer v;  //Half Edge index associated with the corner.
	public Integer n;  //Next Corner index
	public Integer s;  //Swing Corner (another corner on same vertex) index.
	public Integer p;  //Previous Corner
	public Boolean isVisited;

    public CornerIndexInfo() {
        this(null,null,null);
        isVisited=false;
    }

    /**
     * Meant to be used in testing only.
     */
    public CornerIndexInfo(Integer v, Integer n, Integer s) {
        this.v = v;
        this.n = n;
        this.s = s;
        isVisited=false;
    }
    
    public void visit(){
    	isVisited=true;
    }

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CornerIndexInfo other = (CornerIndexInfo) obj;
		if (isVisited == null) {
			if (other.isVisited != null)
				return false;
		} else if (!isVisited.equals(other.isVisited))
			return false;
		if (n == null) {
			if (other.n != null)
				return false;
		} else if (!n.equals(other.n))
			return false;
		if (p == null) {
			if (other.p != null)
				return false;
		} else if (!p.equals(other.p))
			return false;
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.equals(other.s))
			return false;
		if (v == null) {
			if (other.v != null)
				return false;
		} else if (!v.equals(other.v))
			return false;
		return true;
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((isVisited == null) ? 0 : isVisited.hashCode());
		result = prime * result + ((n == null) ? 0 : n.hashCode());
		result = prime * result + ((p == null) ? 0 : p.hashCode());
		result = prime * result + ((s == null) ? 0 : s.hashCode());
		result = prime * result + ((v == null) ? 0 : v.hashCode());
		return result;
	}

    @Override
	public String toString() {
		return "CornerIndexInfo [v=" + v + ", n=" + n + ", s=" + s + ", p=" + p
				+ ", isVisited=" + isVisited + "]";
	}
}
