package io.github.tantarin.concurrency.downloads;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DownloadRegistryTest {
    @Test
    void startsOneDownloadWhenManyThreadsRequestTheSameId() throws Exception {
        int threadCount = 32;
        DownloadRegistry registry = new DownloadRegistry();
        AtomicInteger starts = new AtomicInteger();
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<Download>> results = new ArrayList<>();

        try {
            for (int i = 0; i < threadCount; i++) {
                results.add(executor.submit(() -> {
                    ready.countDown();
                    start.await();
                    return registry.findOrStart("book", id -> {
                        starts.incrementAndGet();
                        return new Download(id, URI.create("https://example.com/book.pdf"));
                    });
                }));
            }

            assertTrue(ready.await(2, TimeUnit.SECONDS));
            start.countDown();

            Download first = results.get(0).get(2, TimeUnit.SECONDS);
            for (Future<Download> result : results) {
                assertSame(first, result.get(2, TimeUnit.SECONDS));
            }

            assertEquals(1, starts.get());
            assertEquals(1, registry.size());
            assertSame(first, registry.find("book"));
        } finally {
            start.countDown();
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(2, TimeUnit.SECONDS));
        }
    }
}
