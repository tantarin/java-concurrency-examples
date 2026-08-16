package io.github.tantarin.concurrency.forkjoin;

import org.junit.jupiter.api.Test;

import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParallelArraySumTest {
    @Test
    void sumsRangeThatDoesNotSplitIntoEqualFinalChunks() {
        long[] values = LongStream.rangeClosed(1, 100_003).toArray();
        long expected = LongStream.of(values).sum();

        assertEquals(expected, ParallelArraySum.sum(values, 1_000));
    }

    @Test
    void sumsEmptyArray() {
        assertEquals(0, ParallelArraySum.sum(new long[0], 100));
    }

    @Test
    void rejectsNonPositiveThreshold() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ParallelArraySum.sum(new long[]{1, 2, 3}, 0)
        );
    }
}
