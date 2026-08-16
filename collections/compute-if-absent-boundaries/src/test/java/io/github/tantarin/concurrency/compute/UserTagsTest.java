package io.github.tantarin.concurrency.compute;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTagsTest {
    @Test
    void concurrentValueRemainsSafeAfterComputeIfAbsentReturns() throws Exception {
        UserTags tags = new UserTags();

        runConcurrently(100, index -> tags.addToConcurrentSet("user-1", "tag-" + index));

        assertEquals(100, tags.concurrentTags("user-1").size());
    }

    @Test
    void computeCanReplaceValueWithImmutableSnapshot() throws Exception {
        UserTags tags = new UserTags();

        runConcurrently(100, index -> tags.addWithAtomicReplacement("user-1", "tag-" + index));

        assertEquals(100, tags.immutableSnapshot("user-1").size());
    }

    @Test
    void mappingFunctionRunsOnceForOneStableKey() throws Exception {
        java.util.concurrent.ConcurrentHashMap<String, String> map = new java.util.concurrent.ConcurrentHashMap<>();
        AtomicInteger calls = new AtomicInteger();

        runConcurrently(100, index -> map.computeIfAbsent("lesson", ignored -> {
            calls.incrementAndGet();
            return "ready";
        }));

        assertEquals(1, calls.get());
    }

    private static void runConcurrently(int count, IndexedTask task) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(8);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<?>> futures = new ArrayList<>();
        try {
            for (int index = 0; index < count; index++) {
                int capturedIndex = index;
                futures.add(executor.submit(() -> {
                    start.await();
                    task.run(capturedIndex);
                    return null;
                }));
            }
            start.countDown();
            for (Future<?> future : futures) {
                future.get(1, TimeUnit.SECONDS);
            }
        } finally {
            executor.shutdownNow();
        }
    }

    private interface IndexedTask {
        void run(int index);
    }
}
