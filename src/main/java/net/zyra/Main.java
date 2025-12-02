package net.zyra;

import net.zyra.annotation.Log;
import net.zyra.annotation.Retry;
import net.zyra.core.AnnotationProxies;

public class Main {
    private int count = 0;

    @Retry(attempts = 4, delayInMs = 150)
    public void retryTest() {
        count++;
        if (count < 3) {
            throw new RuntimeException("Test exception attempt: " + count);
        }
        System.out.println("Success on attempt: " + count);
    }

    @Log
    public String logTest(String param) {
        return param;
    }

    public static void main(String[] args) {
        Main service = AnnotationProxies.create(Main.class);
        service.retryTest();
        service.logTest("test");
    }
}
