package coursera.discreteoptimisation.knapsack;

import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;

public class Knapsack {

	static class Result {
		int value;
		int[] points;
		static Joiner joiner = Joiner.on(" ");
	
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(value).append(" 0").append("\n");
			sb.append(joiner.join(Ints.asList(points)));
			return sb.toString();
		}
	}
	
	static class Problem {
		int[] weights = null;
		int[] values = null;
		int count = 0;
		int capacity = 0;
		int idx = 0;
		
		public Problem(String filename) throws NumberFormatException, IOException{
			final Path path = Paths.get(filename);
			for (String line : Files.readAllLines(path)) {
				if (line.trim().isEmpty()) {
					continue;
				}
				if (weights == null) {
					// Fist line
					final String[] parts = line.split(" ");
					count = parseInt(parts[0]);
					capacity = parseInt(parts[1]);
					weights = new int[count];
					values = new int[count];
				} else {
					final String[] parts = line.split(" ");
					values[idx] = parseInt(parts[0]);
					weights[idx] = parseInt(parts[1]);
					idx++;
				}
			}
		}
	}

}
