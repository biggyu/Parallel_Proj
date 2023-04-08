//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.stream.Stream;
//class Static_block implements Runnable { //thread to check prime numbers with static block method
//    private int block_size, start_pnt;
//    public Static_block(int block_size, int start_pnt) {
//        this.block_size = block_size;
//        this.start_pnt = start_pnt;
//    }
//    @Override
//    public void run() {
//        for(int i = this.start_pnt; i < this.start_pnt + this.block_size; i++) {
//            if (isPrime(i)) {
//                synchronized (pc_static_block.static_block_cnt) {
//                    pc_static_block.static_block_cnt.inc();
//                }
//            }
//        }
//    }
//    private static boolean isPrime(int x) {
//        if (x <= 1) return false;
////        for (int i = 2; i < x; i++) {
//        for (int i = 2; i <= (int)Math.sqrt(x); i++) { //only have to check square of x
//            if (x % i == 0) return false;
//        }
//        return true;
//    }
//}
//public class pc_static_block {
//    public static Cnt static_block_cnt = new Cnt();
//    private static int NUM_END = 200000;
//    private static int NUM_THREADS = 1;
//    public static void main(String[] args) throws IOException {
//        int[] thread_nums = null;
//        if (args.length == 2) { //gets both input of NUM_END & NUM_THREADS
//            NUM_THREADS = Integer.parseInt(args[0]);
//            NUM_END = Integer.parseInt(args[1]);
//            thread_nums = new int[]{NUM_THREADS};
//        }
//        else { //gets input of NUM_END and use data in thread_num.txt
//            NUM_END = args.length > 0 ? Integer.parseInt(args[0]) : 200000;
//            BufferedReader reader = new BufferedReader(
//                    new FileReader("thread_num.txt")
//            );
//            thread_nums = Stream.of(reader.readLine().split(",")).mapToInt(Integer::parseInt).toArray();
//            reader.close();
//        }
//        for(int k : thread_nums) {
//            NUM_THREADS = k;
//            static_block_cnt.setCnt(0);
//            int block_size = NUM_END / NUM_THREADS ;
//            block_size += NUM_END % NUM_THREADS == 0 ? 0 : 1;
//            long startTime = System.currentTimeMillis();
//            for (int i = 0; i < NUM_THREADS - 1; i++) {
//                Static_block block = new Static_block(block_size, i * block_size); //thread creation for checking prime#
//                block.run();
//            }
//            Static_block block = new Static_block(NUM_END % block_size == 0 ? block_size : NUM_END % block_size, (NUM_THREADS - 1) * block_size);
//            block.run();
//
//            long endTime = System.currentTimeMillis();
////            long timeDiff = endTime - startTime;
////        System.out.println("Program Execution Time: " + timeDiff + "ms");
//            System.out.println(NUM_THREADS + "threads took execution time of " + (endTime - startTime) + "ms");
//            System.out.println("prime# counter=" + static_block_cnt.getCnt());
//        }
//    }
//
//}
public class pc_static_block implements Runnable { //thread to check prime numbers with static block method
    private int block_size, start_pnt;
    public pc_static_block(int block_size, int start_pnt) {
        this.block_size = block_size;
        this.start_pnt = start_pnt;
    }
    @Override
    public void run() {
        for(int i = this.start_pnt; i < this.start_pnt + this.block_size; i++) {
            if (isPrime(i)) {
                synchronized (main.static_block_cnt) {
                    main.static_block_cnt.inc();
                }
            }
        }
    }
    public static boolean isPrime(int x) {
        if (x <= 1) return false;
        for (int i = 2; i <= (int)Math.sqrt(x); i++) { // integers that exceed sqrt(x) should have its pair below sqrt(x)
            if (x % i == 0) return false;
        }
        return true;
    }
}