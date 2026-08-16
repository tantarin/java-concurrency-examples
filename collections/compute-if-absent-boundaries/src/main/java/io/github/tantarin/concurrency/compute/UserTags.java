package io.github.tantarin.concurrency.compute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class UserTags {
    private final ConcurrentHashMap<String, Set<String>> concurrentTags = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<String>> immutableSnapshots = new ConcurrentHashMap<>();

    public void addToConcurrentSet(String userId, String tag) {
        concurrentTags
            .computeIfAbsent(userId, ignored -> ConcurrentHashMap.newKeySet())
            .add(tag);
    }

    public Set<String> concurrentTags(String userId) {
        return concurrentTags.getOrDefault(userId, Collections.emptySet());
    }

    public void addWithAtomicReplacement(String userId, String tag) {
        immutableSnapshots.compute(userId, (ignored, current) -> {
            List<String> updated = current == null
                ? new ArrayList<>()
                : new ArrayList<>(current);
            updated.add(tag);
            return Collections.unmodifiableList(updated);
        });
    }

    public List<String> immutableSnapshot(String userId) {
        return immutableSnapshots.getOrDefault(userId, Collections.emptyList());
    }
}
