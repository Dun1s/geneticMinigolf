
public class Vector {
	//fields
	double x;
	double y;
	double z;
	
	//constructors
	public Vector() {
		x=0;
		y=0;
		z=0;
	}
	public Vector(double x, double y) {
		this.x=x;
		this.y=y;
		z=0;
	}
	public Vector(double x, double y, double z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public Vector(double all) {
		this.x=all;
		this.y=all;
		this.z=all;
	}
	
	//adds
	public void add(double x, double y, double z) {	//modifies vec
		this.x+=x;
		this.y+=y;
		this.z+=z;
	}
	public void add(Vector v2) {	//modifes vec
		add(v2.x, v2.y, v2.z);
	}
	public static Vector add(Vector v1, Vector v2) {
		double x = v1.x+v2.x;
		double y = v1.y+v2.y;
		double z = v1.z+v2.z;
		return new Vector(x,y,z);
	}
	//subtract
	public void subtract(double x, double y, double z) { //modifies vec
		this.x-=x;
		this.y-=y;
		this.z-=z;
	}
	public void subtract(Vector v2) {	//modifes vec
		subtract(v2.x, v2.y, v2.z);
	}
	public static Vector subtract(Vector v1, Vector v2) {
		double x = v1.x-v2.x;
		double y = v1.y-v2.y;
		double z = v1.z-v2.z;
		return new Vector(x,y,z);
	}
	
	
	//multiplication
	public Vector mult(double scalar) {	  //modifies vec
		this.x*=scalar;
		this.y*=scalar;
		this.z*=scalar;
		
		return this;		//incase you want to set equal to a new calculated vec
	}
	public static Vector mult(Vector vec, double scalar) {	//doesnt modify vec
		double x = vec.x*scalar;
		double y = vec.y*scalar;
		double z = vec.z*scalar;
		
		return new Vector(x,y,z);
	}
	public static void mult(Vector vec, double scalar, Vector target) {	//doesnt modify vec
		double x = vec.x*scalar;
		double y = vec.y*scalar;
		double z = vec.z*scalar;
		
		target.set(new Vector(x,y,z));
	}
	
	//division
	public Vector div(double scalar) {	//modifies vec
		this.x/=scalar;
		this.y/=scalar;
		this.z/=scalar;
		
		return this;
	}
	public static Vector div(Vector vec, double scalar) {	//doesnt modify vec
		double x = vec.x/scalar;
		double y = vec.y/scalar;
		double z = vec.z/scalar;
		
		return new Vector(x,y,z);
	}
	public static void div(Vector vec, double scalar, Vector target) {	//doesnt modify vec
		double x = vec.x/scalar;
		double y = vec.y/scalar;
		double z = vec.z/scalar;
		
		target.set(new Vector(x,y,z));
	}
	
	//setter
	public void set(double x, double y, double z) {		//modifies vec (duh)
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public void set(Vector v2) {	//modifies
		set(v2.x, v2.y, v2.z);
	}
	
	//magnitude
	public double mag() {	//doesnt modify
		return Math.sqrt((x*x)+(y*y)+(z*z));
	}
	public static double mag(Vector tag) {
		double x=tag.x;
		double y=tag.y;
		double z=tag.z;
		
		return Math.sqrt((x*x)+(y*y)+(z*z));
	}
	
	public double magSq() {		//doesnt modify
		return mag()*mag();
	}
	
	//normalize
	public Vector normalize() {		//doesnt modify
		double mag = mag();
		Vector target = new Vector();
		target.set(x/mag, y/mag, z/mag);
		return target;	
	}
	public static void normalize(Vector vec) {	//modifies
		double mag = vec.mag();
		vec.x/=mag;
		vec.y/=mag;
		vec.z/=mag;
	}
	
	//set mag
	public void setMag(double mag) {	//modifies
		Vector set = normalize();
		set.mult(mag);
		this.set(set);
	}
	
	//dot (come back to when relevant)
	public double dot(Vector v2) {		//doesnt modify
		return(this.x*v2.x + this.y*v2.y + this.z*v2.z);
	}
	
	//cross
	public Vector cross(Vector v2) {	//doesnt modify
		double crossX = this.y * v2.z - v2.y * this.z;
		double crossY = this.z * v2.x - v2.z * this.x;
		double crossZ = this.x * v2.y - v2.x * this.y;
	
		Vector nV = new Vector(crossX, crossY, crossZ);
		return nV;
	}
	
	//rotate (in degrees)
	public static Vector rotate(Vector v, double angle) {	//doesn't modify the vector, but returns the rotated one
		if(v.z!=0)
			return v;
		double theta = angle*(Math.PI/180);
		double ex =v.x*Math.cos(theta) - v.y*Math.sin(theta);
		double why = v.x*Math.sin(theta) + v.y*Math.cos(theta);
		return new Vector(ex, why, 0);
		
	}
	
	//angle between (degrees)
//	public double angleBetween(Vector v1, Vector v2) {
//		
//		
//		return 
//	}
	
	//distance
	//public 
	
	//toString
	public String toString() {
		return "[" + this.x +", " + this.y + ", " + this.z + "]";
	}
	
	public Vector distance(Vector v) {
		return new Vector(this.x-v.x, this.y-v.y, this.z-v.z);
	}
	
}	
