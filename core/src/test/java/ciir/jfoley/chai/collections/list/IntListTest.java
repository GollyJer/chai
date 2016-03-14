package ciir.jfoley.chai.collections.list;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class IntListTest {

	@Test
	public void testAdd() throws Exception {
		IntList test = new IntList(2);
		test.add(1);
		test.add(2);
		test.add(3);
		test.add(4);
		test.add(5);

		assertEquals(Arrays.asList(1,2,3,4,5), test);
    assertArrayEquals(new int[]{1, 2, 3, 4, 5}, test.asArray());
		assertEquals(8, test.capacity());
	}

	@Test
	public void testAdd2() throws Exception {
		IntList test = new IntList();
		test.add(1);
		test.add(2);
		test.add(3);
		test.add(4);
		test.add(5);

		assertEquals(Arrays.asList(1, 2, 3, 4, 5), test);
		int[] expected = new int[]{1, 2, 3, 4, 5};
		assertArrayEquals(expected, test.asArray());
		for (int i = 0; i < 5; i++) {
			assertEquals(expected[i], test.unsafeArray()[i]);
		}
		assertEquals(1, test.getQuick(0));
		assertEquals(5, test.size());
	}

	@Test
	public void testAddAll() {
		List<Integer> original = Arrays.asList(1,2,3,4,5);
		IntList test = new IntList(original);
		assertEquals(original, test);
	}

	@Test
	public void testEncodeDecode() throws IOException {
		IntList foo = new IntList(Arrays.asList(1,2,3,4,5,6));
		byte[] tmp = foo.encode();
		assertArrayEquals(new byte[] {
				0, 0, 0, 6,
				0, 0, 0, 1,
				0, 0, 0, 2,
				0, 0, 0, 3,
				0, 0, 0, 4,
				0, 0, 0, 5,
				0, 0, 0, 6}, tmp);
		IntList foo2 = IntList.decode(new ByteArrayInputStream(tmp));
		assertEquals(foo, foo2);
	}

	@Test
	public void testEncodeDecodeRand() throws IOException {
		Random rand = new Random();
		for (int N : Arrays.asList(237, 15, 7, 0)){
			IntList original = new IntList(N);
			for (int i = 0; i < N; i++) {
				int nibble = rand.nextInt();
				original.push(nibble);
			}

			byte[] data = original.encode();
			IntList decode = IntList.decode(new ByteArrayInputStream(data));

			assertEquals(decode, original);
			for (int i = 0; i < original.size(); i++) {
				assertEquals(original.getQuick(i), decode.getQuick(i));
			}
		}
	}

	@Test
	public void testRemove() {
		IntList foo = new IntList(Arrays.asList(1,2,3,4,5,6));
		foo.removeInt(2);
		assertEquals(new IntList(Arrays.asList(1,3,4,5,6)), foo);
		foo.removeInt(3);
		assertEquals(new IntList(Arrays.asList(1,4,5,6)), foo);
		foo.removeInt(6);
		assertEquals(new IntList(Arrays.asList(1,4,5)), foo);
		foo.removeInt(1);
		assertEquals(new IntList(Arrays.asList(4,5)), foo);
		foo.removeInt(4);
		assertEquals(new IntList(Arrays.asList(5)), foo);
		foo.removeInt(5);
		assertEquals(new IntList(Arrays.asList()), foo);
	}

}