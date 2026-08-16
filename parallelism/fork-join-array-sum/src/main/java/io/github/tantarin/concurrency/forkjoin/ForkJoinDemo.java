package io.github.tantarin.concurrency.forkjoin;

import java.util.stream.LongStream;

public final class ForkJoinDemo {
    private ForkJoinDemo() {
    }

    public static void main(String[] args) {
        long[] values = LongStream.rangeClosed(1, 10_000_000).toArray();
        long result = ParallelArraySum.sum(values, 50_000);
        System.out.println("sum = " + result);
    }
}
