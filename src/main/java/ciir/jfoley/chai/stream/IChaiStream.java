package ciir.jfoley.chai.stream;

import ciir.jfoley.chai.fn.PredicateFn;
import ciir.jfoley.chai.fn.TransformFn;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author jfoley.
 */
public interface IChaiStream<T> extends Iterable<T> {
	public long UNKNOWN_SIZE = -1;
	/** If known, UNKNOWN_SIZE if not. */
	public long expectedSize();

	public <NT> IChaiStream<NT> map(TransformFn<T, NT> transformFn);

	IChaiStream<T> filter(PredicateFn<T> predicate);

	<K> Map<K, List<T>> groupBy(TransformFn<T, K> keyFn);

	public IChaiStream<T> sorted(Comparator<T> cmp);
	public IChaiStream<T> sorted();

	public List<T> intoList();
	public <Coll extends Collection<T>> Coll collect(Coll builder);
}
