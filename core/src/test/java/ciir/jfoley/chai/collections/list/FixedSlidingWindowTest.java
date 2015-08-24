package ciir.jfoley.chai.collections.list;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class FixedSlidingWindowTest {

  @Test
  public void testSimple() {
    FixedSlidingWindow<Integer> data = new FixedSlidingWindow<>(5);
    data.add(1);
    data.add(2);

    assertEquals(Arrays.asList(1, 2), data);
    data.add(3);
    data.add(4);
    data.add(5);
    assertEquals(Arrays.asList(1, 2, 3, 4, 5), data);

    data.add(6);
    assertEquals(Arrays.asList(6, 2, 3, 4, 5), data.data);
    assertEquals(Arrays.asList(2, 3, 4, 5, 6), data);

    assertEquals(2, data.replace(7).intValue());
    assertEquals(Arrays.asList(3, 4, 5, 6, 7), data);
  }
}