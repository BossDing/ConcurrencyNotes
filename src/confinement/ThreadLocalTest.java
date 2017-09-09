package confinement;

public class ThreadLocalTest {


    static class MyThread implements Runnable {
        private int index;
        private Integer value=0;

        MyThread(int index) {
            this.index = index;
        }

        public void run() {
            System.out.println("线程" + index + "的初始value:" + value);
            for (int i = 0; i < 10; i++) {
//                value.set(value.get() + i);
                value+=i;
            }
            System.out.println("线程" + index + "的累加value:" + value);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new MyThread(i)).start();
        }
    }
}
