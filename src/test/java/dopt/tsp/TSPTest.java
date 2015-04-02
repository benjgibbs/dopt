package dopt.tsp;

import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.*;

import org.junit.Test;

public class TSPTest {
	
	TSP tsp = new TSP();
	@Test
	public void checkBasic5_1() throws Exception {
		Result result = tsp.solve("resources/tsp/tsp_5_1");
		assertThat(result.distance, closeTo(4.0,1e-15));
	}
}
