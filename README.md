
# Zyra

Zyra is a lightweight annotation-based utility library for Java.

It provides simple, framework-agnostic method interceptors such as logging, retries, and timing, without needing Spring, AspectJ, or any heavy AOP system.

---

## Features

- `@Log` - Logs method entry, arguments, and return values using Log4j.
- `@Retry(attempts, delayInMs)` - Retries a failing method call multiple times with a configurable delay.
- `@Time` - Measures and logs how long a method takes to execute.
- Lightweight ByteBuddy + reflection-based processing.
- Works in any plain Java project.

---

## Annotations

### `@Log`

Logs:

- Method entry
- Arguments
- Return value
- Thrown exceptions

```java
import net.zyra.annotations.Log;

public class UserService {

    @Log
    public User getUser(String id) {
        return new User(id);
    }
}
```

Make sure Log4j is on your classpath and configured with an appender/layout you like.

---

### `@Retry`

Retries when the method throws an exception.

```java
import net.zyra.annotations.Retry;

public class ApiClient {

    @Retry(attempts = 3, delayInMs = 1_000)
    public String fetchData() {
        // Unstable external call
        return "...";
    }
}
```

Behaviour:

- Attempts the method up to `attempts` times.
- Sleeps `delayInMs` between attempts.
- If all attempts fail, the last exception is rethrown.

---

### `@Time`

Logs how long the method took to run.

```java
import net.zyra.annotations.Time;

public class TaskRunner {

    @Time
    public void runTask() {
        // Some work...
    }
}
```

Example log line (format will follow your Log4j configuration):

```text
[Zyra] Method runTask took 142ms
```

---

## How it works (high level)

Zyra uses:

- **ByteBuddy** to create runtime proxies/wrappers.
- **Reflection** to inspect annotations on methods.
- **Per-annotation processors** to run logic before/after the original method.

This gives you AOP-style behaviour while keeping your business logic clean and free of boilerplate.

---

## Roadmap ideas

Some possible future annotations:

- `@Async` - run methods on a background thread / executor.
- `@Cache(durationMs)` - in-memory result caching.
- `@Fallback(method = "...")` - fallback method when the main call fails.
- `@ValidateNotNull` - basic argument validation.

---
