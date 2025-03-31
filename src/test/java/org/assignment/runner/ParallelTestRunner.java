package org.assignment.runner;

import org.assignment.tests.ELPaisTest;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelTestRunner {
    public static void main(String[] args) throws InterruptedException {
        String[][] platforms = {
                {"safari", "iPhone 15 Pro Max", "17"},
                {"chrome", "Samsung Galaxy Tab S9", "13"},
                {"chromium", "iPad 9th", "18"},
                {"chrome", "Windows", "11", "latest"},
                {"edge", "Windows", "10", "latest"}
        };

        // Define a common build name
        String buildName = "ELPaisTest-Build-" + System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (String[] platform : platforms) {
            executor.execute(() -> {
                try {
                    ELPaisTest test = new ELPaisTest();
                    test.setup(
                            platform[0],
                            platform[1],
                            platform[2],
                            platform.length > 3 ? platform[3] : "latest",
                            buildName
                    );
                    test.elPaisTest();
                    test.tearDownTest();
                } catch (Exception e) {
                    System.out.println("Error on " + Arrays.toString(platform) + ": " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
    }
}
