package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.fn.TransformFn;

import java.util.*;

/**
 * @author jfoley.
 */
public class MapFns {
	public static <K,V> V firstValue(Map<K,V> input) {
		if(input.isEmpty()) return null;
		return input.values().iterator().next();
	}

	public static <K,V> K firstKey(Map<K,V> input) {
		if(input.isEmpty()) return null;
		return input.keySet().iterator().next();
	}

	@SuppressWarnings("unchecked")
	public static <A,B> Map<A,B> cast(Map input) {
		return (Map<A,B>) input;
	}

	public static <K,T, Coll extends Collection<? super T>> void extendCollectionInMap(Map<? super K,Coll> inMap, K key, T value, Coll builder) {
		Coll existing = inMap.get(key);
		if(existing == null) {
			existing = builder;
			inMap.put(key, existing);
		}
		existing.add(value);
	}
	public static <K,T> void extendListInMap(Map<K, List<T>> inMap, K key, T value) {
		extendCollectionInMap(inMap, key, value, new ArrayList<T>());
	}
	public static <K,T> void extendSetInMap(Map<K, Set<T>> inMap, K key, T value) {
		extendCollectionInMap(inMap, key, value, new HashSet<T>());
	}

	public static <K,V,VN> Map<K,VN> mapValues(Map<K, V> initial, TransformFn<V,VN> xfn) {
		return mapValues(initial, xfn, new HashMap<K, VN>(initial.size()));
	}

	public static <K,V,VN> Map<K,VN> mapValues(Map<K, V> initial, TransformFn<V,VN> xfn, Map<K,VN> builder) {
		for (Map.Entry<K, V> kv : initial.entrySet()) {
			builder.put(kv.getKey(), xfn.transform(kv.getValue()));
		}
		return builder;
	}

	public static <K, KN, V> Map<KN, V> mapKeys(Map<K, V> initial, TransformFn<K, KN> keyXFn) {
		return mapKeys(initial, keyXFn, new HashMap<KN, V>(initial.size()));
	}

	public static <K, KN, V> Map<KN, V> mapKeys(Map<K, V> initial, TransformFn<K, KN> keyXFn, Map<KN, V> builder) {
		for (Map.Entry<K, V> kv : initial.entrySet()) {
			builder.put(keyXFn.transform(kv.getKey()), kv.getValue());
		}
		return builder;
	}

	public static <K,V> Iterable<Pair<K,V>> pairs(Map<K,V> input) {
		return IterableFns.map(input.entrySet(), new TransformFn<Map.Entry<K,V>, Pair<K,V>>() {
			@Override
			public Pair<K, V> transform(Map.Entry<K, V> input) {
				return Pair.of(input);
			}
		});
	}

	public static <K,V> Map<V,K> invert(Map<K,V> input) {
		return invert(input, new HashMap<V, K>(input.size()));
	}
	public static <K,V> Map<V,K> invert(Map<K,V> input, Map<V,K> builder) {
		for (Map.Entry<K, V> kv : input.entrySet()) {
			builder.put(kv.getValue(), kv.getKey());
		}
		return builder;
	}

	public static <V> Map<Integer, V> ofListIndex(List<V> input) {
		return ofListIndex(input, new HashMap<Integer, V>(input.size()));
	}
	/** Todo, make this more efficient */
	public static <V> Map<Integer, V> ofListIndex(List<V> input, Map<Integer, V> builder) {
		for (int i = 0; i < input.size(); i++) {
			builder.put(i, input.get(i));
		}
		return builder;
	}

}
