package io.github.tantarin.concurrency.waitnotify;

import java.util.Objects;

public final class BoundedBuffer<E> {
    private final Object[] elements;
    private int head;
    private int tail;
    private int size;

    public BoundedBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        elements = new Object[capacity];
    }

    public synchronized void put(E element) throws InterruptedException {
        Objects.requireNonNull(element, "element");
        while (size == elements.length) {
            wait();
        }

        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
        notifyAll();
    }

    @SuppressWarnings("unchecked")
    public synchronized E take() throws InterruptedException {
        while (size == 0) {
            wait();
        }

        E element = (E) elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        notifyAll();
        return element;
    }

    public synchronized int size() {
        return size;
    }
}
