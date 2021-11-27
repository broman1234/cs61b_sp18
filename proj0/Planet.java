public class Planet {
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;
	public static final double g = 6.67e-11;

	public Planet(double xxPos, double yyPos, double xxVel,
              double yyVel, double mass, String imgFileName) {
		this.xxPos = xxPos;
		this.yyPos = yyPos;
		this.xxVel = xxVel;
		this.yyVel = yyVel;
		this.mass = mass;
		this.imgFileName = imgFileName;
	}

	public Planet(Planet p) {
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
	    yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;
	}

	public double calcDistance(Planet p) {
		double r = Math.sqrt(Math.pow(p.xxPos - xxPos, 2) + Math.pow(p.yyPos - yyPos, 2));
		return r;
	}
	
	public double calcForceExertedBy(Planet p) {
		double r = calcDistance(p);
		double f = (g * mass * p.mass) / (r * r);
		return f;
	}

	public double calcForceExertedByX(Planet p) {
		double r = calcDistance(p);
		double f = calcForceExertedBy(p);
		double fX = f * (p.xxPos - xxPos) / r;
		return fX;
	}

	public double calcForceExertedByY(Planet p) {
		double r = calcDistance(p);
		double f = calcForceExertedBy(p);
		double fY = f * (p.yyPos - yyPos) / r;
		return fY;
	}

	public double calcNetForceExertedByX(Planet[] allBodys) {
		double netFx = 0;
		for (Planet p : allBodys) {
			if (this.equals(p)) {
				continue;
			}
			double fX = calcForceExertedByX(p);
			netFx = netFx + fX;
		}
		return netFx;
	}

	public double calcNetForceExertedByY(Planet[] allBodys) {
		double netFy = 0;
		for (Planet p : allBodys) {
			if (this.equals(p)) {
				continue;
			}
			double fY = calcForceExertedByY(p);
			netFy = netFy + fY;
		}
		return netFy;
	}

	public void update(double dt, double fX, double fY) {
		double aNetX = fX / mass;
		double aNetY = fY / mass;
		xxVel = xxVel + dt * aNetX;
		yyVel = yyVel + dt * aNetY;
		xxPos = xxPos + dt * xxVel;
		yyPos = yyPos + dt * yyVel;
	}

	public void draw() {
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}
}