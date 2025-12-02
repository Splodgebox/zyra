package net.zyra.processor;

import lombok.extern.log4j.Log4j2;
import net.zyra.core.AnnotationScanner;
import net.zyra.annotation.Retry;
import net.zyra.core.AnnotationProcessor;

import java.lang.reflect.Method;

@Log4j2
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
                    log.error("Failed to process annotated method after {} attempts: {}", attempts, method.getName(), e);
                }
                try {
                    Thread.sleep(delayInMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    log.error("Retry interrupted", ie);
                }
            }
        }

        log.error("Failed to process annotated method after {} attempts: {}", attempts, method.getName());
        return wrapper;
    }
}

