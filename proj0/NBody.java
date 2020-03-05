public class NBody{
	public static double readRadius(String fileName){
		In in = new In(fileName);
		in.readInt();
		double Radius = in.readDouble();

		return Radius;
	}

	public static Planet[] readPlanets(String filename){
		In in = new In(filename);
		int num = in.readInt();
		in.readDouble();
		Planet[] Planets = new Planet[num];

		int i = 0;
		for(i = 0; i < num; i++){
			double xP = in.readDouble();
			double yP = in.readDouble();
			double xV = in.readDouble();
			double yV = in.readDouble();
			double m = in.readDouble();
			String img = in.readString();
			Planets[i] = new Planet(xP, yP, xV, yV, m, img);
		}
		return Planets;
	}

	public static void main(String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double uniRadius = NBody.readRadius(filename);
		Planet[] Planets = NBody.readPlanets(filename);
		/*draw the background*/
		StdDraw.setScale(-uniRadius, uniRadius);
		StdDraw.clear();
		StdDraw.picture(0, 0, "images/starfield.jpg");
		
		/*
		Set the Animation
		 */
		StdDraw.enableDoubleBuffering();
		double time = 0;
		
		/* Set up a loop to loop until time variable reaches T*/
		for (time = 0; time < T; time += dt){
			double[] xForces = new double[Planets.length];
			double[] yForces = new double[Planets.length];
			/*do not make any calls to update until all forces have been
			 calculated and safely stored in xForces and yForces*/
			/*update position and velocities of each planet*/
			for (int i = 0; i < Planets.length; i++){
				xForces[i] = Planets[i].calcNetForceExertedByX(Planets);
				yForces[i] = Planets[i].calcNetForceExertedByY(Planets);
			}
			for (int i = 0; i < Planets.length; i++){
				Planets[i].update(dt, xForces[i], yForces[i]);
			}
			/*draw the background*/
			StdDraw.picture(0, 0, "images/starfield.jpg");
			

			/*draw the planets*/
			for (int count = 0; count < Planets.length; count++){
			Planets[count].draw(); //之前报错在于错误调用Planet.draw(),导致没有生成正确的类
			}
			StdDraw.show();
			StdDraw.pause(10);
		}

		/*printing the universe*/
		StdOut.printf("%d\n", Planets.length);
		StdOut.printf("%.2e\n", uniRadius);
		for (int i = 0; i < Planets.length; i++) {
    		StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  Planets[i].xxPos, Planets[i].yyPos, Planets[i].xxVel,
                  Planets[i].yyVel, Planets[i].mass, Planets[i].imgFileName);
		}
	}
}
