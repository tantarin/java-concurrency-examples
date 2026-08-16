# Границы безопасности computeIfAbsent

`ConcurrentHashMap.computeIfAbsent()` атомарно регистрирует значение для ключа, но не превращает возвращённый объект в потокобезопасный.

`UnsafeUserTags` показывает антипаттерн: несколько потоков изменяют один `ArrayList` после завершения `computeIfAbsent()`.

`UserTags` показывает два безопасных протокола:

- значение само является concurrent collection — `ConcurrentHashMap.newKeySet()`;
- всё обновление выполняется через `compute()`, а наружу публикуется immutable snapshot.

```bash
mvn test
```
