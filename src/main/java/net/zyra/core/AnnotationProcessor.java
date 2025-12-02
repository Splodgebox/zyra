package net.zyra.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Getter
@RequiredArgsConstructor
public abstract class AnnotationProcessor<T extends Annotation> {

    private final Class<T> annotationClass;

    public void hasAnnotatedMethod(Object target) {
        validateTarget(target);
        findAnnotatedMethod(target);
    }

    protected Method findAnnotatedMethod(Object target) {
        Class<?> targetClass = target.getClass();
        for (Method method : targetClass.getDeclaredMethods()) {
            if (isMethodAnnotated(method)) {
                return method;
            }
        }
        return null;
    }

    protected T getAnnotation(Method method) {
        return method.getAnnotation(annotationClass);
    }

    protected void getAnnotationFromTarget(Object target) {
        Method method = findAnnotatedMethod(target);
        if (method == null) {
            throw new IllegalStateException("No method found with annotation " + annotationClass.getSimpleName());
        }
        getAnnotation(method);
    }

    private void validateTarget(Object target) {
        if (target == null) {
            throw new IllegalArgumentException("Target object cannot be null");
        }
    }

    private boolean isMethodAnnotated(Method method) {
        return method.isAnnotationPresent(annotationClass);
    }

    public abstract Object execute(AnnotationScanner.MethodWrapper wrapper);

}


