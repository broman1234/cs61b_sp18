public class NBody {
	public static double readRadius(String fileName) {
		In in = new In(fileName);
		int numOfPlanet = in.readInt();
		double radiusOfUniverse = in.readDouble();
		return radiusOfUniverse; 
	}

	public static Planet[] readPlanets(String fileName) {
		In in = new In(fileName);
		int numOfPlanet = in.readInt();
		double radiusOfUniverse = in.readDouble(); 
		Planet[] arrayOfPlanets = new Planet[numOfPlanet];
		int i = 0;
		while (i < numOfPlanet) {
			double xxPos = in.readDouble();
			double yyPos = in.readDouble();
			double xxVel = in.readDouble();
			double yyVel = in.readDouble();
			double mass = in.readDouble();
			String imgFileName = in.readString();
			Planet p = new Planet(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
			arrayOfPlanets[i] = p;
			i = i + 1;
		}
		return arrayOfPlanets;
	}

	public static void main(String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double radiusOfUniverse = readRadius(filename);
		Planet[] planets = readPlanets(filename);

		/* the set scale for drawing on screen */
        StdDraw.setXscale(-radiusOfUniverse, +radiusOfUniverse); 
        StdDraw.setYscale(-radiusOfUniverse, +radiusOfUniverse);

        /* Clears the drawing window. */
		StdDraw.clear();

		/* Draw universe background */
		StdDraw.picture(0, 0, "images/starfield.jpg");

		/* Draw all of the planets */
		for (int i = 0; i < planets.length; i++) {
			planets[i].draw();
		}
		StdDraw.show();

		/* Animation */

		/* Enable double buffering */
		StdDraw.enableDoubleBuffering();

		double time = 0;
		while(time <= T) {
			double [] xForces = new double[planets.length];
			double [] yForces = new double[planets.length];

			for (int i = 0; i < planets.length; i++) {
				xForces[i] = planets[i].calcNetForceExertedByX(planets);
				yForces[i] = planets[i].calcNetForceExertedByY(planets);
			}

			for (int i = 0; i < planets.length; i++) {
				planets[i].update(dt, xForces[i], yForces[i]);
			}
			StdDraw.picture(0, 0, "images/starfield.jpg");
			for (int i = 0; i < planets.length; i++) {
				planets[i].draw();
		 	}
		 	StdDraw.show();
		 	StdDraw.pause(10);
			time += dt;
		}

		StdOut.printf("%d\n", planets.length);
		StdOut.printf("%.2e\n", radiusOfUniverse);
		for (int i = 0; i < planets.length; i++) {
    		StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                	planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                	planets[i].yyVel, planets[i].mass, planets[i].imgFileName);   
		}

		// StdAudio.play("audio/2001.mid");
	}
}