package net.zyra.core;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import net.zyra.processor.LogAnnotation;
import net.zyra.processor.RetryAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationScanner<T> {

    private final Class<T> type;
    private final Map<Class<? extends Annotation>, AnnotationProcessor<?>> processors = new HashMap<>();

    public AnnotationScanner(Class<T> type) {
        this.type = type;
        registerDefaultProcessors();
    }

    private void registerDefaultProcessors() {
        register(new RetryAnnotation());
        register(new LogAnnotation());
    }

    public void register(AnnotationProcessor<?> processor) {
        processors.put(processor.getAnnotationClass(), processor);
    }

    public T createProxy() {
        try {
            Object target = type.getDeclaredConstructor().newInstance();

            InvocationHandler handler = (proxy, method, args) -> {
                Method targetMethod = type.getMethod(method.getName(), method.getParameterTypes());

                MethodWrapper ctx = new MethodWrapper(target, targetMethod, args);

                for (AnnotationProcessor<?> processor : processors.values()) {
                    if (targetMethod.isAnnotationPresent(processor.getAnnotationClass())) {
                        return processor.execute(ctx);
                    }
                }

                return targetMethod.invoke(target, args);
            };

            return new ByteBuddy()
                    .subclass(type)
                    .method(ElementMatchers.any())
                    .intercept(InvocationHandlerAdapter.of(handler))
                    .make()
                    .load(type.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create proxy", e);
        }
    }

    public record MethodWrapper(Object target, Method originalMethod, Object[] args) {
        public Object invoke() throws Exception {
            return originalMethod.invoke(target, args);
        }
    }
}
