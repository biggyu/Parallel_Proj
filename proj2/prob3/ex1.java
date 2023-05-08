import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ex1 {
    private static int SERVER_SPACE = 5;
    public static void main(String[] args) {
        BlockingQueue<BlockingQueueClient> webServer = new ArrayBlockingQueue<>(SERVER_SPACE);
        for (int i = 1; i <= 10; i++) {
            BlockingQueueClient c = new BlockingQueueClient("Client " + i, webServer);
        }
        System.out.println("Server using ArrayBlockingQueue");

    }
}
class BlockingQueueClient extends Thread {
    private BlockingQueue<BlockingQueueClient> queue;
    private String name;
    public BlockingQueueClient(String name, BlockingQueue<BlockingQueueClient> queue) {
        super(name);
        this.queue = queue;
        start();
    }

    public void tryingEnter() {
        System.out.printf("%-7s: trying to enter server\n", this.getName());
    }

    public void justEntered() {
        System.out.printf("%-7s: just entered server\n", this.getName());
    }

    public void aboutToLeave() {
        System.out.printf("%-7s:\t\t\t\t about to leave server\n", this.getName());
    }

    public void justLeft() {
        System.out.printf("%-7s:\t\t\t\t have left server\n", this.getName());
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep((int)(Math.random() * 15000)); // conneciton time
                tryingEnter();
                this.queue.put(this);
                justEntered();
                sleep((int)(Math.random() * 20000)); // server surfing
                aboutToLeave();
                while (this.queue.take() == null);
                justLeft();
            } catch (InterruptedException e) { }
        }
    }
}
