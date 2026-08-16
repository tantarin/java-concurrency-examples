package io.github.tantarin.concurrency.liveness;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.CountDownLatch;

public final class DeadlockDemo {
    private DeadlockDemo() {
    }

    public static void main(String[] args) throws InterruptedException {
        Object lockA = new Object();
        Object lockB = new Object();
        CountDownLatch bothOwnFirstLock = new CountDownLatch(2);

        Thread first = deadlockingThread("A-then-B", lockA, lockB, bothOwnFirstLock);
        Thread second = deadlockingThread("B-then-A", lockB, lockA, bothOwnFirstLock);
        first.start();
        second.start();

        bothOwnFirstLock.await();
        Thread.sleep(100);
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        long[] deadlocked = threads.findDeadlockedThreads();
        System.out.println("Deadlocked threads: " + (deadlocked == null ? 0 : deadlocked.length));
    }

    private static Thread deadlockingThread(
        String name,
        Object first,
        Object second,
        CountDownLatch bothOwnFirstLock
    ) {
        Thread thread = new Thread(() -> {
            synchronized (first) {
                bothOwnFirstLock.countDown();
                await(bothOwnFirstLock);
                synchronized (second) {
                    throw new AssertionError("unreachable");
                }
            }
        }, name);
        thread.setDaemon(true);
        return thread;
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
        }
    }
}
