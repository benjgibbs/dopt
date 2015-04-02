package dopt.tsp;

class Point {
	final double x;
	final double y;
	final int id;

	public Point(int id, double x, double y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return String.format("%d=(x:%f,y:%f)", id, x, y);
	}
}