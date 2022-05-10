
public class Wall {
	static Main main;

	Vector a;
	Vector b;
	
	double buffer = 0.1*Ball.dt;
	
	boolean hole;
	boolean tee;
	
	double scale=main.scale;
	
	boolean straight;
	
	public Wall(Vector one, Vector two) {
		a=one;
		//a.add(main.shift);
		b=two;
		//b.add(main.shift);
		straight=(a.x==b.x || a.y==b.y);
	}
	
	
	public void render() {
		main.stroke(0);
		main.line((float)(scale*a.x+main.shift.x), (float)(scale*a.y+main.shift.y), (float)(scale*b.x+main.shift.x), (float)(scale*b.y+main.shift.y));
	}
	
	public double length() {
		return a.distance(b).mag();
	}
	
	public Vector vWall() {
		return Vector.subtract(a,b);
	}
	
	public boolean isCollision(Ball ball) {
		return (a.distance(ball.pos).mag() + b.distance(ball.pos).mag() < Vector.subtract(a,b).mag() + buffer);

		//return (Math.abs(length()-(ball.pos.distance(a).mag()+ball.pos.distance(b).mag())) < buffer);
	}

}
