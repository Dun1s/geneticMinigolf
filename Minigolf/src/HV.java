
public class HV {
	static Main main;
	
	Vector peak;
	
	double scale=main.scale;
	double radius;
	
	boolean hill;
	
	public HV(Vector one, boolean hill, double rad) {
		peak=one;
		//peak.add(main.shift);
		this.hill=hill;
		radius=rad;
	}
	
	
	public void render() {
		for(int layer=10; layer>0; layer--) {
			main.noStroke();
			if(hill)
				main.fill(160,160,160,8*layer);
			else
				main.fill(20,20,20,4*layer);
			main.ellipse((float)(scale*peak.x+main.shift.x),  (float)(scale*peak.y+main.shift.y), (float)(radius*scale*(layer/5.0)), (float)(radius*scale*(layer/5.0)));
		}
		main.stroke(1);
	}
	
	public void hDPhysics(Ball b) {
		Vector dist = b.pos.distance(peak);
		if(dist.mag()<radius) {
			b.timeOnHV+=b.dt;
			if(hill) {
				b.stuckTimeH+=b.dt;
				if(dist.mag()>radius/2) {
					b.force.add((dist.normalize().mult(0.05/dist.mag())));
				}
				else if(dist.mag()<radius/2) {
					b.force.add((dist.normalize().mult(0.2*dist.mag())));
				}
					
			}
				
			else if(!hill) {
				b.stuckTimeV+=b.dt;
				if(dist.mag()<radius/2) {
					//main.println(b.stuckTime);
					if(dist.mag()>0.05)
						b.force.add((dist.normalize().mult(-0.05/dist.mag())));
				}
				else if(dist.mag()>radius/2) {
					b.force.add((dist.normalize().mult(-0.2*dist.mag())));
				}
			}
		}
		else if(dist.mag()>radius && !hill){
			b.stuckTimeV=0;
		}
		else if(dist.mag()>radius && hill){
			b.stuckTimeH=0;
		}
			
	}
	
	//hills
		/* Aepex defined point
		 * Push away from peak
		 * Distance and normalize? 
		 * Critizal point (max push) half of radius
		 * 
		 * F=0.05/r (outer (while distance is >.5r))  
		 * F=.2r (inner (while distance is <0.5r))
		 * 
		 */
		
		//valley
		/* Low defined point
		 * Pull towards
		 * F=0.05/r (inner)
		 * F=0.2r (outer) 
		 * */
}


