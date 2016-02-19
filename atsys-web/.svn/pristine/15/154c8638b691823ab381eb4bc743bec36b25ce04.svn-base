package org.aquare.test;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkTests {

    private static final int[] array = new int[4 * 1024 * 1024];

    @Benchmark
    public List<Integer> iterateNormalFor() {
        final List<Integer> arrayCopy = new ArrayList<>(array.length);
        for (int i : array) {
            arrayCopy.add(i);
        }
        return arrayCopy;
    }

    @Benchmark
    public List<Integer> iterateForEach() {
        final List<Integer> arrayCopy = new ArrayList<>(array.length);
        IntStream.of(array).forEach(arrayCopy::add);
        return arrayCopy;
    }

    @Benchmark
    public List<Integer> iterateForEachDynamicList() {
        final List<Integer> arrayCopy = new ArrayList<>();
        IntStream.of(array).forEach(arrayCopy::add);
        return arrayCopy;
    }

    @Benchmark
    public List<Integer> iterateStream() {
        return IntStream.of(array).boxed().collect(Collectors.toList());
    }

    @Benchmark
    public List<Integer> iterateStreamFixed() {
        return IntStream.of(array).boxed().collect(toList(array.length));
    }

    public static <T> Collector<T, ?, List<T>> toList(int size) {
        return new CollectorImpl<>(() -> new ArrayList<T>(size), List::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                CH_ID);
    }

    static final Set<Collector.Characteristics> CH_CONCURRENT_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.CONCURRENT,
            Collector.Characteristics.UNORDERED,
            Collector.Characteristics.IDENTITY_FINISH));
    static final Set<Collector.Characteristics> CH_CONCURRENT_NOID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.CONCURRENT,
            Collector.Characteristics.UNORDERED));
    static final Set<Collector.Characteristics> CH_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
    static final Set<Collector.Characteristics> CH_UNORDERED_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED,
            Collector.Characteristics.IDENTITY_FINISH));
    static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

    static class CollectorImpl<T, A, R> implements Collector<T, A, R> {

        private final Supplier<A> supplier;

        private final BiConsumer<A, T> accumulator;

        private final BinaryOperator<A> combiner;

        private final Function<A, R> finisher;

        private final Set<Characteristics> characteristics;

        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Function<A, R> finisher,
                      Set<Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }

        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }

        private static <I, R> Function<I, R> castingIdentity() {
            return i -> (R) i;
        }
    }

    public static void main(String... args) throws Exception {

        Locale.setDefault(Locale.ENGLISH);

        new Runner(new OptionsBuilder()
                .include(".*")
                .warmupIterations(5)
                .measurementIterations(5)

//                Benchmark Mode Cnt Score Error Units
//                BenchmarkTests.iterateForEach    thrpt    5  10,814 ñ 0,419  ops/s
//                BenchmarkTests.iterateNormalFor  thrpt    5   6,861 ñ 0,863  ops/s
//                BenchmarkTests.iterateStream     thrpt    5   2,965 ñ 0,145  ops/s
                        //.jvmArgs("-server", "-XX:+UseG1GC", "-XX:InitiatingHeapOccupancyPercent=80")

//                Benchmark                         Mode  Cnt   Score   Error  Units
//                BenchmarkTests.iterateForEach    thrpt    5  18,667 ñ 3,935  ops/s
//                BenchmarkTests.iterateNormalFor  thrpt    5  29,442ñ 1,835  ops/s
//                BenchmarkTests.iterateStream     thrpt    5   9,802 ñ 0,933  ops/s
                .jvmArgs("-server", "-Xms512m", "-Xmx512m", "-XX:MaxGCPauseMillis=100")

                        //.jvmArgs("-server", "-Xloggc:gc.log", "-XX:+PrintGCDetails")

                .forks(1)
                .mode(Mode.AverageTime)
                .build()).run();
    }
}
