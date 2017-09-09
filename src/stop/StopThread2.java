package stop;

// http://www.cnblogs.com/bosongokay/p/6832409.html?utm_source=itdadao&utm_medium=referral
public class StopThread2 {
    /**
     * 使用stop方法强行终止线程，可能发生不可预料的结果，不推荐使用
     */
    static class MyThread0 implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    int i = 0;
                    System.out.println(Thread.currentThread().getName() + " is running..." + i++);
                }
            } finally {
                System.out.println(Thread.currentThread().getName() + " is exiting under request.");
            }
        }
    }

    /**
     * 使用共享变量的方式（退出标志），使线程正常退出，也就是当run方法完成后线程终止
     */
    static class MyThread1 implements Runnable {
        // volatile：同一时刻只能由一个线程来修改stop的值
        private volatile boolean stop = false;

        public void shutDown() {
            stop = true;
            // 停止线程时，需要调用停止线程的interrupt()方法，因为线程有可能在wait()或sleep(), 提高停止线程的即时性
            Thread.currentThread().interrupt();
        }

        @Override
        public void run() {
            try {
                while (!stop) {
                    int i = 0;
                    System.out.println(Thread.currentThread().getName() + " is running..." + i++);
                }
            } finally {
                System.out.println(Thread.currentThread().getName() + " is exiting under request.");
            }
        }
    }

    /**
     * 建议使用如下的方法来停止线程
     */
    static class MyThread2 implements Runnable {
        // volatile：同一时刻只能由一个线程来修改stop的值
        private volatile Thread blinker;

        public void shutDown() {
            blinker = null;
        }

        @Override
        public void run() {
            Thread thisThread = Thread.currentThread();
            try {
                while (blinker == thisThread) {
                    int i = 0;
                    System.out.println(Thread.currentThread().getName() + " is running..." + i++);
                }
            } finally {
                System.out.println(Thread.currentThread().getName() + " is exiting under request.");
            }
        }
    }

    /**
     * join()
     */
    static class MyThread3 implements Runnable {
        @Override
        public void run() {
            try {
                for(int i = 1; i <= 5; i++){
                    System.out.println(Thread.currentThread().getName() + " is running..." + i);
                }
            } finally {
                System.out.println(Thread.currentThread().getName() + " is exiting under request.");
            }
        }
    }

    /**
     * 使用interrupt方法终止线程
     */
    static class MyThread4 implements Runnable {
        int i = 0;
        @Override
        public void run() {
//            while(!Thread.currentThread().isInterrupted()) {
//                System.out.println(Thread.currentThread().getName() + " is running..." + i++);
//            }

//            try {
//                while(!Thread.currentThread().isInterrupted()) {
//                    System.out.println(Thread.currentThread().getName() + " is running..." + i++);
//                    Thread.sleep(500);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

//            try {
//                while(!Thread.interrupted()) {
//                    System.out.println(Thread.currentThread().getName() + " is running..." + i++);
//                    Thread.sleep(500);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            // 如果try在while循环里时，因该在catch块里重新设置一下中断标示
//            // 因为抛出InterruptedException异常后，中断标示位会自动清除
//            while (!Thread.currentThread().isInterrupted()) {
//                try {
//                    System.out.println(Thread.currentThread().getName() + " is running..." + i++);
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt(); //重新设置中断标示
//                }
//            }

        }
    }


    public static void main(String[] args) throws InterruptedException {
//        MyThread0 mt0 = new MyThread0();
//        Thread thread0 = new Thread(mt0);
//        thread0.start();
//        Thread.sleep(500);
//        thread0.stop();

//        MyThread1 mt1 = new MyThread1();
//        Thread thread1 = new Thread(mt1);
//
//        thread1.start();
//
//        for(int i = 0; i < 4; i++) {
//            System.out.println("the main Thread is running : "+i);
//        }
//
//        System.out.println("Thread main is void");
//        mt1.shutDown();



//        MyThread2 mt2 = new MyThread2();
//        Thread thread2 = new Thread(mt2);
//
//        thread2.start();
//        mt2.shutDown();



//        MyThread3 mt3 = new MyThread3();
//        Thread thread3 = new Thread(mt3);
//
//        thread3.start();
//        try {
//            thread3.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


//        http://luckyes.iteye.com/blog/1313995
        MyThread4 mt4 = new MyThread4();
        Thread thread4 = new Thread(mt4);

        thread4.start();
        Thread.sleep(1000);
        thread4.interrupt();    // 如果有阻塞，需要使用Thread的interrupt()方中断阻塞
        // 在Thread类中有两个方法可以判断线程是否通过interrupt方法被终止。
        // 一个是静态的方法interrupted（），一个是非静态的方法isInterrupted（），
        // 这两个方法的区别是interrupted用来判断当前线是否被中断，
        // 而isInterrupted可以用来判断其他线程是否被中断。


    }
}
