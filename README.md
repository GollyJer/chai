# Chai 
[![Travis Status](https://travis-ci.org/jjfiv/chai.svg?branch=master)](https://travis-ci.org/jjfiv/chai)

*Because I don't like coffee, but I like Java. Mostly.*

## About

The Java standard libraries are amazing. When I pull out a ConcurrentHashMap, I know I'm standing on the shoulders of giants. There are other great libraries like GNU Trove, which add pieces that are missing to the standard libraries (primitive collections) but don't necessarily play nice with the interfaces in the standard JDK.

Another way to put it is that in my work I need the raw performance and I prefer the typing of Java, but I'm really jealous of Clojure's standard library.

Chai is a lightweight, well-tested, opinionated library that builds on some of the really nice interfaces and tools built into the JDK that makes my coding and prototyping a lot easier and simpler.

## Version Support

Supports the now-deprecated Java 7 and Java 8. I have Travis double-checking my commits so that I don't break anything.

## Documentation

This is a tough one. I'm going to figure out how to build and push javadoc somewhere soon, but until then, check out some of the tests for an example of some of the useful static functions provided by Chai.

Until then, some favorites:
```java
// Chai provides wrapper types that allow chaining:
assertEquals(
  Arrays.asList(10, 20, 30),
  ChaiMap.create(
    Pair.of(1, 10),
    Pair.of(2, 20),
    Pair.of(3, 30)
	).readOnly().vals().sorted().intoList());
```

Or if you ever just really need a simple "group-by" operation. Note that this plays nice with existing JVM types, like ``List<T>`` and ``Iterable<T>``.

```java
public static <K,T> Map<K, List<T>> groupBy(Iterable<T> data, TransformFn<T,K> makeKeyFn) {
  Map<K, List<T>> grouped = new HashMap<>();
  for (T t : data) {
    MapFns.extendListInMap(grouped, makeKeyFn.transform(t), t);
  }
  return grouped;
}
```

## History

### v0.4
 - Annotations: ``@Beta``, for classes that aren't ready, ``EmergencyUseOnly``, for methods that ought to be private but aren't.
 - Start using findbugs jsr305 ``@Nullable`` and ``@Nonnull``
 - ``IntList.resize()``
 - ``MapFns.extendCollectionInMap()`` takes a ``GenerateFn<Coll>`` rather than a collection, so that it doesn't wastefully allocate on each call even if the collection is not needed.
 - ``Require.that()``, for when you're not sure assertions are on
 - handle some unicode quotes in ``StrUtil``
 - ``long|Pair<Long,T> Timing.milliseconds(Runnable|GenerateFn<T>)``
 - ``ListFns.slice()``
 - ``ListBasedOrderedSet.toList()``
 - ``ListFns.zip()``
 - ``ListFns.unique()``
 - ``MapFns.fromPairs()``
 - ``ListFnsTest`` catchup
 - Add useful constructor to ``ListBasedOrderedSet``, and fix associated bugs.
 - remove newlines from preview, create a ``MemoryNotifier`` class, ``ThreadsafeLazyPtr``, ``ListFns.maxBy``
 - Fix/Break ``Pair`` comparators to not super ``Map.Entry`` anymore.
 - ``ZipWriter instanceof Flushable``
 - ``QuickSort`` implementation which avoids copying, has wildcarded comparators.
 - ``Builder<T>`` marker interface for things with ``T getOutput()``
 - ``TemporaryDirectory instanceof Directory, GenerateFn<File>``
 - ``IO.openOutputStream(File|String)``
 - ``IOTest``
 - better error message if you try to use a default comparator on something that isn't comparable.
 - ``wrapInputStream`` paving the way for compressed files inside archives, for instance
 - add a quick ``FS.isDirectory`` for when you don't care about the file object afterward.
 - ``IO.resource(String name)``, ``IO.resourceStream(String name)``, and ``IO.slurp(InputStream)``
 - ``StrUtil.indent(text, tabChars)``


### v0.3
 - ``TemporaryDirectory``
 - ``ZipWriter``
 - ``IntMath::fromLong``
 - ``Closer<T> implements GenerateFn<T> { T get(); }``
 - ``Comparing.byteArrays()``
 - ``ArrayFns.compare(byte[], byte[])``
 - Scattered Javadocs and Tests

### v0.2
 - ``StreamFns``
    - ``readBytes``
    - ``fromByteBuffer``
    - ``readAll``
 - More reader factories on ``IO``
 - ``DoubleFns.equals(a,b,epsilon)``
 - ``ZipArchive, ZipArchiveEntry``
 - ``LazyPtr<T>``
 - ``ListFns.partition`` and ``ListFns.roundRobinPartition``
 - ``MapFns.getOrElse``
 - indenting, tests, javadoc

### v0.1
 - XML as a separate module
 - ``TreeFns``
 - ``AChaiList`` is a superset of ``AbstractList`` functionality
 - ``ChaiIterable`` isa ``Collection``
 - ``TopKHeap`` has ``addAll`` and ``bool offer()``
 - Move to github and setup travis
 - ... implement and pull in from other files.

