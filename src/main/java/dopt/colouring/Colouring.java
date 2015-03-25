package dopt.colouring;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

public class Colouring {

	static class Result {
		public Result(int sz){
			colouring =  new ArrayList<>(sz);
			for(int i = 0; i < sz; i++){
				colouring.add(null);
			}
		}
		
		int numColors;
		List<Integer> colouring;

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(numColors).append(" 0").append("\r");
			Joiner joiner = Joiner.on(" ");
			builder.append(joiner.join(colouring));
			return builder.toString();
		}

	}

	Result calculate(String path) throws NumberFormatException, IOException {
		
		Multimap<Integer, Integer> graph = readGraph(path);
		
		
		Multimap<Integer, Integer> order = TreeMultimap.create(Comparator.reverseOrder(), Comparator.naturalOrder());

		for (Integer key : graph.keySet()) {
			int size = graph.get(key).size();
			order.put(size, key);
		}

		final int iters = 10;

		Result result = calculateResult(graph, order);
		
		for(int i = 0; i < iters; i++){
			Result r1 = calculateResult(graph, order);
			if(r1.numColors < result.numColors){
				result = r1;
			}
		}
		return result;
	}

	private Result calculateResult(Multimap<Integer, Integer> graph, Multimap<Integer, Integer> order) {
		Result result = new Result(graph.keySet().size());
		
		Set<Integer> s = new HashSet<>();
		for (Entry<Integer, Integer> e : order.entries()) {
			int i = e.getValue();
			s.add(i);
			int nextColour = findMinColor(result,graph,i);
			result.colouring.set(i,nextColour);
		}

		result.numColors = result.colouring.stream().reduce(0, (x,y) -> (x > y) ? x : y);
		return result;
	}

	private int findMinColor(Result r, Multimap<Integer, Integer> graph, Integer i) {
		int minColour = 0;
		Collection<Integer> neighbours = graph.get(i);
		while(true){
			boolean unique = true;
			for(Integer n : neighbours){
				Integer col = r.colouring.get(n);
				if(col != null && minColour == col){
					minColour++;
					unique = false;
					break;
				}
			}
			if(unique){
				break;
			}
		}
		return minColour;
	}

	Multimap<Integer, Integer> readGraph(String pathStr) throws NumberFormatException, IOException {
		Multimap<Integer, Integer> edges = null;

		Path path = Paths.get(pathStr);
		for (String line : Files.readAllLines(path)) {
			String[] parts = line.split(" ");
			if (edges == null) {
				edges = ArrayListMultimap.create();
			} else {
				Integer first = Integer.valueOf(parts[0]);
				Integer second = Integer.valueOf(parts[1]);
				edges.put(first, second);
				edges.put(second, first);
			}
		}
		return edges;
	}

	public static void main(String[] args) throws IOException {
		Colouring c = new Colouring();
		System.out.println(c.calculate("resources/colouring/gc_20_1"));
	}
}
