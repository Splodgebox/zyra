package net.zyra.processor;

import lombok.extern.log4j.Log4j2;
import net.zyra.annotation.Log;
import net.zyra.core.AnnotationProcessor;
import net.zyra.core.AnnotationScanner;
import net.zyra.exception.AnnotationProcessingException;

import java.lang.reflect.Method;
import java.util.Arrays;

@Log4j2
public class LogAnnotation extends AnnotationProcessor<Log> {

    public LogAnnotation() {
        super(Log.class);
    }

    @Override
    public Object execute(AnnotationScanner.MethodWrapper wrapper) {
        Method method = wrapper.originalMethod();
        Object[] args = wrapper.args();

        log.info("Entering {} with args {}", method.getName(), Arrays.toString(args));
        try {
            Object result = wrapper.invoke();
            log.info("Exiting {} with result {}", method.getName(), result);
            return result;
        } catch (Exception e) {
            log.error("Exception in {}", method.getName(), e);
            throw new AnnotationProcessingException("Error executing " + method.getName(), e);
        }
    }
}
