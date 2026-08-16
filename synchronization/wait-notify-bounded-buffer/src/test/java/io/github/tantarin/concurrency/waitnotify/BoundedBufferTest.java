package io.github.tantarin.concurrency.waitnotify;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BoundedBufferTest {
    @Test
    void consumerWaitsWhileBufferIsEmpty() throws Exception {
        BoundedBuffer<String> buffer = new BoundedBuffer<>(1);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<String> taken = executor.submit(buffer::take);

            assertThrows(TimeoutException.class, () -> taken.get(50, TimeUnit.MILLISECONDS));
            buffer.put("lesson");

            assertEquals("lesson", taken.get(1, TimeUnit.SECONDS));
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void producerWaitsWhileBufferIsFull() throws Exception {
        BoundedBuffer<String> buffer = new BoundedBuffer<>(1);
        buffer.put("first");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> secondPut = executor.submit(() -> {
                buffer.put("second");
                return null;
            });

            assertThrows(TimeoutException.class, () -> secondPut.get(50, TimeUnit.MILLISECONDS));
            assertEquals("first", buffer.take());
            secondPut.get(1, TimeUnit.SECONDS);
            assertEquals("second", buffer.take());
        } finally {
            executor.shutdownNow();
        }
    }
}
