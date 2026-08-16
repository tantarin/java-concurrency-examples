package io.github.tantarin.concurrency.compute;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class UnsafeUserTags {
    private final ConcurrentHashMap<String, List<String>> tags = new ConcurrentHashMap<>();

    public void add(String userId, String tag) {
        tags.computeIfAbsent(userId, ignored -> new ArrayList<>()).add(tag);
    }
}
