package dopt.tsp;

class Edge implements Comparable<Edge> {
	final int a;
	final int b;

	public Edge(int a, int b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public String toString() {
		return String.format("(a:%d,b:%d)", a, b);
	}

	@Override
	public int compareTo(Edge o) {
		if (a > o.a)
			return 1;
		if (a < o.a)
			return -1;
		if (b > o.b)
			return 1;
		if (b < o.b)
			return -1;
		return 0;
	}
}