public class Planet{
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;
	public Planet(double xP, double yP, double xV,
		           double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}
	public Planet(Planet p){
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;
	}

	public double calcDistance(Planet p){
		double DistanceX = (xxPos - p.xxPos)*(xxPos - p.xxPos) + (yyPos - p.yyPos)*(yyPos - p.yyPos);
		return DistanceX;
	}

	public double calcForceExertedBy(Planet p){
		double R = calcDistance(p);
		double G = 6.67e-11;
		double Force = G * mass * p.mass / R;
		return Force;
	}

	public double calcForceExertedByX(Planet p){
		double Rx = p.xxPos - xxPos;
		double ForceX = calcForceExertedBy(p) * Rx / Math.pow(calcDistance(p),0.5);
		return ForceX;
	}

	public double calcForceExertedByY(Planet p){
		double Ry = p.yyPos - yyPos;
		double ForceY = calcForceExertedBy(p) * Ry / Math.pow(calcDistance(p),0.5);
		return ForceY;
	}

	public double calcNetForceExertedByX(Planet[] plants){
		int i = 0;
		double forcex = 0;
		for (i = 0; i < plants.length; i++){
			if(calcDistance(plants[i]) == 0){
				continue;
			}
			forcex = forcex + calcForceExertedByX(plants[i]);
		} 
		return forcex;
	}

	public double calcNetForceExertedByY(Planet[] plants){
		int i = 0;
		double forcey = 0;
		for (i = 0; i < plants.length; i++){
			/* this if is used to singled out the Plant itself*/
			if(calcDistance(plants[i]) == 0){
				continue;
			}
			forcey = forcey + calcForceExertedByY(plants[i]);
		} 
		return forcey;
	}

	public void update(double dt, double fX, double fY){
		double ax = fX / mass;
		double ay = fY / mass;
		xxVel = xxVel + dt * ax;
		yyVel = yyVel + dt * ay;
		xxPos = xxPos + xxVel * dt;
		yyPos = yyPos + yyVel * dt;
	}

	public void draw(){
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}
}