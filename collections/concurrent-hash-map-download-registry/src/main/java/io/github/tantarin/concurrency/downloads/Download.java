package io.github.tantarin.concurrency.downloads;

import java.net.URI;
import java.util.Objects;

public final class Download {
    private final String id;
    private final URI source;

    public Download(String id, URI source) {
        this.id = Objects.requireNonNull(id, "id");
        this.source = Objects.requireNonNull(source, "source");
    }

    public String getId() {
        return id;
    }

    public URI getSource() {
        return source;
    }
}
