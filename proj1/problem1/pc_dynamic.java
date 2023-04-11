public class pc_dynamic implements Runnable { //thread to check prime numbers with static block method
    private int start_pnt;
    public pc_dynamic(int start_pnt) {
        this.start_pnt = start_pnt;
    }
    @Override
    public void run() {
        while(start_pnt < prob1_main.NUM_END) {
            for(int i = 0; i < prob1_main.TASK_SIZE; i++) {
                if(Cnt.isPrime(start_pnt + i)) {
                    synchronized (prob1_main.dynamic_cnt) {
                        prob1_main.dynamic_cnt.inc();
                    }
                }
            }
            start_pnt += prob1_main.TASK_SIZE * prob1_main.NUM_THREADS;
        }
    }
}