package com.jking31cs;

public class Corner {
	public Integer v;  //Half Edge associated with the corner.
	public Integer n;  //Next Corner
	public Integer s;  //Swing Corner (another corner on same vertex).

    public Corner() {
        this(null,null,null);
    }

    /**
     * Meant to be used in testing only.
     */
    public Corner(Integer v, Integer n, Integer s) {
        this.v = v;
        this.n = n;
        this.s = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Corner corner = (Corner) o;

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
