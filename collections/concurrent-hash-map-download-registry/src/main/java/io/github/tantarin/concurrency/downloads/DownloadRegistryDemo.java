package io.github.tantarin.concurrency.downloads;

import java.net.URI;

public final class DownloadRegistryDemo {
    private DownloadRegistryDemo() {
    }

    public static void main(String[] args) {
        DownloadRegistry registry = new DownloadRegistry();

        Download download = registry.findOrStart(
            "java-concurrency-book",
            id -> new Download(id, URI.create("https://example.com/book.pdf"))
        );

        System.out.println(download.getId() + " -> " + download.getSource());
    }
}
