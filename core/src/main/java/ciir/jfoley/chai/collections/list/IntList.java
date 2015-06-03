package ciir.jfoley.chai.collections.list;

import ciir.jfoley.chai.IntMath;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author jfoley.
 */
public class IntList extends AChaiList<Integer> {
	int fill;
	int size;
	int[] data;

	public IntList() {
		this(16);
	}

	public IntList(int size) {
		this.size = IntMath.nearestPowerOfTwo(size);
		this.data = new int[this.size];
		this.fill = 0;
	}

	public IntList(Collection<Integer> other) {
		this(other.size());
		addAll(other);
	}

	@Override
	public boolean addAll(Collection<? extends Integer> other) {
		reserve(fill+other.size());
		for (Integer value : other) {
			if(value == null) throw new NullPointerException();
			data[fill++] = value;
		}
		return true;
	}

	/**
	 * Resize to exact value.
	 * @param amt the exact number of items this should be able to hold.
	 */
	public void resize(int amt) {
		data = Arrays.copyOf(data, amt);
	}

	/**
	 * Reserve at least amt number of items; uses the nearest power of two
	 * /***
* @param amt
 *
	 * Reserve at least amt number of items; uses the nearest power of two.
	 * @param amt number of items to reserve
	 */
	public void reserve(int amt) {
		if(amt >= size) {
			size = IntMath.nearestPowerOfTwo(amt);
		}
		data = Arrays.copyOf(data, size);
	}

	@Override
	public boolean add(Integer value) {
		if(value == null) throw new NullPointerException();
		reserve(fill+1);
		data[fill++] = value;
		return true;
	}

	@Override
	public void clear() {
		fill = 0;
	}

	@Override
	public Integer get(int index) {
		return data[index];
	}

  @Override
  public Integer set(int index, Integer value) {
    int prev = data[index];
    data[index] = value;
    return prev;
  }

	@Override
	public int size() {
		return fill;
	}

  /** This is toArray, but without the generic problems inherent to Java's toArray */
  public int[] asArray() {
    return Arrays.copyOfRange(data, 0, fill);
  }

  public int[] unsafeArray() {
    return this.data;
  }

  public int getQuick(int key) {
    return data[key];
  }
}
