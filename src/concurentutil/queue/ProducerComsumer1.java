package concurentutil.queue;

import java.util.PriorityQueue;

/**
 * 进阶wait notify
 */
public class ProducerComsumer1 {
    private static final int queueSize = 10;
    private PriorityQueue queue = new PriorityQueue<>(queueSize);

    public static void main(String[] args) {
        ProducerComsumer1 test = new ProducerComsumer1();
        Producer producer = test.new Producer();
        Consumer consumer = test.new Consumer();
        producer.start();
        consumer.start();
    }

    class Producer extends Thread {
        @Override
        public void run() {
            try {
                produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // produce()中的notify是用来使consumer()中阻塞的wait打通。
        private void produce() throws InterruptedException {
            while (true) {
                synchronized (queue) {  //只有一个线程能访问对象queue
                    while (queue.size() == queueSize) {
                        try {
                            System.out.println("队列满，等待有空余空间, 堵塞住，等待Consumer->Notify()");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            queue.notify();
                        }
                    }
                    queue.offer(1); //每次插入一个元素
                    queue.notify(); //是解Consumer中的wait()
                    System.out.println("向队列取中插入一个元素，队列剩余空间：" + (queueSize - queue.size()));
                }
            }
        }
    }

    class Consumer extends Thread {
        @Override
        public void run() {
            try {
                consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // consumer()中的nofity是用来使producer()中的阻塞的wait打通。
        private void consume() throws InterruptedException {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == 0) {
                        try {
                            System.out.println("队列空，等待数据, 堵塞住，等待Producer->Notify()");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            queue.notify();
                        }
                    }
                    queue.poll();   //每次移走队首元素
                    queue.notify(); //是解produce中的wait()
                    System.out.println("从队列取走一个元素，队列剩余" + queue.size() + "个元素");
                }
            }
        }
    }
}
