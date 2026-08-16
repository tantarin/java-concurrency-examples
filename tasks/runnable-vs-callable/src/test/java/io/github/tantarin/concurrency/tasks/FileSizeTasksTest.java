package io.github.tantarin.concurrency.tasks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileSizeTasksTest {
    @Test
    void callableReturnsValueThroughFuture(@TempDir Path directory) throws Exception {
        Path file = Files.write(directory.resolve("data.txt"), new byte[]{1, 2, 3});
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Long> size = executor.submit(FileSizeTasks.calculateSize(file));
            assertEquals(3L, size.get());
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void runnableCommunicatesThroughSharedState(@TempDir Path directory) throws Exception {
        Path file = Files.write(directory.resolve("data.txt"), new byte[]{1, 2, 3});
        AtomicLong destination = new AtomicLong();

        FileSizeTasks.rememberSize(file, destination).run();

        assertEquals(3L, destination.get());
    }

    @Test
    void callableCheckedExceptionAppearsAsExecutionException(@TempDir Path directory) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Long> size = executor.submit(
                FileSizeTasks.calculateSize(directory.resolve("missing.txt"))
            );
            assertThrows(ExecutionException.class, size::get);
        } finally {
            executor.shutdownNow();
        }
    }
}
