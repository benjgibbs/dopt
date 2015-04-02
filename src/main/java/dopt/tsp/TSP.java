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

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

public class TSP {

	private static final int MAX_STEP = 2;

	private static final int ITERATIONS = 1000;

	private static final int MAX_FAIL = 5;

	public static void main(String[] args) throws IOException {
		TSP tsp = new TSP();
		Result result = tsp.solve(args[0]);
		result.display();
		System.out.println(result);
	}

	private boolean print = false;

	private static Random rand = new Random();

	public Result solve(String pathStr) throws NumberFormatException, IOException {
		rand.setSeed(1);
		Path path = Paths.get(pathStr);

		List<Point> points = readFile(path);
		double[][] distances = calculateDistances(points);
		printDistances(distances);

		Result best = new Result(Double.MAX_VALUE, new ArrayList<>(), points);
		for (int i = 0; i < ITERATIONS; i++) {
			List<Integer> route = runIteration(points, distances);
			double distance = calcDistance(distances, route);
			int failCount = 0;
			do {
				failCount++;
				Edge e = findLongest(distances, route);
				int p1 = findClosest(distances, e.a, points.size());
				if (p1 != e.b) {
					List<Integer> newRoute = new ArrayList<>(points.size());
					for (int pi = 0; pi < route.size(); pi++) {
						int p = route.get(pi);
						if (p == e.a) {
							newRoute.add(e.a);
							newRoute.add(p1);
						} else if (p == e.b) {
							newRoute.add(e.b);
						} else if (p == p1) {
							// skip
						} else {
							newRoute.add(p);
						}
					}
					double newDistance = calcDistance(distances, newRoute);
					if (newDistance < distance) {
						// System.out.println("Saved: " +
						// (distance-newDistance));
						route = newRoute;
						distance = newDistance;
						failCount--;
					}
				}

			} while (failCount < MAX_FAIL);

			if (distance < best.distance) {
				best = new Result(distance, route, points);
				// best.display();
			}
		}
		return best;
	}

	private int findClosest(double[][] distances, int a, int numPts) {
		double minSoFar = Double.MAX_VALUE;
		int minPos = -1;
		for (int i = 0; i < numPts; i++) {
			if (i == a)
				continue;
			double dist = getDistance(distances, a, i);
			if (dist < minSoFar) {
				minSoFar = dist;
				minPos = i;
			}
		}
		return minPos;
	}

	private Edge findLongest(double[][] distances, List<Integer> route) {
		double longestSoFar = Double.MIN_VALUE;
		int last = route.get(route.size() - 1);
		Edge result = null;
		for (int i : route) {
			if (last != i) {
				double dist = getDistance(distances, last, i);
				if (dist > longestSoFar) {
					longestSoFar = dist;
					result = new Edge(last, i);
				}
			}
		}
		return result;
	}

	private List<Integer> runIteration(List<Point> points, double[][] distances) {
		List<Integer> result = new ArrayList<>();

		Multimap<Double, Edge> ordering = sortDistances(distances);
		Set<Integer> joined = new HashSet<>();
		int last = rand.nextInt(points.size());
		int stepSz = rand.nextInt(MAX_STEP) + 1;

		while (joined.size() < points.size()) {
			ArrayList<Double> keySet = Lists.newArrayList(ordering.keySet());
			for (int i = 0; i < keySet.size(); i += stepSz) {
				List<Edge> orderedEdges = new ArrayList<>();
				for (int j = 0; j < stepSz; j++) {
					int idx = Math.min(i + j, keySet.size() - 1);
					orderedEdges.addAll(ordering.get(keySet.get(idx)));
				}
				Collections.shuffle(orderedEdges, rand);
				for (Edge edge : orderedEdges) {
					if (edge.a == last && !joined.contains(edge.b)) {
						result.add(edge.b);
						joined.add(edge.b);
						last = edge.b;
						break;
					} else if (edge.b == last && !joined.contains(edge.a)) {
						result.add(edge.a);
						joined.add(edge.a);
						last = edge.a;
						break;
					}
				}
			}
		}
		return result;
	}

	private double calcDistance(double[][] distances, List<Integer> points) {
		double result = 0.0;
		int last = points.get(points.size() - 1);
		for (Integer i : points) {
			result += getDistance(distances, i, last);
			last = i;
		}
		return result;
	}

	private double getDistance(double[][] distances, int a, int b) {
		if (a > b) {
			return distances[a][b];
		} else {
			return distances[b][a];
		}
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
