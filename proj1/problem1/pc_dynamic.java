public class pc_dynamic implements Runnable { //thread to check prime numbers with static block method
    private int start_pnt;
    public pc_dynamic(int start_pnt) {
        this.start_pnt = start_pnt;
    }
    @Override
    public void run() {
        while(start_pnt < prob1_main.NUM_END) {
            for(int i = 0; i < prob1_main.TASK_SIZE; i++) {
//                if(isPrime(start_pnt + i)) {
//                    synchronized (main.dynamic_cnt) {
//                        main.dynamic_cnt.inc();
//                    }
//                }
                if(Cnt.isPrime(start_pnt + i)) {
                    synchronized (prob1_main.dynamic_cnt) {
                        prob1_main.dynamic_cnt.inc();
                    }
                }
            }
            start_pnt += prob1_main.TASK_SIZE * prob1_main.NUM_THREADS;
        }
    }
//    public static boolean isPrime(int x) {
//        if (x <= 1) return false;
//        for (int i = 2; i <= (int)Math.sqrt(x); i++) { // integers that exceed sqrt(x) should have its pair below sqrt(x)
//            if (x % i == 0) return false;
//        }
//        return true;
//    }
}