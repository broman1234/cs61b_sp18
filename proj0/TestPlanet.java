public class TestPlanet {
    /* Test pairwise force between two planets */
	public static void main(String[] args) {
		Planet p1 = new Planet(1.0, 1.0, 3.0, 4.0, 5.0, "jupiter.gif");
		Planet p2 = new Planet(3.0, 3.0, 0.0, 0.0, 10.0, "sun.gif");
		System.out.println(p1.calcForceExertedBy(p2));
	}
}