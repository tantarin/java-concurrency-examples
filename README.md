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

Репозиторий будет расширяться отдельными Maven-модулями по темам: race condition,
locks, blocking queues, executors, futures, atomics и virtual threads.

## Проверка

Нужны JDK 8+ и Maven 3:

```bash
mvn test
```
