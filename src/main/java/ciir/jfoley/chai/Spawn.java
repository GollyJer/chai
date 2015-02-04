package ciir.jfoley.chai;

import ciir.jfoley.chai.io.Streams;
import ciir.jfoley.chai.string.StrUtil;

import java.io.IOException;
import java.util.Arrays;

/**
 * Interface to Java's lackluster Process API.
 * @author jfoley.
 */
public class Spawn {
	public static void doProcess(String... args) throws IOException, InterruptedException {
		Process exec = Runtime.getRuntime().exec(args);
		int status = exec.waitFor();
		Streams.copy(exec.getErrorStream(), System.err);
		Streams.copy(exec.getInputStream(), System.err);
		System.err.println("# status: " + status + " for: " + StrUtil.join(Arrays.asList(args), " "));
		if(status != 0) throw new RuntimeException();
	}
}
