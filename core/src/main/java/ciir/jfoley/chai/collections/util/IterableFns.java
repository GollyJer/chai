package ciir.jfoley.chai.collections.util;

import ciir.jfoley.chai.collections.TopKHeap;
import ciir.jfoley.chai.collections.iters.*;
import ciir.jfoley.chai.fn.*;
import ciir.jfoley.chai.io.Closer;
import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.lang.Module;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Note that all these functions try to close their inputs if they consume the whole or part.
 * @see ciir.jfoley.chai.io.IO ::close()
 * @author jfoley.
 */
public class IterableFns extends Module {

	/** Provides a lazy map over an iterable, that assumes it is repeatable,
	 *  but that's really up to the underlying implementation. */
	public static <A, B> Iterable<B> map(final Iterable<A> coll, final TransformFn<A,B> mapfn) {
		return () -> new MappingIterator<>(coll.iterator(), mapfn);
	}

	@Nonnull
	public static <A> Iterable<List<A>> sortedStreamingGroupBy(final Iterable<A> coll, final CompareFn<A> cmpFn) {
		return () -> new GroupByIterator<A>(coll.iterator(), cmpFn);
	}
	/** Lazy, functional group-by (assumes sorted) */
	@Deprecated
	public static <A> Iterable<List<A>> groupBy(final Iterable<A> coll, final CompareFn<A> cmpFn) {
		return () -> new GroupByIterator<A>(coll.iterator(), cmpFn);
	}

	/** Lazy, functional group-by (assumes sorted) that compares on equality. */
	public static <A> Iterable<List<A>> groupBy(final Iterable<A> coll) {
		return () -> new GroupByIterator<A>(coll.iterator());
	}

	public static <A> Iterable<A> filter(final Iterable<A> input, final PredicateFn<A> filterFn) {
		return () -> new FilteringIterator<>(input.iterator(), filterFn);
	}

	/** Collect results into the given collection */
	public static <T, Coll extends Collection<T>> Coll collect(Iterable<? extends T> input, Coll builder) {
		try (Closer<?> ignored = Closer.of(input)) {
			for (T t : input) {
				builder.add(t);
			}
		}
		return builder;
	}

	public static <T> void intoSink(Iterator<T> iter, SinkFn<T> output) {
		while(iter.hasNext()) {
			output.process(iter.next());
		}
	}

	public static <T,X extends SinkFn<T>> X intoSink(Iterable<T> input, X sink) {
		try (Closer<Iterable<T>> cc = Closer.of(input)) {
			for (T x : cc.get()) {
				sink.process(x);
			}
		}
		return sink;
	}

	public static <T> List<T> maxK(Iterable<T> items, int k) {
		return maxK(items, k, Comparing.<T>defaultComparator());
	}
	public static <T> List<T> maxK(Iterable<T> items, int k, Comparator<? super T> cmp) {
		return IterableFns.collect(items, TopKHeap.maxItems(k, cmp)).getSorted();
	}
	public static <T> List<T> minK(Iterable<T> items, int k) {
		return minK(items, k, Comparing.<T>defaultComparator());
	}
	public static <T> List<T> minK(Iterable<T> items, int k, Comparator<? super T> cmp) {
		return IterableFns.collect(items, TopKHeap.minItems(k, cmp)).getSorted();
	}

	public static <T extends Comparable> List<T> sorted(Collection<? extends T> input) {
		return sorted(input, Comparing.<T>defaultComparator(), new ArrayList<T>());
	}
	public static <T extends Comparable> List<T> sorted(Collection<? extends T> input, List<T> builder) {
		return sorted(input, Comparing.<T>defaultComparator(), builder);
	}

	public static <T> List<T> sorted(Collection<? extends T> input, Comparator<? super T> cmp) {
		return sorted(input, cmp, new ArrayList<T>());
	}
	public static <T> List<T> sorted(Collection<? extends T> input, Comparator<? super T> cmp, List<T> builder) {
		try (Closer<Collection<? extends T>> cc = Closer.<Collection<? extends T>>of(input)) {
			builder.addAll(cc.get());
		}
		Collections.sort(builder, cmp);
		return builder;
	}

	public static <T> Iterator<T> lazyConcat(final Iterator<T> first, final Iterator<T> second) {
		return new ClosingIterator<T>() {
			public int state = 0;

			@Override
			public void close() throws Exception {
				IO.close(first);
				IO.close(second);
			}

			@Override
			public boolean hasNext() {
				return first.hasNext() || second.hasNext();
			}

			@Override
			public T next() {
				if(first.hasNext()) {
					return first.next();
				}
				if(second.hasNext()) {
					state = 1;
					return second.next();
				}
				throw new NoSuchElementException();
			}

			@Override
			public void remove() {
				if(state == 1) {
					second.remove();
				} else {
					first.remove();
				}
			}
		};
	}

	public static <T> Iterable<T> lazyConcat(Iterable<T> first, Iterable<T> second) {
		return () -> lazyConcat(first.iterator(), second.iterator());
	}

	public static <T> List<T> intoList(Iterable<? extends T> of) {
		return collect(of, new ArrayList<T>());
	}

	public static <K,T> Map<K, List<T>> groupBy(Iterable<T> data, TransformFn<T,K> makeKeyFn) {
		Map<K, List<T>> grouped = new HashMap<>();
		for (T t : data) {
			MapFns.extendListInMap(grouped, makeKeyFn.transform(t), t);
		}
		return grouped;
	}

	public static <T,X> T min(Iterable<T> input, TransformFn<T,X> mapper) {
		return min(input, mapper, Comparing.<X>defaultComparator());
	}
	public static <T,X> T min(Iterable<T> input, TransformFn<T,X> mapper, Comparator<X> cmp) {
		Iterator<T> iter = input.iterator();
		if(!iter.hasNext()) return null;
		T minimum = iter.next();
		X minValue = mapper.transform(minimum);
		while(iter.hasNext()) {
			T candidate = iter.next();
			X cval = mapper.transform(candidate);
			if(cmp.compare(cval, minValue) < 0) {
				minimum = candidate;
				minValue = cval;
			}
		}
		return minimum;
	}

	public static <T> T first(Collection<T> coll) {
		return coll.iterator().next();
	}

	public static <T> Iterable<List<T>> batches(Iterable<T> input, int size) {
		return () -> new BatchedIterator<T>(input.iterator(), size);
	}

	public static <T> Iterable<T> lazyReduce(Iterable<T> input, LazyReduceFn<T> reducer) {
		return () -> new ReducingIterator<>(input.iterator(), reducer);
	}
}
