import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;
public class main {
    public static Cnt static_block_cnt = new Cnt();
    public static Cnt static_cyclic_cnt = new Cnt();
    public static Cnt dynamic_cnt = new Cnt();
    public static int NUM_END = 200000;
    public static int NUM_THREADS = 1;
    public static int TASK_SIZE = 10;

    public static void main(String[] args) throws IOException {
        if (args.length == 2) { //gets both input of TASK_SIZE & NUM_END
            TASK_SIZE = Integer.parseInt(args[0]);
            NUM_END = Integer.parseInt(args[1]);
        }
        else if(args.length == 1){ //gets input of TASK_SIZE
            TASK_SIZE = Integer.parseInt(args[0]);
        }

        BufferedReader reader = new BufferedReader(
                new FileReader("thread_num.txt") //NUM_THREAD is initialized by thread_num.txt
        );
        int[] thread_nums = Stream.of(reader.readLine().split(",")).mapToInt(Integer::parseInt).toArray();
        reader.close();

//        System.out.println("Static Block Method");
//        for(int k : thread_nums) { //pc_static_block method
//            NUM_THREADS = k;
//            static_block_cnt.setCnt(0);
//            int block_size = NUM_END / NUM_THREADS;
//            block_size += NUM_END % NUM_THREADS == 0 ? 0 : 1;
//            long startTime = System.currentTimeMillis();
//            for (int i = 0; i < NUM_THREADS - 1; i++) {
//                pc_static_block block = new pc_static_block(block_size, i * block_size);
//                block.run();
//            }
//            pc_static_block block = new pc_static_block(NUM_END % block_size == 0 ? block_size : NUM_END % block_size, (NUM_THREADS - 1) * block_size);
//            block.run();
//            long endTime = System.currentTimeMillis();
//            System.out.println(NUM_THREADS + "threads took execution time of " + (endTime - startTime) + "ms");
//            System.out.println("1..." + (NUM_END - 1) + "prime# counter=" + static_block_cnt.getCnt());
//        }

        System.out.println("\nStatic Cyclic Method");
        for(int k : thread_nums) { //pc_static_cyclic method
            NUM_THREADS = k;
            static_cyclic_cnt.setCnt(0);

//            int[][] chk_nums = new int[NUM_THREADS][];
//            int leftover = (((int)NUM_END / NUM_THREADS % 10) * NUM_THREADS + NUM_END % NUM_THREADS) / 10;
//            for(int i = 0; i < leftover; i++) {
//                chk_nums[i] = new int[(int)NUM_END / NUM_THREADS / 10 * 10 + 10];
//            }
//            for(int i = leftover; i < NUM_THREADS; i++) {
//                chk_nums[i] = new int[(int)NUM_END / NUM_THREADS / 10 * 10];
//            }
//
//            int num = 0;
//            boolean chk = false;
//            while(true) {
//                for(int i = 0; i < NUM_THREADS; i++) {
//                    for(int j = 0; j < TASK_SIZE; j++) {
//                        chk_nums[i][j] = num + TASK_SIZE * i + j;
//                    }
//                    if(num + TASK_SIZE * (i + 1) == NUM_END) {
//                        chk = true;
//                        break;
//                    }
//                }
//                if (chk) break;
//                num += TASK_SIZE * NUM_THREADS;
//            }
//            long startTime = System.currentTimeMillis();
//            for(int[] ar : chk_nums) {
//                pc_static_cyclic cyclic = new pc_static_cyclic(ar);
//                cyclic.run();
//            }
//            long endTime = System.currentTimeMillis();




            ArrayList<Integer>[] chk_nums = new ArrayList[NUM_THREADS];
            for(int i = 0; i < NUM_THREADS; i++) {
                chk_nums[i] = new ArrayList<>();
            }
            int num = 0;
            boolean chk = false;
            while(true) {
                for(int i = 0; i < NUM_THREADS; i++) {
                    for(int j = 0; j < TASK_SIZE; j++) {
                        chk_nums[i].add(num + TASK_SIZE * i + j);
                    }
                    if(num + TASK_SIZE * (i + 1) == NUM_END) {
                        chk = true;
                        break;
                    }
                }
                if (chk) break;
                num += TASK_SIZE * NUM_THREADS;
            }

            long startTime = System.currentTimeMillis();
            for (ArrayList<Integer> ar : chk_nums) {
                pc_static_cyclic cyclics = new pc_static_cyclic(ar);
                cyclics.run();
            }
            long endTime = System.currentTimeMillis();


            System.out.println(NUM_THREADS + "threads took execution time of " + (endTime - startTime) + "ms");
            System.out.println("1..." + NUM_END + " prime# counter=" + static_cyclic_cnt.getCnt());
        }

//        System.out.println("Dynamic Method");
//        for(int k : thread_nums) { //pc_static_cyclic method
//            NUM_THREADS = k;
//            dynamic_cnt.setCnt(0);
//            long startTime = System.currentTimeMillis();
//            for (int i = 0; i < NUM_THREADS; i++) {
//                pc_dynamic dynamics = new pc_dynamic((i * TASK_SIZE));
//                dynamics.run();
//            }
//            long endTime = System.currentTimeMillis();
//            System.out.println(NUM_THREADS + "threads took execution time of " + (endTime - startTime) + "ms");
//            System.out.println("1..." + NUM_END + " of tasks size " + TASK_SIZE + " prime# counter=" + dynamic_cnt.getCnt());
//        }
    }
}
