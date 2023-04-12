import java.io.*;
import java.util.stream.Stream;

public class prob2_main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(
                new FileReader("thread_num.txt") //read thread_num.txt
        );
        int[] thread_nums = Stream.of(reader.readLine().split(",")).mapToInt(Integer::parseInt).toArray();
        reader.close();

        reader = new BufferedReader(
                new FileReader("mat1000.txt")
        );
        int[][] mat1 = new int[Integer.parseInt(reader.readLine().split(" ")[0])][]; //transfer txt to matrix1
        for(int i = 0; i < mat1.length; i++) {
            mat1[i] =  Stream.of(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        }
        int[][] mat2 = new int[Integer.parseInt(reader.readLine().split(" ")[0])][]; //transfer txt to matrix2
        for(int i = 0; i < mat2.length; i++) {
            mat2[i] =  Stream.of(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        }

//        int[][] result = new int[mat1.length][mat2[0].length]; //result of matrix multiplication of mat1 & mat2
        for(int n : thread_nums) { //static block method using each thread number in thread_num.txt
            int[] submat_size = new int[n]; //height of submat1 & width of submat2
            int totalSum = 0; //total sum of result matrix
            long totalTime = 0; // total execution time of each thread
            mm_static_block[] blocks = new mm_static_block[n]; //thread array
            for(int i = 0; i < n; i++) {
                submat_size[i] = mat1.length / n; // width/height size for static block method
            }
            for(int i = 0; i < mat1.length % n; i++) {
                submat_size[i]++; // add modular for load distribution
            }
            int x_pnt = 0;
            for(int i = 0; i < n; i++) {
                int[][] submat1 = new int[submat_size[i]][mat1[0].length];
                for(int j = 0; j < submat1.length; j++) {
                    for(int k = 0; k < mat1[0].length; k++) {
                        submat1[j][k] = mat1[x_pnt + j][k];
                    }
                }
                int y_pnt = 0;
                for(int j = 0; j < n; j++) {
                    int[][] submat2 = new int[mat2.length][submat_size[j]];
                    for(int k = 0; k < submat2.length; k++) {
                        for(int m = 0; m < submat_size[j]; m++) {
                            submat2[k][m] = mat2[k][y_pnt + m];
                        }
                    }

                    long startTime = System.currentTimeMillis();
                    blocks[j] = new mm_static_block(submat1, submat2);
                    blocks[j].run();
                    long endTime = System.currentTimeMillis();
                    System.out.print(endTime - startTime + "ms "); //thread execution time
                    totalTime += endTime - startTime;
                    totalSum += matSum(blocks[j].getMatMul());
//                    //matrix multiplication is deactivated since it's not needed
//                    int[][] subResult = blocks[j].getMatMul();
//                    for(int k = 0; k < submat_size[i]; k++) {
//                        for(int m = 0; m < submat_size[j]; m++) {
//                            result[x_pnt + k][y_pnt + m] = subResult[k][m];
//                        }
//                    }
                    y_pnt += submat_size[j]; // move y_pnt
                }
                System.out.println();
                x_pnt += submat_size[i]; // move x_pnt
            }
            System.out.println(n + "threads took execution time of " + totalTime + "ms");
            System.out.println("matrix size " + mat1.length + ", " + mat1[0].length + " multiplication sum " + totalSum);
        }
        System.out.println(matSum(MatmultD.multMatrix(mat1, mat2))); // result of matrix_multiplication w/o parallel prg
    }
    public static int matSum(int mat[][]) {
        int sum = 0;
        for(int[] ar : mat) {
            for(int i : ar) {
                sum += i;
            }
        }
        return sum;
    }
}
