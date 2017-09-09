package concurentutil.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class ConcurrentHashMapTest {
    private final Map<String, Long> wordCounts = new ConcurrentHashMap<>();

    public long increase(String word) {
        Long oldValue, newValue;
        while (true) {
            oldValue = wordCounts.get(word);
            if (oldValue == null) {
                // Add the word firstly, initial the value as 1
                newValue = 1L;
                if (wordCounts.putIfAbsent(word, newValue) == null) {
                    break;
                }
            } else {
                newValue = oldValue + 1;
                if (wordCounts.replace(word, oldValue, newValue)) {
                    break;
                }
            }
        }
        return newValue;
    }


    public static void main(String[] args) {
        ConcurrentHashMapTest test = new ConcurrentHashMapTest();
        String str = "test";
        CountDownLatch latch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(() -> {
                test.increase(str);
                System.out.println(1);
            });
            t.start();
        }
        latch.countDown();
        System.out.println(test.wordCounts.get(str));
    }
}
