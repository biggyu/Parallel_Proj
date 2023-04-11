import java.io.*;
import java.util.Arrays;
import java.util.stream.Stream;

public class prob2_main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(
                new FileReader("thread_num.txt") //NUM_THREAD is initialized by thread_num.txt
        );
        int[] thread_nums = Stream.of(reader.readLine().split(",")).mapToInt(Integer::parseInt).toArray();
        reader.close();

        reader = new BufferedReader(
                new FileReader("mat5.txt")
        );
        int[][] mat1 = new int[Integer.parseInt(reader.readLine().split(" ")[0])][];
        for(int i = 0; i < mat1.length; i++) {
            mat1[i] =  Stream.of(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        }
        int[][] mat2 = new int[Integer.parseInt(reader.readLine().split(" ")[0])][];
        for(int i = 0; i < mat2.length; i++) {
            mat2[i] =  Stream.of(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        }
        int n = 4;
        int[][] result = new int[mat1.length][mat2[0].length];
//        for(int n : thread_nums) { //static block method
            int[] submat_size = new int[n]; //height of submat1 & width of submat2
            for(int i = 0; i < n; i++) {
                submat_size[i] = mat1.length / n;
            }
            submat_size[n - 1] += mat1.length % n;
            int x_pnt = 0;
            for(int i = 0; i < n; i++) {
                int[][] submat1 = new int[submat_size[i]][mat1[0].length];
                for(int j = 0; j < submat1.length; j++) {
                    for(int k = 0; k < mat1[0].length; k++) {
                        submat1[j][k] = mat1[x_pnt + j][k];
                    }
                }
                int y_pnt = 0;
                int[][] submat2 = new int[mat2.length][submat_size[i]];
                for(int j = 0; j < submat_size.length; j++) {
                    for(int k = 0; k < submat1.length; k++) {
                        for(int m = 0; m < mat1[0].length; m++) {
                            submat2[m][k] = mat2[m][y_pnt + k];
                        }
                    }
                    y_pnt += submat_size[j];
                }
                x_pnt += submat_size[i];
            }
//        }

//        for(int[] ar : mat1) {
//            for(int i : ar) {
//                System.out.print(i + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();
//        for(int[] ar : mat2) {
//            for(int i : ar) {
//                System.out.print(i + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();
    }
}
