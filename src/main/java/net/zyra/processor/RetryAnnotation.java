package net.zyra.processor;

import net.zyra.core.AnnotationScanner;
import net.zyra.exception.AnnotationProcessingException;
import net.zyra.annotation.Retry;
import net.zyra.core.AnnotationProcessor;

import java.lang.reflect.Method;

public class RetryAnnotation extends AnnotationProcessor<Retry> {
    public RetryAnnotation() {
        super(Retry.class);
    }

    @Override
    public Object execute(AnnotationScanner.MethodWrapper wrapper) {
        Method method = wrapper.originalMethod();
        Retry retry = getAnnotation(method);

        int attempts = retry.attempts();
        int delayInMs = retry.delayInMs();

        for (int i = 0; i < attempts; i++) {
            try {
                method.setAccessible(true);
                return wrapper.invoke();
            } catch (Exception e) {
                if (i == attempts - 1) {
                    throw new AnnotationProcessingException("Failed to process annotated method after " + attempts + " attempts: " + method.getName(), e);
                }
                try {
                    Thread.sleep(delayInMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new AnnotationProcessingException("Retry interrupted", ie);
                }
            }
        }

        throw new AnnotationProcessingException("Failed to process annotated method after " + attempts + " attempts: " + method.getName());
    }
}

