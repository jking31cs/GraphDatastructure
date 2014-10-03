package com.jking31cs;

import static processing.core.PApplet.*;

/**
 * Unlike a Point, a vector represents more of a direction than a point in space.  This
 * is specifically for 2 dimensions.
 * @author jking31
 */
public class Vector {
	public final float x;
	public final float y;
  
	public Vector(float a,float b){
		x=a;
		y=b;
	}	
  
	/**
	 * Gets the magnitude of the Vector.
	 * @return
	 */
	public float getMag(){
	    return sqrt( x*x + y*y );
	}
  
	/**
	 * Gets the angle of the vector.
	 * @return
	 */
	public float getAngle(){
		return atan2(y,x);
	}
	
	/**
	 * Normalizes the vector.
	 * @return
	 */
	public Vector normalize(){
		return this.mul(1f/getMag());
	}
	
	/**
	 * Scalar multiplication of vector.
	 * @param c
	 * @return
	 */
	public Vector mul(float c){
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
	public Vector rotate(float angle) {
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
	public float dotProduct(Vector v) {
		return v.x*x + v.y*y;
	}
	
	/**
	 * Vector or cross product, returns the determinate of 
	 * 		| x, v.x |
	 * 		| y, v.y |
	 * @param v
	 * @return
	 */
	public float vectorProduct(Vector v) {
		return x*v.y - y*v.x;
	}
	
	public float angleBetween(Vector v) {
		float angle = acos(normalize().dotProduct(v.normalize()));
	       
       // Check the current rotation of vectors
       if(this.vectorProduct(v) < 0) {
    	   angle *= -1;   
       }                 
       return angle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
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
		Vector other = (Vector) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}
	
	
} 
