package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.collections.list.AChaiList;
import ciir.jfoley.chai.lang.annotations.Beta;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Constant memory consecutive integer ranges.
 * @author jfoley
 */
public class IntRange extends AChaiList<Integer> implements List<Integer> {
  private final int start;
  private final int size;

  public IntRange(int start, int size) {
    this.start = start;
    this.size = size;
  }

  public int inclusiveEnd() {
    return start + size - 1;
  }
  public int exclusiveEnd() {
    return start + size;
  }

  @Override
  public boolean contains(Object x) {
    return x instanceof Integer && containsInt((Integer) x);
  }

  public boolean containsInt(int val) {
    return val >= start && val < exclusiveEnd();
  }

  @Override
  public Integer get(int index) {
    if(index < 0 || index >= size) throw new NoSuchElementException();
    return start + index;
  }

  public List<Integer> asList() {
    return this;
  }

  @Override
  public int size() {
    return size;
  }

  public static IntRange inclusive(int begin, int end) {
    return new IntRange(begin, end-begin+1);
  }
  public static IntRange exclusive(int begin, int end) {
    return new IntRange(begin, end-begin);
  }

  @Beta
  public boolean intersects(IntRange originalTag) {
    int al = this.start;
    int bl = originalTag.start;
    int ar = this.inclusiveEnd();
    int br = originalTag.inclusiveEnd();
    return ar >= bl && br >= al;
  }
}
