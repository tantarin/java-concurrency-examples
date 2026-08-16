package io.github.tantarin.concurrency.tasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

public final class FileSizeTasks {
    private FileSizeTasks() {
    }

    public static Runnable rememberSize(Path file, AtomicLong destination) {
        return () -> {
            try {
                destination.set(Files.size(file));
            } catch (IOException error) {
                throw new IllegalStateException("Cannot read " + file, error);
            }
        };
    }

    public static Callable<Long> calculateSize(Path file) {
        return () -> Files.size(file);
    }
}
