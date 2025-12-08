package net.zyra;

import lombok.extern.log4j.Log4j2;
import net.zyra.annotation.Log;
import net.zyra.annotation.Retry;
import net.zyra.annotation.Time;
import net.zyra.core.AnnotationProxies;

import java.util.Random;

@Log4j2
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

    @Time
    public void execute() {
        Random random = new Random();
        random.nextInt();
        System.out.println("Executed");
    }

    @Time
    public void executeLonger() {
        Random random = new Random();
        random.nextInt();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Executed");
    }

    public static void main(String[] args) {
        Main service = AnnotationProxies.create(Main.class);
//        service.retryTest();
//        log.info(service.logTest("test"));

        service.execute();
        service.executeLonger();
    }
}
