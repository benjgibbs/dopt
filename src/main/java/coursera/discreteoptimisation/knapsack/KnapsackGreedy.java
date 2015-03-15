package coursera.discreteoptimisation.knapsack;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

public class KnapsackGreedy extends Knapsack {
	public static void main(String[] args) throws URISyntaxException, IOException {
		final String filename = args[0];
		final KnapsackGreedy runner = new KnapsackGreedy();
		System.out.println(runner.run(new Problem(filename)));
	}

	public Result run(final Problem pr) throws URISyntaxException, IOException {
		Result r = new Result();
		r.points = new int[pr.count];
		
		Map<Double, Integer> m = Maps.newTreeMap();
		for(int i = 0; i < pr.count; i++){
			m.put((double)pr.values[i]/pr.weights[i], i);
		}
		int capacity = 0;
		int value = 0;
		for (Entry<Double, Integer> kv : m.entrySet()) {
			int i = kv.getValue();
			capacity += pr.weights[i];
			if(capacity <= pr.capacity){
				r.points[i] = 1;
				value += pr.values[i];
			} else {
				break;
			}
		}
		r.value = value;
				
		return r;
	}

}
