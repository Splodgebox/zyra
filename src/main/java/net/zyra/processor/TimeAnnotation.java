package net.zyra.processor;

import lombok.extern.log4j.Log4j2;
import net.zyra.annotation.Time;
import net.zyra.core.AnnotationProcessor;
import net.zyra.core.AnnotationScanner;
import net.zyra.exception.AnnotationProcessingException;

import java.lang.reflect.Method;
import java.util.Arrays;

@Log4j2
public class TimeAnnotation extends AnnotationProcessor<Time> {
    public TimeAnnotation() {
        super(Time.class);
    }

    @Override
    public Object execute(AnnotationScanner.MethodWrapper wrapper) {
        Method method = wrapper.originalMethod();
        Object[] args = wrapper.args();


        long startTime = System.nanoTime();
        try {
            return wrapper.invoke();
        } catch (Exception e) {
            log.warn("Exception in {} with args {}", method.getName(), Arrays.toString(args));
        } finally {
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            if (args != null && args.length > 0) {
                log.info("Time taken for {} with args {} is {} ms", method.getName(), Arrays.toString(args), duration / 1_000_000);
            } else {
                log.info("Time taken for {} is {} ms", method.getName(), duration / 1_000_000);
            }
        }

        return wrapper;
    }
}
