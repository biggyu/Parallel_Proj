import java.util.concurrent.atomic.AtomicInteger;

public class ex3 {
    private static int SERVER_SPACE = 5;
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (int i = 1; i <= 4; i++) {
            AtomicIntegerTemp c = new AtomicIntegerTemp("Temp " + i, atomicInteger);
        }
        for(int i = 1; i <= 3; i++) {
            AtomicIntegerTemp c = new AtomicIntegerFreeze("Freeze " + i, atomicInteger);
            AtomicIntegerTemp w = new AtomicIntegerHot("Hot " + i, atomicInteger);
        }
        System.out.println("Temperature modification using AtomicInteger");

    }
}
class AtomicIntegerTemp extends Thread {
    protected AtomicInteger atomicInteger;
    private String name;
    public AtomicIntegerTemp(String name, AtomicInteger atomicInteger) {
        super(name);
        this.atomicInteger = atomicInteger;
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep((int)(Math.random() * 10000)); // wait time
                double rnd = Math.random();
                if(rnd > .5) {
//                    tryingCooling();
                    if(rnd > .75) {
                        System.out.printf("%-7s: Check temperature than cool\nCurrent temperature: %d\n",
                                this.getName(), this.atomicInteger.getAndDecrement());
                        for(int i = 1; i <= (int)(Math.random() * 10) - 1; i++) {
                            this.atomicInteger.getAndDecrement();
                        }
                    } else {
                        System.out.printf("%-7s: Cool than check temperature\n", this.getName());
                        for(int i = 1; i <= (int)(Math.random() * 10); i++) {
                            this.atomicInteger.decrementAndGet();
                        }
                    }
                } else {
//                    tryingWarming();
                    if(rnd > .25) {
                        System.out.printf("%-7s:\t\t\t\t Check temperature than warm\nCurrent temperature: %d\n",
                                this.getName(), this.atomicInteger.getAndAdd((int)(Math.random() * 10)));
                    } else {
                        System.out.printf("%-7s:\t\t\t\t Warm than check temperature\n", this.getName());
                        this.atomicInteger.addAndGet((int)(Math.random() * 10));
                    }
                }
                System.out.println("Current temperature: "+ this.atomicInteger.get());
            } catch (InterruptedException e) { }
        }
    }
}
class AtomicIntegerFreeze extends AtomicIntegerTemp {

    public AtomicIntegerFreeze(String name, AtomicInteger atomicInteger) {
        super(name, atomicInteger);
    }

    public void tryingFreezing() {
        System.out.printf("%-7s: trying to freeze\n", this.getName());
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep((int)(Math.random() * 20000)); // wait time
                tryingFreezing();
                this.atomicInteger.set(0);
                System.out.println("Current temperature: " + this.atomicInteger.get());
            } catch (InterruptedException e) { }
        }
    }
}
class AtomicIntegerHot extends AtomicIntegerTemp {

    public AtomicIntegerHot(String name, AtomicInteger atomicInteger) {
        super(name, atomicInteger);
    }

    public void tryingHot() {
        System.out.printf("%-7s:\t\t\t\t making it hot\n", this.getName());
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep((int)(Math.random() * 20000)); // wait time
                tryingHot();
                this.atomicInteger.set(this.atomicInteger.get() + 20);
                System.out.println("Current temperature: " + this.atomicInteger.get());
            } catch (InterruptedException e) { }
        }
    }
}