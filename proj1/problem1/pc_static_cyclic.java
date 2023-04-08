class pc_static_cyclic implements Runnable { //thread to check prime numbers with static block method
    private int[] chk_num;
    public pc_static_cyclic(int[] chk_num) {
        this.chk_num = chk_num;
    }
    @Override
    public void run() {
        for (int i : chk_num) {
            if(main.static_cyclic_cnt.isPrime(i)) {
                synchronized (main.static_cyclic_cnt) {
                    main.static_cyclic_cnt.inc();
                }
            }
        }
//        while(start_pnt < main.NUM_END) {
//            for(int i = 0; i < main.TASK_SIZE; i++) {
//                if(pc_static_block.isPrime(start_pnt + i)) {
//                    synchronized (main.static_cyclic_cnt) {
//                        main.static_cyclic_cnt.inc();
//                    }
//                }
//            }
//            start_pnt += main.TASK_SIZE * main.NUM_THREADS;
//        }
    }
}