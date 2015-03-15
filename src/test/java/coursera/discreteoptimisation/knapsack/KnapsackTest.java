package coursera.discreteoptimisation.knapsack;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import coursera.discreteoptimisation.knapsack.Knapsack.Problem;

public class KnapsackTest {

	final String prefix = "/Users/ben/Google Drive/Code/DO/knapsack/data/";
	
	@Test
	public void testDP1() throws URISyntaxException, IOException {
		KnapsackDP dp = new KnapsackDP();
		Knapsack.Result res = dp.run(new Problem(prefix+"ks_lecture_dp_1"));
		System.out.println(res.toString());
		assertThat(res.value, is(equalTo(11)));
		assertThat(res.points, is(new int[]{1,1,0}));
	}
	
	@Test
	public void testDP2() throws URISyntaxException, IOException {
		KnapsackDP dp = new KnapsackDP();
		Knapsack.Result res = dp.run(new Problem(prefix+"ks_lecture_dp_2"));
		System.out.println(res.toString());
		assertThat(res.value, is(equalTo(44)));
		assertThat(res.points, is(new int[]{1,0,0,1}));
	}
	
	@Test
	public void testDP3() throws URISyntaxException, IOException {
		KnapsackDP dp = new KnapsackDP();
		Knapsack.Result res = dp.run(new Problem(prefix+"ks_4_0"));
		System.out.println(res.toString());
		assertThat(res.value, is(equalTo(19)));
		assertThat(res.points, is(new int[]{0,0,1,1}));
	}
	
	@Test
	public void testFirstOnly() throws URISyntaxException, IOException {
		KnapsackDP dp = new KnapsackDP();
		Knapsack.Result res = dp.run(new Problem("resources/knapsack/first-only.txt"));
		System.out.println(res.toString());
		assertThat(res.value, is(equalTo(3)));
		assertThat(res.points, is(new int[]{0,0,1}));
	}

	
	@Test
	public void testGreedy1() throws URISyntaxException, IOException {
		KnapsackGreedy dp = new KnapsackGreedy();
		Knapsack.Result res = dp.run(new Problem(prefix+"ks_lecture_dp_1"));
		System.out.println(res.toString());
		assertThat(res.value, is(equalTo(11)));
		assertThat(res.points, is(new int[]{1,1,0}));
	}
	
	@Test
	public void testGreedy2() throws URISyntaxException, IOException {
		KnapsackGreedy dp = new KnapsackGreedy();
		Knapsack.Result res = dp.run(new Problem(prefix+"ks_lecture_dp_2"));
		System.out.println(res.toString());
		assertThat(res.value, is(equalTo(44)));
		assertThat(res.points, is(new int[]{1,0,0,1}));
	}
	
}
