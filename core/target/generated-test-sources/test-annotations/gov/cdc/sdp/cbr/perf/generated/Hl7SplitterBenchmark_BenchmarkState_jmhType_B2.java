package gov.cdc.sdp.cbr.perf.generated;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
public class Hl7SplitterBenchmark_BenchmarkState_jmhType_B2 extends Hl7SplitterBenchmark_BenchmarkState_jmhType_B1 {
    public volatile int setupTrialMutex;
    public volatile int tearTrialMutex;
    public final static AtomicIntegerFieldUpdater<Hl7SplitterBenchmark_BenchmarkState_jmhType_B2> setupTrialMutexUpdater = AtomicIntegerFieldUpdater.newUpdater(Hl7SplitterBenchmark_BenchmarkState_jmhType_B2.class, "setupTrialMutex");
    public final static AtomicIntegerFieldUpdater<Hl7SplitterBenchmark_BenchmarkState_jmhType_B2> tearTrialMutexUpdater = AtomicIntegerFieldUpdater.newUpdater(Hl7SplitterBenchmark_BenchmarkState_jmhType_B2.class, "tearTrialMutex");

    public volatile int setupIterationMutex;
    public volatile int tearIterationMutex;
    public final static AtomicIntegerFieldUpdater<Hl7SplitterBenchmark_BenchmarkState_jmhType_B2> setupIterationMutexUpdater = AtomicIntegerFieldUpdater.newUpdater(Hl7SplitterBenchmark_BenchmarkState_jmhType_B2.class, "setupIterationMutex");
    public final static AtomicIntegerFieldUpdater<Hl7SplitterBenchmark_BenchmarkState_jmhType_B2> tearIterationMutexUpdater = AtomicIntegerFieldUpdater.newUpdater(Hl7SplitterBenchmark_BenchmarkState_jmhType_B2.class, "tearIterationMutex");

    public volatile int setupInvocationMutex;
    public volatile int tearInvocationMutex;
    public final static AtomicIntegerFieldUpdater<Hl7SplitterBenchmark_BenchmarkState_jmhType_B2> setupInvocationMutexUpdater = AtomicIntegerFieldUpdater.newUpdater(Hl7SplitterBenchmark_BenchmarkState_jmhType_B2.class, "setupInvocationMutex");
    public final static AtomicIntegerFieldUpdater<Hl7SplitterBenchmark_BenchmarkState_jmhType_B2> tearInvocationMutexUpdater = AtomicIntegerFieldUpdater.newUpdater(Hl7SplitterBenchmark_BenchmarkState_jmhType_B2.class, "tearInvocationMutex");

    public volatile boolean readyTrial;
    public volatile boolean readyIteration;
    public volatile boolean readyInvocation;
}
