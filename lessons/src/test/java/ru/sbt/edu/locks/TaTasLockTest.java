package ru.sbt.edu.locks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import ru.sbt.edu.counter.Counter;
import ru.sbt.edu.counter.LockCounter;
import ru.sbt.edu.locks.tas.TaTasLock;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@Fork(1)
public class TaTasLockTest {
    @State(Scope.Benchmark)
    public static class CounterState {
        final SLock lock = new TaTasLock();
        final Counter counter = new LockCounter(lock);
    }

    @Benchmark
    @Group("ConcurentCounter")
    @GroupThreads(8)
    public void increments(final CounterState state) {
        state.counter.increment();
    }

//    @Benchmark
//    @Group("ConcurentCounter")
//    @GroupThreads(8)
//    public long gets(final CounterState state) {
//        return state.counter.value();
//    }

    public static void main(String[] args) throws Exception {
        final Options options = new OptionsBuilder()
                .include(TaTasLockTest.class.getName())
                .forks(1)
                .threadGroups(1,2,4,8)
                .build();

        new Runner(options).run();
    }
}