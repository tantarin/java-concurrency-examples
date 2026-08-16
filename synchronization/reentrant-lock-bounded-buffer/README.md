# Ограниченный буфер через ReentrantLock и Condition

Это тот же producer–consumer сценарий, что и в модуле `wait-notify-bounded-buffer`, но явный lock позволяет создать две очереди ожидания:

- `notEmpty` — для consumers;
- `notFull` — для producers.

Поэтому `put()` будит только consumer через `notEmpty.signal()`, а `take()` — только producer через `notFull.signal()`. Захват выполняется через `lockInterruptibly()`, освобождение гарантируется блоком `finally`.

```bash
mvn test
```
