import java.util.ArrayList;

//public class pc_static_cyclic implements Runnable { //thread to check prime numbers with static block method
//    private int[] chk_nums;
//    public pc_static_cyclic(int[] chk_nums) {
//        this.chk_nums = chk_nums;
//    }
//    @Override
//    public void run() {
//        for (int i : chk_nums) {
//            if(Cnt.isPrime(i)) {
//                synchronized (main.static_cyclic_cnt) {
//                    main.static_cyclic_cnt.inc();
//                }
//            }
//        }
//    }
//}


public class pc_static_cyclic implements Runnable { //thread to check prime numbers with static block method
    private ArrayList<Integer> chk_nums;
    public pc_static_cyclic(ArrayList<Integer> chk_nums) {
        this.chk_nums = chk_nums;
    }
    @Override
    public void run() {
        for (int i : chk_nums) {
            if(Cnt.isPrime(i)) {
                synchronized (main.static_cyclic_cnt) {
                    main.static_cyclic_cnt.inc();
                }
            }
        }
    }
}