
public class Hole {
	static Main main;
	
	Vector a;
	
	public Hole(Vector hole) {
		a=hole;
		//a.add(main.shift);
	}
	
	
	public void render() {
		
		main.noStroke();
		main.fill(100,100,100,100);
		main.ellipse((float)(main.scale*a.x+main.shift.x),  (float)(main.scale*a.y+main.shift.y), (float)(1*main.scale), (float)(1*main.scale));
		main.stroke(1);
		main.fill(255);
		main.ellipse((float)(main.scale*a.x+main.shift.x),  (float)(main.scale*a.y+main.shift.y), (float)(0.10795*main.scale), (float)(0.10795*main.scale));

	}
	
	public boolean effectiveRadius(Ball ball) {
		return(a.distance(ball.pos).mag()<(-1.0/32.0 * ball.vel.mag() + 0.05));
	}

}
