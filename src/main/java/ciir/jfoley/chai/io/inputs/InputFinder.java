package ciir.jfoley.chai.io.inputs;

import ciir.jfoley.chai.fn.SinkFn;
import ciir.jfoley.chai.io.Directory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * @author jfoley
 */
public class InputFinder {
  public static final Logger logger = Logger.getLogger(InputFinder.class.getName());
  public final List<FileHandler> handlers;

  public static List<FileHandler> defaultHandlers = new Vector<>();
  static {
    defaultHandlers.add(new ZipInputHandler());
    defaultHandlers.add(new TarInputHandler());
  }

  public static InputFinder Default() {
    return new InputFinder(defaultHandlers);
  }

  public InputFinder(List<FileHandler> handlers) {
    this.handlers = handlers;
  }

  public interface FileHandler {
    boolean matches(File input);
    InputContainer getContainer(File input) throws IOException;
  }

  public List<? extends InputContainer> findAllInputs(List<String> paths) throws IOException {
    ArrayList<InputContainer> output = new ArrayList<>();
    for (String path : paths) {
      output.addAll(findAllInputs(path));
    }
    return output;
  }

  public List<? extends InputContainer> findAllInputs(String path) throws IOException {
    File fp = new File(path);
    if(fp.isDirectory()) {
      return findAllInputs(Directory.Read(path));
    } else {
      return Collections.singletonList(asInputContainer(fp));
    }
  }

  public List<? extends InputContainer> findAllInputs(Directory dir) throws IOException {
    ArrayList<InputContainer> output = new ArrayList<>();
    for (File child : dir.recursiveChildren()) {
      output.add(asInputContainer(child));
    }
    return output;
  }

  public void forEachPath(List<String> inputs, SinkFn<InputStreamable> output) throws IOException {
    forEach(findAllInputs(inputs), output);
  }
  public void forEach(List<? extends InputContainer> inputs, SinkFn<InputStreamable> output) throws IOException {
    for (InputContainer inputContainer : inputs) {
      for (InputStreamable inputStreamable : inputContainer.getInputs()) {
        output.process(inputStreamable);
      }
    }
  }

  public InputContainer asInputContainer(File child) throws IOException {
    for (FileHandler handler : this.handlers) {
      if(handler.matches(child)) {
        return handler.getContainer(child);
      }
    }
    return new SingletonInputContainer(child.getName(), new FileInput(child));
  }

}
