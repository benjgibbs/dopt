package dopt.colouring;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import dopt.colouring.Colouring.Result;

public class ColouringTest {

	Colouring c = new Colouring();

	@Test
	public void test_4_1() throws NumberFormatException, IOException {
		Result result = c.calculate("resources/colouring/gc_4_1");
		System.out.println(result);
		assertThat(result.numColors, equalTo(1));
	}

	@Test
	public void testOthers() {
		Path path = Paths.get("resources", "colouring");
		List<String> failedHigh = new ArrayList<>();
		List<String> failedLow = new ArrayList<>();
		List<String> ok = new ArrayList<>();
		int okTotal = 0;
		int highTotal = 0;	
		int lowTotal = 0;	
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
			for (Path file : ds) {
				Result result = c.calculate(file.toString());
				// System.out.println(file.toString());
				// System.out.println(result);
				Path basename = file.getName(file.getNameCount() - 1);
				String expectStr = basename.toString().split("_")[2];
				
				int expect = Integer.parseInt(expectStr); 
				if ( expect < result.numColors) {
					highTotal += result.numColors;
					failedHigh.add(file.toString());
				} else if ( expect > result.numColors){
					lowTotal += result.numColors;
					failedLow.add(file.toString());
				} else {
					okTotal += result.numColors;
					ok.add(file.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Ok (" + ok.size()+ "/"+ okTotal + "): " + ok);
		System.out.println("TooHigh (" + failedHigh.size() +"/"+ highTotal+ "): " + failedHigh);
		System.out.println("TooLow (" + failedLow.size() +"/"+ lowTotal+ "): " + failedLow);
		assertThat(failedLow.size(), equalTo(0));
		assertThat(failedHigh.size(), equalTo(0));
	}
}
