package io.github.tantarin.concurrency.liveness;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LivenessTest {
    @Test
    void symmetricRetriesStayActiveWithoutProgress() throws Exception {
        LivelockDemo.Result result = LivelockDemo.runSymmetricRetries(100);

        assertEquals(200, result.attempts);
        assertEquals(0, result.completed);
    }

    @Test
    void globalLockOrderAllowsOppositeTransfersToFinish() throws Exception {
        OrderedTransferService service = new OrderedTransferService();
        OrderedTransferService.Account first = new OrderedTransferService.Account(1, 1_000);
        OrderedTransferService.Account second = new OrderedTransferService.Account(2, 1_000);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Future<?> forward = executor.submit(() -> service.transfer(first, second, 100));
            Future<?> backward = executor.submit(() -> service.transfer(second, first, 100));
            forward.get(1, TimeUnit.SECONDS);
            backward.get(1, TimeUnit.SECONDS);

            assertEquals(2_000, first.balance() + second.balance());
        } finally {
            executor.shutdownNow();
        }
    }
}
