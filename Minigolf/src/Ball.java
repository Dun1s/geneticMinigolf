import java.util.ArrayList;

public class Ball {
	public static int numOfBalls;
	
	static double dt = 0.01;          //time increments
	double time; //what dt is added to
	static double radius=0.021335;           //radius of ball
	
	double mass=0.04593;           //mass in kg
	double magnitude;     //launch power in direction
	double angle;         //angle of launch
	
	double fric=0.015;
	double bounces;
	
	double stuckTimeH;
	double stuckTimeV;

	double timeOnHV;
	
	double sunkVel;
	
	double holeDistance;
	double practicalDistance;
	
	Vector closest = new Vector(200, 10, 0);
//	double closest=2000;
//	double velClosest=10;
	
	double lastBounceTime;
	
	boolean sunk;
	boolean stuck;
	
	boolean collideNext;
	
	boolean finished;
	
	double pScore;
	

	//public static double ballScale=280;
	
	//vectors
	Vector pos = new Vector();    //pos vec
	Vector vel = new Vector();    //vel relative to the ground
	Vector lVel = new Vector();
	Vector acc = new Vector();    //acc
	Vector friction = new Vector();
	
	Vector force = new Vector();  //mass * acc
	Vector color;
	Vector out = new Vector(0,0,1);
	
	Vector startingPos=main.tee;//Vector.add(main.tee,  main.shift);
	

	//lists
	ArrayList<Vector> forces = new ArrayList<>();	//this will hold all the forces
	//Class
	static Main main;
	Ball parent;
	
	public Ball(double ang, double mag) {
		angle=ang;
		magnitude=mag;
		vel.set(new Vector(magnitude*main.cos(main.radians((float)angle)), -1*magnitude*main.sin(main.radians((float)angle)),0));  //sets angle
	  	pos.set(new Vector(startingPos.x, startingPos.y, 0));                           //sets height
	  	acc.set(new Vector(0,0,0));
	  	color=new Vector(255);
	  	numOfBalls++;
	  
	  	lVel.set(vel);;
	}
	
	public Ball(double ang, double mag, Ball p) {
		angle=ang;
		magnitude=mag;
		vel.set(new Vector(magnitude*main.cos(main.radians((float)angle)), -1*magnitude*main.sin(main.radians((float)angle)),0));  //sets angle
	  	pos.set(new Vector(startingPos.x, startingPos.y, 0));                           //sets height
	  	acc.set(new Vector(0,0,0));
	  	color=p.color;
	  	numOfBalls++;
	  	
		pScore=p.score();
		lVel.set(vel);;
	}
	
	public Ball(Vector launch) {
		
		magnitude=launch.mag();
		vel.set(new Vector(launch.x, -1*launch.y,launch.z));  //sets angle
	  	pos.set(new Vector(startingPos.x, startingPos.y, 0));                           //sets height
	  	acc.set(new Vector(0,0,0));
	  	color=new Vector(main.random(0,255),main.random(0,255),main.random(0,255));
	  	numOfBalls++;
	  	angle=Math.sin(vel.y/magnitude)*(180/main.PI);
	  	//forces.add(friction);
	  	lVel.set(launch);;
	}
	
	
	public void render() {
		main.fill((float)color.x, (float)color.y, (float)color.z, 255);
		main.ellipse((float)(main.scale*pos.x+main.shift.x), (float)(main.scale*pos.y+main.shift.y), (float)(radius*main.scale*2),(float)(radius*main.scale*2));
	}
	
	public void math() {
		if(!finished) {
			
			force.add(vel.normalize().mult(-fric));
			for(Vector vec : forces) {
				force.add(vec);
			}
			acc=force.div(mass);
			force=new Vector();
		
			time+=dt;
			vel.add(Vector.mult(acc, dt));  //get new vel by adding a small part of acc
			pos.add(Vector.mult(vel, dt));  //update position by adding small vel
			
			
			if(pos.distance(main.holes.get(0).a).mag()<closest.x) {
				closest.x=pos.distance(main.holes.get(0).a).mag();
				closest.y=vel.mag();
				closest.z=time;
			}
			
		}
		
		if(((stuck || (vel.mag()<=0.1 && acc.mag()<0.33)) && !finished && !sunk)) {
			
			
			
			if(pos.distance(main.holes.get(0).a).mag()<0.5) {
				color = new Vector(0,0,150*0.5/pos.distance(main.holes.get(0).a).mag());
			}
			else if(closest.x<0.25) {
				color = new Vector(125,0,150*0.5/pos.distance(main.holes.get(0).a).mag());
			}
			else {
				color=new Vector(0,0,100);
			}
			if(stuck) {
				color=new Vector(255,0,0);
			}
			
			finished=true;
		}
			
		if(stuckTimeH>5.0 || stuckTimeV>2.0) {
			stuck=true;
		}
		
		outOfBounds();
		
		
		
	}
	
