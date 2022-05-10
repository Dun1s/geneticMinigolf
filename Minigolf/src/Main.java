import processing.core.PApplet;
import java.util.ArrayList;



public class Main extends PApplet{
	//lists
	ArrayList<Ball> shots = new ArrayList<>();
	ArrayList<Ball> sorts = new ArrayList<>();
	Ball previousMax;
	
	ArrayList<Wall> walls = new ArrayList<>();
	ArrayList<HV> hillValleys = new ArrayList<>();
	ArrayList<Watertrap> wTs = new ArrayList<>();
	ArrayList<Hole> holes = new ArrayList<>();
	ArrayList<Wall> extraWalls = new ArrayList<>();
	

	//run counter
	int runs;
	int generations=1;
	//render stuff
	double scale=150;
	
	//Vectors
	
	//wall setup
	double wide=3;
	double high=6;
	
	double population=100;
	int sunk;
	double totalVel;
	double finished;
	
	boolean sorted;
	boolean initiated;
	boolean bunker=false;
	boolean gWall=false;
	
	
	Vector tee = new Vector(wide/2, high*11/12, 0);
	Vector shift = new Vector((((width/2)/scale)),(((height/2)/scale)),0).mult(scale);

	public void settings() {
		//frameRate(120);
		size(600,1000);
	}
		
	public void setup() {
//		Vector test = new Vector(1,1,0);
//		println(test);
//		double degrees=-90;
//		println(Vector.rotate(test, degrees) + " " + degrees + " degrees");
		
		Wall.main=this;
		Ball.main=this;
		HV.main=this;
		Watertrap.main=this;
		Hole.main=this;
		
		hillValley();
		//moat();
		//greatWall();
		//bunkerDown();

		
		initiate();
	
	}
	
	
	

	
	public void draw() {
		finished=0;
		
		
		background(124,252,0);
		fill(0);
		
		fill(210,209,205);
		for(Wall w : walls) {
			w.render();
			for(Ball b : shots) {
				if(w.isCollision(b)) {
					b.collide(w.a, w.b); 
				}
			}
		}
		
		for(HV h : hillValleys) {
			h.render();
		}
		
		for(Hole h: holes) {
			h.render();
		}
		
		for(Watertrap w: wTs) {
			w.render();
		}
		
		
		for(Ball b : shots) {
			if(b.finished || b.sunk) {
				finished++;
			}
			//println(b.pos);
			if(!b.sunk) {
				b.render();
			}
			if(!b.finished) {
				b.math();
				for(Hole h: holes) {
					if(h.effectiveRadius(b)) {
						b.sunk();
						sunk++;
						if(bunker) {
							if(b.bounces>2){
								b.sunk();
							}
							else{
								b.stuck=true;
								sunk--;
							}
						}
						if(gWall) {
							if(b.bounces>1){
								b.sunk();
							}
							else{
								b.stuck=true;
								sunk--;
							}
						}
					}
				}
				for(HV h : hillValleys) {
					h.hDPhysics(b);
				}
				for(Watertrap w: wTs) {
					if(w.overlap(b.pos))
						b.stuck=true;
				}
			}
			
			
		}
		
		if(finished==population && initiated) {
			sort();
		}
		if(sorted) {
			regen();
		}
		
		fill(255,0,0);
		ellipse((float)mouseX, (float)mouseY, 0.5f, 0.5f);
		
	}
	
	
	
	public void mousePressed() {
		
	}
	
	public void keyPressed() {
		if(key==' ') {
			sort();
//			for(int i=0; i<sorts.size()/2; i++) {
//				println(sorts.get(i) + "  " + i);
//			}noLoop();
		}
		if(key=='f') {
			println(finished);
		}
		if(key=='s') {
			println("Population: " + population + "  Shots size: " + shots.size());
		}
	}
	
	public void initiate() {	
		shots.add(new Ball(new Vector(1.1882298786315852, -3.0260829936043647, 0)));
		for(int i = 0; i<population; i++) {
			//shots.add(new Ball(0,4));
			shots.add(new Ball(random(0,360), random(0,8)));
		}
		initiated=true;
	}
	
	public void sort() {
		//println("Sunk: " + sunk + "  Finished: " + finished + "  Population: " + population);
		//println((100.0*sunk)/finished + "%");
		for(int i =0; i<shots.size(); i++){
			sorts.add(shots.get(i));
		}
		for(int j = 0; j<sorts.size(); j++) {
			for(int k=j+1; k<sorts.size(); k++) {
				if(sorts.get(k).score()>sorts.get(j).score()) {
					Ball hold = sorts.get(j);
					sorts.set(j,  sorts.get(k));
					sorts.set(k, hold);
				}
			}
		}
		sorted=true;
	}
	
	public void reset() {
		
	}
	
