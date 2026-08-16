package io.github.tantarin.concurrency.liveness;

public final class OrderedTransferService {
    public void transfer(Account from, Account to, long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }

        Account first = from.id < to.id ? from : to;
        Account second = from.id < to.id ? to : from;
        synchronized (first) {
            synchronized (second) {
                if (from.balance < amount) {
                    throw new IllegalStateException("not enough money");
                }
                from.balance -= amount;
                to.balance += amount;
            }
        }
    }

    public static final class Account {
        private final long id;
        private long balance;

        public Account(long id, long balance) {
            this.id = id;
            this.balance = balance;
        }

        public synchronized long balance() {
            return balance;
        }
    }
}
