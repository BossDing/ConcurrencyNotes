package concurentutil.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentHashMapTest4 {
    static class MyThread implements Runnable {
        static final Object lock = new Object();
        static final String KEY = "key";
        private Map<String, Integer> map;

        MyThread(Map<String, Integer> map) {
            this.map = map;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                synchronized (lock) {
                    this.addup();
                }
            }
        }

        private void addup() {
            if (!map.containsKey(KEY)) {
                map.put(KEY, 1);
            } else {
                map.put(KEY, map.get(KEY) + 1);
            }
        }
    }

    private static int test() throws InterruptedException {
        Map<String, Integer> map = new HashMap<>();
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 8; i++) {
            pool.execute(new MyThread(map));
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.DAYS);

        return map.get(MyThread.KEY);
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            System.out.println(test());
        }
    }
}
