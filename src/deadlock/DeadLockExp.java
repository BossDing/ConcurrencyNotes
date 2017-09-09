package deadlock;

public class DeadLockExp implements Runnable {
    private final Resource myOwn;
    private final Resource myNeed;

    DeadLockExp(Resource myOwn, Resource myNeed) {
        this.myOwn = myOwn; this.myNeed = myNeed;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        try {
            while(true) {
                synchronized(myOwn) {
                    System.out.println("Thread "+threadName+" has got Resource "+myOwn);
                    Thread.sleep(1000);
                    synchronized(myNeed) {
                        System.out.println("Thread "+threadName+" has got Resource "+myNeed);
                        Thread.sleep(1000);
                        System.out.println("Thread "+threadName+"'s job has done.");
                    }
                }
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
