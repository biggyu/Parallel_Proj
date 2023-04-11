public class pc_static_block implements Runnable { //thread to check prime numbers with static block method
    private int block_size, start_pnt;
    public pc_static_block(int block_size, int start_pnt) {
        this.block_size = block_size;
        this.start_pnt = start_pnt;
    }
    @Override
    public void run() {
        for(int i = this.start_pnt; i < this.start_pnt + this.block_size; i++) {
            if (Cnt.isPrime(i)) {
                synchronized (prob1_main.static_block_cnt) {
                    prob1_main.static_block_cnt.inc();
                }
            }
        }
    }
}