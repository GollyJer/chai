package ciir.jfoley.chai.collections;

import javax.sound.sampled.Line;
import java.util.AbstractList;
import java.util.NoSuchElementException;

/**
 * @author jfoley
 */
public class LinearSpace extends AbstractList<Double> {
  public final double start;
  public final double end;
  public final int numPoints;
  private final double step;

  private LinearSpace(double start, double end, int numPoints) {
    this.start = start;
    this.end = end;
    this.numPoints = numPoints;
    this.step = (end - start) / ((double) numPoints);
  }

  @Override
  public Double get(int index) {
    if(index < 0 || index >= numPoints) throw new NoSuchElementException();
    return start + step * index;
  }

  @Override
  public int size() {
    return numPoints;
  }

  public static LinearSpace of(double start, double end, int numPoints) {
    return new LinearSpace(start, end, numPoints);
  }
  public static LinearSpace of(double start, double end) {
    return new LinearSpace(start, end, 100);
  }
}