	public void regen() {
		//double worst = sorts.get(sorts.size()-1).score();
		println(sorts.get(0).lVel);
		shots.clear();
		double standardD = 1.0;
		int ndx=0;
		int kids;
		int size=0;
		//shots.add(new Ball(new Vector(1.1882298786315852, -3.0260829936043647, 0)));
		while(size<population && shots.size()<population) {
			kids=1;
			Ball parent = sorts.get(ndx);
			if(parent.pScore>10 && parent.sunk) {
				kids=15;
				standardD=Math.abs(parent.pScore-parent.score());
				//println("Double sink: " + standardD);
			}
			else if(parent.sunk) {
				kids=10;
				standardD=0;
			}
			else if(parent.holeDistance<0.5) {
				kids=4;
				standardD=parent.holeDistance;
				//println("Kids: " + kids + " SD: " + standardD + "\nBall: \n" + parent);
			}
			else if(parent.closest.x<0.25) {
				kids=2;
				standardD=parent.closest.x*parent.closest.y;
			}
			else {
				kids=1;
				standardD=parent.practicalDistance;
				//println("Neither: " + standardD);
			}
			if(parent.bounces>0) {
				standardD/=parent.bounces;
			}
			
			if(parent.stuck) {
				kids=0;
			}
			
			
			
			
			
			
			
			
			for(int i=0; i<kids && shots.size()<population; i++) {
				double distribution= (-2*Math.log(Math.random()) * Math.cos(2*Math.PI * Math.random())*standardD);
				double kidA = (distribution + parent.angle);
				double kidM = distribution + parent.magnitude;
				if(kidM>8) {
					kidM=8;
				}
				if(kidM<0) {
					kidM=2;
				}
			
				shots.add(new Ball(kidA, kidM, parent));
				size++;
			}
			ndx++;
			if(ndx>population-1) {
				println("Loop back");
				ndx=0;
			}
		}
		
		
		
		//println(generations++);
		previousMax=sorts.get(0);
		sunk=0;
		sorts.clear();
		sorted=false;
	}
	
	
	
	
	public void hillValley() {
		//tee
		tee = new Vector(wide/2, high*11/12, 0);
		//hole
		holes.add(new Hole(new Vector(wide/2, high/12,0)));	
				
				//stage walls
		walls.add(new Wall(new Vector(0,0), new Vector(wide,0)));//cap
		walls.add(new Wall(new Vector(wide,0), new Vector(wide,high)));//rW
		walls.add(new Wall(new Vector(0,high), new Vector(wide,high)));//bottom
		walls.add(new Wall(new Vector(0,0), new Vector(0,high)));//lW
				
				//hills and valleys
		hillValleys.add(new HV(new Vector(2,2,0), false, 1));
		hillValleys.add(new HV(new Vector(1,4,0), true, 1));
	}
	
	public void moat() {
		//tee
		tee = new Vector(wide/2, high*11/12, 0);
		//hole
		holes.add(new Hole(new Vector(wide/2, high/12,0)));	
		
		//stage walls
		walls.add(new Wall(new Vector(0,0), new Vector(wide,0)));//cap
		walls.add(new Wall(new Vector(wide,0), new Vector(wide,high)));//rW
		walls.add(new Wall(new Vector(0,high), new Vector(wide,high)));//bottom
		walls.add(new Wall(new Vector(0,0), new Vector(0,high)));//lW
		
		//hills and valleys
		hillValleys.add(new HV(new Vector(2,2,0), false, 1));
		hillValleys.add(new HV(new Vector(1,4,0), true, 1));
		
		//obstacles
		//water trap
		wTs.add(new Watertrap(new Vector(1,.75,0), new Vector(1, .25,0)));
		

	}
	
	public void greatWall() {
		//tee
		tee = new Vector(2.5, 5.5, 0);
		//hole
		holes.add(new Hole(new Vector(wide/6, high*11/12,0)));
		
		//stage walls
		walls.add(new Wall(new Vector(0,0), new Vector(wide,0)));//cap
		walls.add(new Wall(new Vector(wide,0), new Vector(wide,high)));//rW
		walls.add(new Wall(new Vector(0,high), new Vector(wide,high)));//bottom
		walls.add(new Wall(new Vector(0,0), new Vector(0,high)));//lW
		
		//hills and valleys
		hillValleys.add(new HV(new Vector(2,2,0), false, 1));
		hillValleys.add(new HV(new Vector(1,4,0), true, 1));
		
		//obstacles
		//extra walls
		walls.add(new Wall(new Vector(2,2,0), new Vector(1,4,0)));
		walls.add(new Wall(new Vector(1,4,0), new Vector(1,high+6,0)));
		
		
		extraWalls.add(new Wall(new Vector(2,2,0), new Vector(1,4,0)));
		extraWalls.add(new Wall(new Vector(1,4,0), new Vector(1,high,0)));
		
		gWall=true;
	}
	
	
	public void bunkerDown() {
		Vector hP = new Vector(1.5,3,0);
		//tee
		tee = new Vector(wide/2, 5.5, 0);
		//hole
		holes.add(new Hole(hP));	
				
		//stage walls
		walls.add(new Wall(new Vector(0,0), new Vector(wide,0)));//cap
		walls.add(new Wall(new Vector(wide,0), new Vector(wide,high)));//rW
		walls.add(new Wall(new Vector(0,high), new Vector(wide,high)));//bottom
		walls.add(new Wall(new Vector(0,0), new Vector(0,high)));//lW
		
		double gapSize=0.1;
		double boxW=1;
		double boxH=2;
		Vector tL=new Vector(hP.x-(boxW/2), hP.y-(boxH/2),0);
		Vector tR=new Vector(hP.x+(boxW/2), hP.y-(boxH/2),0);
		Vector bL=new Vector(hP.x-(boxW/2), hP.y+(boxH/2),0);
		Vector bR=new Vector(hP.x+(boxW/2), hP.y+(boxH/2),0);
		//hole box
		walls.add(new Wall(bL, bR));	//bottom
		walls.add(new Wall(bL, tL));	//left
		walls.add(new Wall(bR, tR));	//right
		//top
		walls.add(new Wall(tL, new Vector(hP.x-(gapSize/2), tL.y, 0)));
		walls.add(new Wall(tR, new Vector(hP.x+(gapSize/2), tR.y, 0)));
		
		
		bunker=true;
	}
	
	public static void main(String[] args) {
		PApplet.main("Main");
	}
}
