package stop;

public class StopThread1 {
    static class MyThread1 implements Runnable {
        public volatile boolean stop = false;

        private void dosomethig() throws InterruptedException {
            long time = System.currentTimeMillis();
            while (System.currentTimeMillis() - time < 1000) {

            }
            System.out.println("all things had been done!");
        }

        @Override
        public void run() {
            try {
                while (!stop) {
                    System.out.println(Thread.currentThread().getName() + " is running...");
                    dosomethig();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() + " is exiting under request.");
            }
        }
    }

    static class MyThread2 implements Runnable {
        public volatile boolean stop = false;

        private void dosomethig() throws InterruptedException {
//            long time = System.currentTimeMillis();
//            while (System.currentTimeMillis() - time < 1000) {
//
//            }
            Thread.currentThread().join();
            System.out.println("all things had been done!");
        }

        @Override
        public void run() {
            try {
                while (!stop) {
                    System.out.println(Thread.currentThread().getName() + " is running...");
                    dosomethig();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() + " is exiting under request.");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyThread1 mt1 = new MyThread1();
        Thread thread1 = new Thread(mt1);

        System.out.println("System is ready to start thread");
        thread1.start();

        Thread.sleep(3000);

        System.out.println("System is ready to stop thread");
        // 线程没有处于阻塞状态，调用线程对应的interrupt()不能让运行的线程停止下来
        thread1.interrupt();
        mt1.stop = true; // 加了这句才有效



        MyThread2 mt2 = new MyThread2();
        Thread thread2 = new Thread(mt2);

        System.out.println("System is ready to start thread");
        thread2.start();

        Thread.sleep(3000);

        System.out.println("System is ready to stop thread");
        mt2.stop = true; // 加了这句才有效
    }
}
