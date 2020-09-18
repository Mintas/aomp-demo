package ru.sbt.edu.locks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import ru.sbt.edu.counter.Counter;
import ru.sbt.edu.counter.LockCounter;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@Fork(1)
public class TasLockTest {
    @State(Scope.Benchmark)
    public static class CounterState {
        final SLock lock = new TasLock();
        final Counter counter = new LockCounter(lock);
    }

    @Benchmark
    @Group("ConcurentCounter")
    @GroupThreads(8)
    public void increments(final CounterState state) {
        state.counter.increment();
    }

    @Benchmark
    @Group("ConcurentCounter")
    @GroupThreads(8)
    public long gets(final CounterState state) {
        return state.counter.value();
    }

    public static void main(String[] args) throws Exception {
        final Options options = new OptionsBuilder()
                .include(TasLockTest.class.getName())
                .forks(1)
                .build();

        new Runner(options).run();
    }
}