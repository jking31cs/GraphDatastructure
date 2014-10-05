package com.jking31cs;

import static java.lang.Math.*;

/**
 * Unlike a Point, a vector represents more of a direction than a point in space.  This
 * is specifically for 2 dimensions.
 * @author jking31
 */
public class Vector {
	public final double x;
	public final double y;
  
	public Vector(double a,double b){
		x=a;
		y=b;
	}	
  
	/**
	 * Gets the magnitude of the Vector.
	 * @return
	 */
	public double getMag(){
	    return sqrt( x*x + y*y );
	}
  
	/**
	 * Gets the angle of the vector.
	 * @return
	 */
	public double getAngle(){
		return atan2(y,x);
	}
	
	/**
	 * Normalizes the vector.
	 * @return
	 */
	public Vector normalize(){
		return this.mul(1/getMag());
	}
	
	/**
	 * Scalar multiplication of vector.
	 * @param c
	 * @return
	 */
	public Vector mul(double c){
		return new Vector(x*c,y*c);
 	}
	
	/**
	 * Adds the vector to this vector.
	 * @param v
	 * @return
	 */
	public Vector add(Vector v) {
		return new Vector(v.x+x, v.y+y);
	}
	
	/**
	 * Subtracts the vector from this vector.
	 * @param v
	 * @return
	 */
	public Vector sub(Vector v) {
		return new Vector(x-v.x, y-v.y);
	}
	
	/**
	 * Rotates the vector by the given angle which is in radians.
	 * @param angle
	 * @return
	 */
	public Vector rotate(double angle) {
		return new Vector(
			cos(angle)*x - sin(angle)*y,
			sin(angle)*x + cos(angle)*y
		);
	}
	
	/**
	 * Returns the dot product.
	 * @param v
	 * @return
	 */
	public double dotProduct(Vector v) {
		return v.x*x + v.y*y;
	}
	
	/**
	 * Vector or cross product, returns the determinate of 
	 * 		| x, v.x |
	 * 		| y, v.y |
	 * @param v
	 * @return
	 */
	public double vectorProduct(Vector v) {
		return x*v.y - y*v.x;
	}
	
	public double angleBetween(Vector v) {
		double dotProduct = normalize().dotProduct(v.normalize());

		if (abs(dotProduct + 1) < .005) {
			dotProduct = -1d;
		} else if (abs(dotProduct - 1) < .005) {
			dotProduct = 1;
		}


		double angle = Math.acos(dotProduct);
	       
       // Check the current rotation of vectors
       if(this.vectorProduct(v) < 0) {
    	   angle *= -1;   
       }                 
       return angle;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Vector vector = (Vector) o;

		if (Double.compare(vector.x, x) != 0) return false;
		if (Double.compare(vector.y, y) != 0) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}
