#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include <omp.h>
const int NUM_END = 200000;
bool isPrime(int x) {
    if (x <= 1) return false;
    for (int i = 2; i <= (int)sqrt(x); i++) {
        if (x % i == 0) return false;
    }
    return true;
}
int main(int argc, char* argv[]) {
    if(argc != 3) {
        printf("prob1.c needs 2 input parameters");
    } else {
        int thread_nums[] = {1, 2, 4, 6, 8, 10, 12, 14, 16}; //보고서 작성을 위해 모든 쓰레드로 실행

        int thread_num = atoi(argv[2]), schedule_type = atoi(argv[1]);
        printf("%d %d\n", schedule_type, thread_num);

        int sum = 0;
        for(int i = 1; i <= NUM_END; i++) {
            if(isPrime(i)) sum++;
        }
        printf("sum= %d\n", sum);
        // for(int i = 0; i < sizeof(thread_nums) / sizeof(thread_nums[0]); i++) {
            
        // }
    }
}
//compile with command below
//gcc prob1.c -lgomp -o a.exe