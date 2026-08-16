package io.github.tantarin.concurrency.liveness;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public final class LivelockDemo {
    private LivelockDemo() {
    }

    public static Result runSymmetricRetries(int rounds) throws InterruptedException {
        ReentrantLock lockA = new ReentrantLock();
        ReentrantLock lockB = new ReentrantLock();
        CyclicBarrier bothHoldFirstLock = new CyclicBarrier(2);
        CyclicBarrier bothTriedSecondLock = new CyclicBarrier(2);
        CyclicBarrier bothReleasedFirstLock = new CyclicBarrier(2);
        AtomicInteger attempts = new AtomicInteger();
        AtomicInteger completed = new AtomicInteger();

        Thread first = worker(lockA, lockB, rounds, bothHoldFirstLock, bothTriedSecondLock, bothReleasedFirstLock, attempts, completed);
        Thread second = worker(lockB, lockA, rounds, bothHoldFirstLock, bothTriedSecondLock, bothReleasedFirstLock, attempts, completed);
        first.start();
        second.start();
        first.join();
        second.join();
        return new Result(attempts.get(), completed.get());
    }

    private static Thread worker(
        ReentrantLock first,
        ReentrantLock second,
        int rounds,
        CyclicBarrier bothHoldFirstLock,
        CyclicBarrier bothTriedSecondLock,
        CyclicBarrier bothReleasedFirstLock,
        AtomicInteger attempts,
        AtomicInteger completed
    ) {
        return new Thread(() -> {
            for (int round = 0; round < rounds; round++) {
                first.lock();
                await(bothHoldFirstLock);
                attempts.incrementAndGet();
                if (second.tryLock()) {
                    try {
                        completed.incrementAndGet();
                    } finally {
                        second.unlock();
                    }
                }
                await(bothTriedSecondLock);
                first.unlock();
                await(bothReleasedFirstLock);
            }
        });
    }

    private static void await(CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(error);
        } catch (BrokenBarrierException error) {
            throw new IllegalStateException(error);
        }
    }

    public static final class Result {
        public final int attempts;
        public final int completed;

        Result(int attempts, int completed) {
            this.attempts = attempts;
            this.completed = completed;
        }
    }
}
