
public class Watertrap {
	static Main main;
	
	Vector a;
	Vector b;
	
	public Watertrap(Vector one, Vector two) {
		a=one;
		//a.add(main.shift);
		b=two;
		
	}
	
	public void render() {
		main.stroke(0);
		main.fill(0,0,255);
		main.rect((float)(a.x*main.scale+main.shift.x), (float)(a.y*main.scale+main.shift.y), (float)(b.x*main.scale), (float)(b.y*main.scale));
	}
	
	public boolean overlap(Vector pos) {
		return((pos.x<a.x+b.x && pos.x>a.x)&&(pos.y<a.y+b.y && pos.y>a.y));
	}
	
	

}
