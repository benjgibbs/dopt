package dopt.tsp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

public class TSP {

	private static final int MAX_STEP = 5;

	private static final int ITERATIONS = 1000;

	static class Point {
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

	static class Edge implements Comparable<Edge> {
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

	public static void main(String[] args) throws IOException {
		TSP tsp = new TSP();
		tsp.solve(args[0]);
	}

	private boolean print = false;

	private static Random rand = new Random();

	private void solve(String pathStr) throws NumberFormatException, IOException {
		rand.setSeed(1);
		Path path = Paths.get(pathStr);

		List<Point> points = readFile(path);
		double[][] distances = calculateDistances(points);
		printDistances(distances);

		List<Integer> bestResult = new ArrayList<>();
		double minDistance = Double.MAX_VALUE;
		for (int i = 0; i < ITERATIONS; i++) {
			List<Integer> result = new ArrayList<>();
			double distance = runIteration(points, distances, result);
			if (distance < minDistance) {
				System.out.println("Better: " + distance);
				minDistance = distance;
				bestResult.clear();
				bestResult.addAll(result);
			}
		}

		display(points, bestResult);

		System.out.printf("%f 0\n", minDistance);
		Joiner joiner = Joiner.on(" ");
		System.out.println(joiner.join(bestResult));

	}

	private double runIteration(List<Point> points, double[][] distances, List<Integer> result) {
		Multimap<Double, Edge> ordering = sortDistances(distances);
		Set<Integer> joined = new HashSet<>();
		double distance = 0.0;
		int last = rand.nextInt(points.size());
		int stepSz = rand.nextInt(MAX_STEP)+1;
		
		while (joined.size() < points.size()) {
			ArrayList<Double> keySet = Lists.newArrayList(ordering.keySet());
			for(int i = 0; i < keySet.size(); i+=stepSz){
				List<Edge> orderedEdges = new ArrayList<>();
				for(int j=0; j < stepSz; j++){
					int idx = Math.min(i+j, keySet.size()-1);
					orderedEdges.addAll(ordering.get(keySet.get(idx)));
				}
				Collections.shuffle(orderedEdges, rand);
				for (Edge edge : orderedEdges) {
					if (edge.a == last && !joined.contains(edge.b)) {
						distance += getDistance(distances, edge.a, edge.b);
						result.add(edge.b);
						joined.add(edge.b);
						last = edge.b;
						break;
					} else if (edge.b == last && !joined.contains(edge.a)) {
						distance += getDistance(distances, edge.a, edge.b);
						result.add(edge.a);
						joined.add(edge.a);
						last = edge.a;
						break;
					}
				}
			}
		}
		return distance;
	}
	
	private double getDistance(double[][] distances, int a, int b){
		if (a > b) {
			return distances[a][b];
		} else {
			return distances[b][a];
		}
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
		if (print) {
			for (int i = 0; i < distances.length; i++) {
				for (int j = 0; j < distances[i].length; j++) {
					System.out.printf("%.2f\t", distances[i][j]);
				}
				System.out.println();
			}
		}
	}

	private double[][] calculateDistances(List<Point> points) {
		// only calculate the lower left triangle
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
