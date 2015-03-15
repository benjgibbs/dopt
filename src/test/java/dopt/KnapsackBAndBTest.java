package dopt;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import dopt.Knapsack;
import dopt.KnapsackBAndB;
import dopt.Knapsack.Problem;

public class KnapsackBAndBTest {

    final String prefix = "B:/Code/d-opt/knapsack/data/";

    @Test
    public void testDP1() throws URISyntaxException, IOException {
        KnapsackBAndB dp = new KnapsackBAndB();
        Knapsack.Result res = dp.run(new Problem(prefix + "ks_lecture_dp_1"));
        System.out.println(res.toString());
        assertThat(res.value, is(equalTo(11)));
        assertThat(res.points, is(new int[] { 1, 1, 0 }));
    }

    @Test
    public void testDP2() throws URISyntaxException, IOException {
        KnapsackBAndB dp = new KnapsackBAndB();
        Knapsack.Result res = dp.run(new Problem(prefix + "ks_lecture_dp_2"));
        System.out.println(res.toString());
        assertThat(res.value, is(equalTo(44)));
        assertThat(res.points, is(new int[] { 1, 0, 0, 1 }));
    }

    @Test
    public void testDP3() throws URISyntaxException, IOException {
        KnapsackBAndB dp = new KnapsackBAndB();
        Knapsack.Result res = dp.run(new Problem(prefix + "ks_4_0"));
        System.out.println(res.toString());
        assertThat(res.value, is(equalTo(19)));
        assertThat(res.points, is(new int[] { 0, 0, 1, 1 }));
    }

    @Test
    public void takeTheThird() throws URISyntaxException, IOException {
        KnapsackBAndB dp = new KnapsackBAndB();
        Knapsack.Result res = dp.run(new Problem("resources/knapsack/take-the-third.txt"));
        System.out.println(res.toString());
        assertThat(res.value, is(equalTo(3)));
        assertThat(res.points, is(new int[] { 0, 0, 1 }));
    }

    @Test
    public void oneFits() throws URISyntaxException, IOException {
        KnapsackBAndB dp = new KnapsackBAndB();
        Knapsack.Result res = dp.run(new Problem("resources/knapsack/one-fits.txt"));
        System.out.println(res.toString());
        assertThat(res.value, is(equalTo(2)));
        assertThat(res.points, is(new int[] { 1 }));
    }

    @Test
    public void oneTooBig() throws URISyntaxException, IOException {
        KnapsackBAndB dp = new KnapsackBAndB();
        Knapsack.Result res = dp.run(new Problem("resources/knapsack/one-too-big.txt"));
        System.out.println(res.toString());
        assertThat(res.value, is(equalTo(0)));
        assertThat(res.points, is(new int[] { 0 }));
    }

    @Test
    public void twoFirst() throws URISyntaxException, IOException {
        KnapsackBAndB dp = new KnapsackBAndB();
        Knapsack.Result res = dp.run(new Problem("resources/knapsack/two-first.txt"));
        System.out.println(res.toString());
        assertThat(res.value, is(equalTo(3)));
        assertThat(res.points, is(new int[] { 1, 0 }));
    }

    @Test
    public void twoSecond() throws URISyntaxException, IOException {
        KnapsackBAndB dp = new KnapsackBAndB();
        Knapsack.Result res = dp.run(new Problem("resources/knapsack/two-second.txt"));
        System.out.println(res.toString());
        assertThat(res.value, is(equalTo(3)));
        assertThat(res.points, is(new int[] { 0, 1 }));
    }

    @Test
    public void twoBoth() throws URISyntaxException, IOException {
        KnapsackBAndB dp = new KnapsackBAndB();
        Knapsack.Result res = dp.run(new Problem("resources/knapsack/two-both.txt"));
        System.out.println(res.toString());
        assertThat(res.value, is(equalTo(6)));
        assertThat(res.points, is(new int[] { 1, 1 }));
    }

    @Test
    public void shortCircuits() throws URISyntaxException, IOException {
        KnapsackBAndB dp = new KnapsackBAndB();
        Knapsack.Result res = dp.run(new Problem("resources/knapsack/shortcircuit.txt"));
        System.out.println(res.toString());
        assertThat(res.value, is(equalTo(50)));
        assertThat(res.points, is(new int[] { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0 }));
    }

}
