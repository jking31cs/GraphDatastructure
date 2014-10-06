package com.jking31cs;

public class CornerIndexInfo {
	public Integer v;  //Half Edge index associated with the corner.
	public Integer n;  //Next Corner index
	public Integer s;  //Swing Corner (another corner on same vertex) index.
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CornerIndexInfo corner = (CornerIndexInfo) o;

        if (n != null ? !n.equals(corner.n) : corner.n != null) return false;
        if (s != null ? !s.equals(corner.s) : corner.s != null) return false;
        if (v != null ? !v.equals(corner.v) : corner.v != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = v != null ? v.hashCode() : 0;
        result = 31 * result + (n != null ? n.hashCode() : 0);
        result = 31 * result + (s != null ? s.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Corner{" +
                "v=" + v +
                ", n=" + n +
                ", s=" + s +
                '}';
    }
}
