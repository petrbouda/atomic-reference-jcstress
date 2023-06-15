package org.example;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

import java.util.concurrent.atomic.AtomicReference;

import static org.openjdk.jcstress.annotations.Expect.*;

public class AtomicReferenceOrdering {

    @JCStressTest
    @Outcome(id = "0, 0", expect = ACCEPTABLE)
    @Outcome(id = "1, 1", expect = ACCEPTABLE)
    @Outcome(id = "1, 0", expect = ACCEPTABLE_INTERESTING)
    @Outcome(id = "0, 1", expect = ACCEPTABLE)
    @State
    public static class NotGuardedByAtomic {

        AtomicReference<Integer> atomic_int = new AtomicReference<>(0);
        int plain_int;

        @Actor
        public void actor1() {
            plain_int = 1;
            atomic_int.compareAndSet(0, 1);
        }

        @Actor
        public void actor2(II_Result r) {
            r.r1 = atomic_int.get();
            r.r2 = plain_int;
        }
    }

    @JCStressTest
    @Outcome(id = "0, 0", expect = ACCEPTABLE)
    @Outcome(id = "1, 1", expect = ACCEPTABLE)
    @Outcome(id = "1, 0", expect = FORBIDDEN)
    @Outcome(id = "0, 1", expect = ACCEPTABLE)
    @State
    public static class GuardedByAtomic {

        AtomicReference<Integer> atomic_int = new AtomicReference<>(0);
        int plain_int;

        @Actor
        public void actor1() {
            plain_int = 1;
            atomic_int.set(1);
        }

        @Actor
        public void actor2(II_Result r) {
            r.r1 = atomic_int.get();
            r.r2 = plain_int;
        }
    }
}
