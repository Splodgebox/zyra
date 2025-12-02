package net.zyra.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AnnotationProxies {

    private static final Map<Class<?>, AnnotationScanner<?>> CACHE = new ConcurrentHashMap<>();

    private AnnotationProxies() {}

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> type) {
        AnnotationScanner<T> scanner =
                (AnnotationScanner<T>) CACHE.computeIfAbsent(type, AnnotationScanner::new);
        return scanner.createProxy();
    }
}
