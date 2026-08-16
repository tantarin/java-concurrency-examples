package io.github.tantarin.concurrency.forkjoin;

import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public final class ParallelArraySum {
    private ParallelArraySum() {
    }

    public static long sum(long[] values, int threshold) {
        Objects.requireNonNull(values, "values");
        if (threshold <= 0) {
            throw new IllegalArgumentException("threshold must be positive");
        }

        ForkJoinPool pool = new ForkJoinPool();
        try {
            return pool.invoke(new SumTask(values, 0, values.length, threshold));
        } finally {
            pool.shutdown();
        }
    }

    static final class SumTask extends RecursiveTask<Long> {
        private final long[] values;
        private final int from;
        private final int to;
        private final int threshold;

        SumTask(long[] values, int from, int to, int threshold) {
            this.values = values;
            this.from = from;
            this.to = to;
            this.threshold = threshold;
        }

        @Override
        protected Long compute() {
            if (to - from <= threshold) {
                return sumSequentially();
            }

            int middle = from + (to - from) / 2;
            SumTask left = new SumTask(values, from, middle, threshold);
            SumTask right = new SumTask(values, middle, to, threshold);

            left.fork();
            long rightResult = right.compute();
            return left.join() + rightResult;
        }

        private long sumSequentially() {
            long result = 0;
            for (int index = from; index < to; index++) {
                result += values[index];
            }
            return result;
        }
    }
}
