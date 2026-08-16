# Deadlock vs. livelock

- `DeadlockDemo` детерминированно создаёт цикл ожидания двух intrinsic locks и обнаруживает его через `ThreadMXBean`. Заблокированные потоки сделаны daemon, поэтому демонстрация завершается.
- `LivelockDemo` выполняет ограниченное число симметричных повторов: потоки работают и освобождают locks, но ни один не завершает полезную операцию.
- `OrderedTransferService` предотвращает deadlock единым порядком захвата счетов.

```bash
mvn test
```

Демонстрация deadlock из каталога модуля:

```bash
mvn package
java -cp target/classes io.github.tantarin.concurrency.liveness.DeadlockDemo
```
