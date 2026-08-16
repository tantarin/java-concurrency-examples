# Java Concurrency Examples

Набор небольших запускаемых подпроектов к учебнику
[«Многопоточность в Java»](https://github.com/tantarin/java-concurrency-book).

Каждый подпроект отвечает на один вопрос:

1. Какая практическая проблема возникла?
2. Какие решения доступны?
3. Почему выбран конкретный concurrency-инструмент?
4. Как тестом воспроизвести требуемое конкурентное поведение?

## Подпроекты

| Тема | Практический пример | Что показывает |
|---|---|---|
| Concurrent collections | [Download registry](./collections/concurrent-hash-map-download-registry) | почему для общего поиска по ключу подходит `ConcurrentHashMap` |
| Monitor coordination | [Bounded buffer](./synchronization/wait-notify-bounded-buffer) | как `wait()` и `notifyAll()` координируют producer и consumer |
| Liveness failures | [Deadlock vs. livelock](./liveness/deadlock-vs-livelock) | чем блокировка навсегда отличается от активности без прогресса |
| Task contracts | [Runnable vs. Callable](./tasks/runnable-vs-callable) | как выбрать задачу-действие или задачу-вычисление с результатом |
| Parallel computations | [Fork/Join array sum](./parallelism/fork-join-array-sum) | как рекурсивно делить CPU-bound вычисление и объединять результаты |

Репозиторий будет расширяться отдельными Maven-модулями по темам: race condition,
locks, blocking queues, executors, futures, atomics и virtual threads.

## Проверка

Нужны JDK 8+ и Maven 3:

```bash
mvn test
```
