package deadlock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Problem1 {
    public static void main(String[] args) {
        int num = 4;
        Resource[] res = new Resource[num];
        ExecutorService exec = Executors.newFixedThreadPool(num);
        for(int i = 0; i < num; i++){
            res[i] = new Resource(i);
        }
        for(int i = 0; i < num; i++) {
            exec.execute(new DeadLockExp(res[i], res[(i+1)%num]));
        }
        exec.shutdown();
    }
}
