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
	public void testOthers(){
		Path path = Paths.get("resources", "colouring");
		List<String> failed = new ArrayList<>();
		List<String> ok = new ArrayList<>();
		try(DirectoryStream<Path> ds = Files.newDirectoryStream(path)){
			for (Path file : ds) {
				Result result = c.calculate(file.toString());
				//System.out.println(file.toString());
				//System.out.println(result);
				Path basename = file.getName(file.getNameCount()-1);
				String expectStr = basename.toString().split("_")[2];
				if(Integer.parseInt(expectStr) != result.numColors){
					failed.add(file.toString());
				} else {
					ok.add(file.toString());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("Ok: " + ok);
		System.out.println("Failed: " + failed);
		assertThat(failed.size(), equalTo(0));
	}
}
