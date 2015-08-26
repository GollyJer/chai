package ciir.jfoley.chai.random;

import ciir.jfoley.chai.collections.list.AChaiList;
import ciir.jfoley.chai.fn.SinkFn;

import java.util.ArrayList;
import java.util.Random;

/**
 * This collection is built on resevoir sampling.
 *
 * {@see <a href="https://en.wikipedia.org/wiki/Reservoir_sampling#Algorithm_R">Reservoir Sampling / Algorithm R</a>}
 *
 * @author jfoley
 */
public class ReservoirSampler<T> extends AChaiList<T> implements SinkFn<T> {
  Random rand;
  int numSamples;

  ArrayList<T> storage;
  int totalOffered;

  public ReservoirSampler(int numSamples) {
    this(new Random(), numSamples);
  }
  public ReservoirSampler(Random rand, int numSamples) {
    this.rand = rand;
    this.numSamples = numSamples;
    storage = new ArrayList<>(numSamples);
    totalOffered = 0;
  }

  @Override
  public boolean add(T obj) {
    process(obj);
    return true;
  }

  @Override
  public T get(int index) {
    return storage.get(index);
  }

  @Override
  public int size() {
    return storage.size();
  }

  @Override
  public void process(T input) {
    if(totalOffered < numSamples) {
      storage.set(totalOffered++, input);
      return;
    }

    // find a place for it in virtual array that covers all samples at random;
    int position = rand.nextInt(totalOffered);

    // only store it if it fits in the first bit:
    if(position >= numSamples) return;
    storage.set(position, input);
  }
}
