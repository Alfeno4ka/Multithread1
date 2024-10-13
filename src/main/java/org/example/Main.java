package org.example;

import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        long startTs = System.currentTimeMillis(); // start time

        int threadsNumber = 8;
        ExecutorService threadPool = Executors.newFixedThreadPool(threadsNumber);

        List<Future<String>> resultFutures = new ArrayList<>();
        for (String text : texts) {
            resultFutures.add(CompletableFuture.supplyAsync(() -> buildResult(text), threadPool));
        }

        for (Future<String> future : resultFutures) {
            System.out.println(future.get());
        }

        long endTs = System.currentTimeMillis(); // end time

        System.out.println("Time: " + (endTs - startTs) + "ms");
    }

    private static int getMaxRepeats(String text) {
        int maxSize = 0;
        for (int i = 0; i < text.length(); i++) {
            for (int j = 0; j < text.length(); j++) {
                if (i >= j) {
                    continue;
                }
                boolean bFound = false;
                for (int k = i; k < j; k++) {
                    if (text.charAt(k) == 'b') {
                        bFound = true;
                        break;
                    }
                }
                if (!bFound && maxSize < j - i) {
                    maxSize = j - i;
                }
            }
        }

        return maxSize;
    }

    private static String buildResult(String text) {
        return text.substring(0, 100) + " -> " + getMaxRepeats(text);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}