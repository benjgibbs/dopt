package dopt;

import static java.lang.Math.max;

import java.io.IOException;
import java.net.URISyntaxException;

public class KnapsackDP extends Knapsack {

	public static void main(String[] args) throws URISyntaxException, IOException {
		final String filename = args[0];
		final KnapsackDP runner = new KnapsackDP();
		System.out.println(runner.run(new Problem(filename)));
	}

	public Knapsack.Result run(Problem pr) throws URISyntaxException, IOException {

		// items x capacity
		int[][] M = new int[pr.count][];
		for (int i = 0; i < pr.count; i++) {
			M[i] = new int[pr.capacity + 1];
			for (int c = 1; c < pr.capacity + 1; c++) {
				int w = pr.weights[i];
				int v = pr.values[i];
				int p = i == 0 ? 0 : M[i - 1][c];
				int q = 0;
				if (w <= c) {
					if (i == 0) {
						q = v;
					} else if (i > 0) {
						q = M[i - 1][c - w] + v;
					}
				}
				M[i][c] = max(p, q);
			}
		}

//		for (int r = 0; r < pr.capacity + 1; r++) {
//			for (int c = 0; c < pr.count; c++) {
//				System.out.print(M[c][r] + "\t");
//			}
//			System.out.println();
//		}
//
//		System.out.println("- - - ");

		Knapsack.Result r = new Knapsack.Result(pr.count);
		r.value = M[pr.count - 1][pr.capacity];
		int i = pr.count - 1;
		int c = pr.capacity;
		while (c > 0 && i >= 0) {
			int w = pr.weights[i];
			if (i == 0) {
				r.points[i] = (c-w >= 0) ? 1 : 0;
			} else if (M[i][c] == M[i - 1][c]) {
				// did not select i
			} else {
				r.points[i] = 1;
				c = c - w;
			}
			i--;
		}
		return r;
	}
}
