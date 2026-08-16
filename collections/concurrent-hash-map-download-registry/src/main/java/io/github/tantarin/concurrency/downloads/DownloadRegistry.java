package io.github.tantarin.concurrency.downloads;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public final class DownloadRegistry {
    private final ConcurrentHashMap<String, Download> downloads =
        new ConcurrentHashMap<>();

    public Download find(String id) {
        return downloads.get(id);
    }

    public Download findOrStart(
        String id,
        Function<String, Download> startDownload
    ) {
        return downloads.computeIfAbsent(id, startDownload);
    }

    public Collection<Download> snapshot() {
        return new ArrayList<>(downloads.values());
    }

    public int size() {
        return downloads.size();
    }
}