	public void outOfBounds() {
		if((pos.x>main.wide|| pos.x<0) && (pos.y>main.high || pos.y<0))
			stuck=true;
	}
	
	public void collide(Vector v1, Vector v2) {
		double velIn= vel.mag();
		
		pos.add(Vector.mult(vel,-dt));  //update position by adding small vel (j.pos=j.pos-j.vel*dt)
		
		Vector c = Vector.subtract(v1, v2);
		Vector wallNorm=c.cross(out).normalize();
		Vector rotatedWallNorm = Vector.rotate(wallNorm, 0.72*(0.5-Math.random()));	//rwallnorm=wallnorm.rotate((0.5-random())*0.004, axis=vec(0,0,1))
		
		Vector velRotatedWallNorm = Vector.mult(rotatedWallNorm, -2*vel.dot(rotatedWallNorm));
		
		double thing=0.2*rotatedWallNorm.dot(vel.normalize());
		

		

		
//		if(vel.mag()<1 || lastBounceTime-time<10*dt) {
//			pos.add(Vector.mult(Vector.mult(vel,-1.0), 2*dt));
//		}
		if(pos.x>v1.x && pos.x>v2.x && vel.x<=0) {	//fully to left of ball
			//main.println("Fully Right side");
			//vel.mult(1-main.random(0.4f, 0.42f)*(vel.normalize().dot(perp.normalize())));
			thing = 1.0-thing;
		}
		else if(pos.x<v1.x && pos.x<v2.x && vel.x>=0) { //fully to right of ball
			//main.println("Fully Left side");
			//vel.mult(1+main.random(0.4f, 0.42f)*(vel.normalize().dot(perp.normalize())));
			thing = 1.0+thing;
		}
		else if(pos.y<v1.y && pos.y<v2.y && vel.y>=0 ) { //fully below ball
			//main.println("Fully Above");
			//vel.mult(1-main.random(0.4f, 0.42f)*(vel.normalize().dot(perp.normalize())));
			thing = 1.0-thing;
		}
		else if(pos.y>v1.y && pos.y>v2.y && vel.y<=0) { //fully above ball
			//main.println("Fully Below");
			//vel.mult(1+main.random(0.4f, 0.42f)*(vel.normalize().dot(perp.normalize())));
			thing = 1.0+thing;
		}
		
		else if(pos.x<v1.x && pos.x>v2.x || pos.x>v1.x && pos.x<v2.x) { //Ball is between x-points (only gets here if not straight wall)
			//main.println("In between points");
			if(vel.x>0 || vel.y>0)
				thing=1+thing;
			else if(vel.x<0 || vel.y<0)
				thing = 1-thing;
			
		}
		
		lastBounceTime=time;
		vel.add(velRotatedWallNorm);
		vel.mult(thing);

		

		
		
		
		
		
		//main.println(c);
		if(vel.mag()>velIn && !stuck) {
			//main.println("Bad bounce");
//			stuck=true;
//			main.println(velIn);
//			main.println(vel.mag());
		}
			
		
		
		
		
		bounces++;
	}
	
	public void sunk() {
		finished=true;
		color=new Vector(200, 100, 200);
		sunk=true;
		sunkVel=vel.mag();
		
		if(pScore>10) {
			color=new Vector(255,255,0);
		}
	}
	
	public double score() {
		holeDistance=pos.distance(main.holes.get(0).a).mag();
		practicalDistance=holeDistance;
//		//for(Wall w : main.extraWalls) {
//		if(main.extraWall)
//		Wall w=main.extraWalls.get(1);
//			if((w.a.x < pos.x || w.b.x < pos.x) && pos.y>w.a.y) {
//				practicalDistance=50;
//		//	}
//		}
		
		
		double score=0;
		//Real Params
		
		if(stuck) {
			score-=100.0;
			practicalDistance=50;
		}
		else if(sunk) {
			score+=500.0;
			//score-=time*4.0;
		}
		else{
			score-=practicalDistance;//*main.scale;
		}
		score-=bounces*2;

		
		return score;
	}
	
	public String toString() {
		return "Angle: " + angle + "\nLaunch Mag: " + magnitude + "  \nSunk Vel: "+ sunkVel + "  Sunk: " + sunk + "  \nBounces: " + bounces + "  \nTime: " + time + "  \nDistance from hole: " + holeDistance + "   Practical Distance: " + practicalDistance + "\nScore: " + score();
	}
}
