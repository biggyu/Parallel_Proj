import java.util.concurrent.CyclicBarrier;

public class ex4 {
    private final static int THREADS = 6;
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(THREADS);
        for(int i = 1; i <= THREADS; ++i) {
            SleepingThread t = new SleepingThread("Thread " + i, cyclicBarrier);
        }

    }
}
class SleepingThread extends Thread {
    private CyclicBarrier cyclicBarrier;
    public SleepingThread(String name, CyclicBarrier cyclicBarrier) {
        super(name);
        this.cyclicBarrier = cyclicBarrier;
        start();
    }

    @Override
    public void run() {
        int delay = (int)(Math.random() * 10000);
        try {
            System.out.println(this.getName() + " : Sleeping for " + delay + "ms");
            Thread.sleep(delay);
            System.out.println(this.getName() + " : Woke up");
        } catch (InterruptedException e) { }
        try {
            cyclicBarrier.await();
        } catch (Exception e) { }
        System.out.println(this.getName() + " : Ended");
    }
}