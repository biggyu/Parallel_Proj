
public class mm_static_block implements Runnable {
    int[][] mat1, mat2, matMul;
    public mm_static_block(int[][] mat1, int[][] mat2) {
        this.mat1 = mat1;
        this.mat2 = mat2;
        matMul = new int[mat1.length][mat2[0].length];
    }
    @Override
    public void run() {
//        if(this.mat1.length == 0) return new int[0][0];
        if(this.mat1[0].length == this.mat2.length) { //invalid dims
            for(int i = 0; i < mat1.length; i++){
                for(int j = 0; j < mat2[0].length; j++){
                    for(int k = 0; k < mat1[0].length; k++){
                        this.matMul[i][j] += mat1[i][k] * mat2[k][j];
                    }
                }
            }
        }
    }
    public int[][] getMatMul() {
        return this.matMul;
    }
}