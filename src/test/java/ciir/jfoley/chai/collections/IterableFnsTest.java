package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.collections.util.IterableFns;
import ciir.jfoley.chai.collections.util.ListFns;
import ciir.jfoley.chai.fn.LazyReduceFn;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IterableFnsTest {

	@Test
	public void testSorted() {
		List<Integer> data = Arrays.asList(4,2,1,3,5);
		assertEquals(Arrays.asList(1, 2, 3, 4, 5),
			IterableFns.sorted(data));
		assertEquals(Arrays.asList(5,4,3,2,1),
			IterableFns.sorted(data, Collections.<Integer>reverseOrder()));
	}

	@Test
	public void testConcat() {
		assertEquals(
			Arrays.asList(1,2,3,4,5,6),
			IterableFns.intoList(IterableFns.lazyConcat(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6))));
		// Test list version:
		List<Integer> lazilyMerged = ListFns.lazyConcat(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
		assertEquals(Arrays.asList(1,2,3,4,5,6), lazilyMerged);
	}


  @Test
  public void testHeapStuff() {
    List<Integer> data = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    assertEquals(Arrays.asList(10,9,8), IterableFns.maxK(data, 3));
    assertEquals(Arrays.asList(1,2,3), IterableFns.minK(data, 3));
  }

  @Test
  public void testChunking() {
    assertEquals(Arrays.asList(
        Arrays.asList(1,2),
        Arrays.asList(3,4),
        Arrays.asList(5)
    ), IterableFns.intoList(IterableFns.batches(IntRange.inclusive(1, 5), 2)));
    assertEquals(Arrays.asList(
        Arrays.asList(1, 2, 3),
        Arrays.asList(4, 5)
    ), IterableFns.intoList(IterableFns.batches(IntRange.inclusive(1, 5), 3)));
  }

  @Test
  public void testLazyReduce() {
    List<Integer> data = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
    LazyReduceFn<Integer> reducefn = new LazyReduceFn<Integer>() {
      @Override
      public boolean shouldReduce(Integer lhs, Integer rhs) {
        return lhs < rhs;
      }

      @Override
      public Integer reduce(Integer lhs, Integer rhs) {
        return lhs+rhs;
      }
    };

    // 1+2, 3+4, 5+6, 7+8, 9+10
    assertEquals(Arrays.asList(3,7,11,15,19), IterableFns.intoList(IterableFns.lazyReduce(data, reducefn)));
  }
}