
public class mm_static_block implements Runnable {
    int[][] mat1, mat2;
    public mm_static_block(int[][] mat1, int[][] mat2) {
        this.mat1 = mat1;
        this.mat2 = mat2;
    }
    @Override
    public void run() {
        this.multMatrix(this.mat1, this.mat2);
    }
    private int[][] multMatrix(int mat1[][], int mat2[][]){
        if(mat1.length == 0) return new int[0][0];
        if(mat1[0].length != mat2.length) return null; //invalid dims

        int result[][] = new int[mat1.length][mat2[0].length];

        for(int i = 0; i < mat1.length; i++){
            for(int j = 0; j < mat2[0].length; j++){
                for(int k = 0; k < mat1[0].length; k++){
                    result[i][j] += mat1[i][k] * mat2[k][j];
                }
            }
        }
        return result;
    }
}