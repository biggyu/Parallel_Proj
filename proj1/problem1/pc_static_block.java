public class pc_static_block implements Runnable { //thread to check prime numbers with static block method
    private int block_size, start_pnt;
    public pc_static_block(int block_size, int start_pnt) {
        this.block_size = block_size;
        this.start_pnt = start_pnt;
    }
    @Override
    public void run() {
        for(int i = this.start_pnt; i < this.start_pnt + this.block_size; i++) {
//            if (isPrime(i)) {
//                synchronized (main.static_block_cnt) {
//                    main.static_block_cnt.inc();
//                }
//            }
            if (Cnt.isPrime(i)) {
                synchronized (main.static_block_cnt) {
                    main.static_block_cnt.inc();
                }
            }
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