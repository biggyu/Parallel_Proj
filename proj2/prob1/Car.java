import java.util.concurrent.BlockingQueue;

public class Car extends Thread {
    private BlockingQueue<Car> queue;
    private String name;
    Car(String name, BlockingQueue<Car> queue) {
        super(name);
        this.queue = queue;
        start();
    }

    public void tryingEnter() {
        System.out.printf("%-7s: trying to enter\n", this.getName());
//        System.out.println(this.getName()+": trying to enter");
    }

    public void justEntered() {
        System.out.printf("%-7s: just entered\n", this.getName());
//        System.out.println(this.getName()+": just entered");
    }

    public void aboutToLeave() {
        System.out.printf("%-7s:\t\t\t\t\t about to leave\n", this.getName());
//        System.out.println(this.getName()+":\\t\\t\\t\\t\\t about to leave");
    }

    public void justLeft() {
        System.out.printf("%-7s:\t\t\t\t\t have been left\n", this.getName());
//        System.out.println(this.getName()+":\\t\\t\\t\\t\\t have been left");
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep((int)(Math.random() * 10000)); // drive before parking
                tryingEnter();
                this.queue.put(this);
                justEntered();
                sleep((int)(Math.random() * 20000)); // stay within the parking garage
                aboutToLeave();
                while (this.queue.take() == null);
                justLeft();
            } catch (InterruptedException e) {}
        }
    }
}
