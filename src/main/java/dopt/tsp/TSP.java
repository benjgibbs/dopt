package dopt.tsp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;

import com.google.common.base.Joiner;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

public class TSP {

	static class Point {
		final double x;
		final double y;
		final int id;

		public Point(int id, double x, double y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}

		public String toString() {
			return String.format("%d=(x:%f,y:%f)", id, x, y);
		}
	}

	static class Edge implements Comparable<Edge> {
		final int a;
		final int b;

		public Edge(int a, int b) {
			this.a = a;
			this.b = b;
		}

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

	public static void main(String[] args) throws IOException {
		TSP tsp = new TSP();
		tsp.solve(args[0]);
	}

	private boolean print = false;

	private void solve(String pathStr) throws NumberFormatException, IOException {
		Path path = Paths.get(pathStr);

		List<Point> points = readFile(path);
		double[][] distances = calculateDistances(points);
		printDistances(distances);
		Multimap<Double, Edge> ordering = sortDistances(distances);
		Set<Integer> joined = new HashSet<>();
		List<Integer> result = new ArrayList<>();

		double distance = 0.0;
		int last = Integer.MAX_VALUE;
		while (joined.size() < points.size()) {
			if (result.isEmpty()) {
				Entry<Double, Edge> next = ordering.entries().iterator().next();
				Edge edge = next.getValue();
				result.add(edge.a);
				result.add(edge.b);
				joined.add(edge.a);
				joined.add(edge.b);
				last = edge.b;
				distance += next.getKey();
			} else {
				for (Entry<Double, Edge> i : ordering.entries()) {
					Double hopDist = i.getKey();
					Edge edge = i.getValue();
					if (edge.a == last && !joined.contains(edge.b)) {
						distance += hopDist;
						result.add(edge.b);
						joined.add(edge.b);
						last = edge.b;
						break;
					} else if (edge.b == last && !joined.contains(edge.a)) {
						distance += hopDist;
						result.add(edge.a);
						joined.add(edge.a);
						last = edge.a;
						break;
					}
				}
			}
		}

		int first = result.get(0);
		if (first > last) {
			distance += distances[first][last];
		} else {
			distance += distances[last][first];
		}
		display(points, result);

		System.out.printf("%f 0\n", distance);
		Joiner joiner = Joiner.on(" ");
		System.out.println(joiner.join(result));

	}

	private void display(final List<Point> points, final List<Integer> result) {
		JFrame pane = new JFrame();
		pane.add(new TSPDisplay(points, result));
		pane.pack();
		pane.setVisible(true);

	}

	private Multimap<Double, Edge> sortDistances(double[][] distances) {
		Multimap<Double, Edge> result = TreeMultimap.create();
		for (int r = 0; r < distances.length; r++) {
			for (int c = 0; c < distances[r].length; c++) {
				result.put(distances[r][c], new Edge(r, c));
			}
		}
		return result;
	}

	private void printDistances(double[][] distances) {
		if (print ) {
			for (int i = 0; i < distances.length; i++) {
				for (int j = 0; j < distances[i].length; j++) {
					System.out.printf("%.2f\t", distances[i][j]);
				}
				System.out.println();
			}
		}
	}

	private double[][] calculateDistances(List<Point> points) {
		// only calculate the upper left triangle
		double[][] result = new double[points.size()][];
		for (int r = 0; r < points.size(); r++) {
			int rowSz = r;
			result[r] = new double[rowSz];
			for (int c = 0; c < rowSz; c++) {
				if (r == c) {
					result[r][c] = 0.0;
				} else {
					Point a = points.get(r);
					Point b = points.get(c);

					result[r][c] = Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
				}
			}
		}

		return result;
	}

	private List<Point> readFile(Path path) throws IOException {
		List<Point> points = null;
		int id = 0;
		for (String l : Files.readAllLines(path)) {
			if (points == null) {
				int sz = Integer.parseInt(l);
				points = new ArrayList<>(sz);
			} else {
				String[] split = l.split(" ");
				//@formatter:off
				Point p = new Point(id++, 
						Double.parseDouble(split[0]),
						Double.parseDouble(split[1]));
				//@formatter:on
				points.add(p);
			}
		}
		return points;
	}
}
