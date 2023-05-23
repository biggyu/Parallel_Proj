#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <omp.h>
const long NUM_STEPS = 10000000;
const double PI = 3.141592653589793238462643;
typedef struct value {
	double exc_time;
	double pi_value;
} values;
values piCalc(int schedule_type, int chunk_size, int thread_num) {
    values v;
    int i;
	double x, sum = 0, step = 1.0 / (double) NUM_STEPS, start_time = omp_get_wtime();
#pragma omp parallel num_threads(thread_num)
	if (schedule_type == 1) { // static
#pragma omp for private(i, x) schedule(static, chunk_size) reduction(+:sum)
		for (i = 0; i < NUM_STEPS; i++) {
            x = (i + 0.5) * step;
			sum += 4.0 / (1.0 + x * x);
		}
	} else if (schedule_type == 2) { // dynamic
#pragma omp for private(i) schedule(dynamic, chunk_size) reduction(+:sum)
		for (i = 0; i < NUM_STEPS; i++) {
            x = (i + 0.5) * step;
			sum += 4.0 / (1.0 + x * x);
		}
	} else if (schedule_type == 3) { // guided
#pragma omp for private(i) schedule(guided, chunk_size) reduction(+:sum)
		for (i = 0; i < NUM_STEPS; i++) {
            x = (i + 0.5) * step;
			sum += 4.0 / (1.0 + x * x);
		}
	}
	double end_time = omp_get_wtime();
	v.exc_time = (end_time - start_time) * 1000;
	v.pi_value = sum * step;
	return v;
}
int main(int argc, char *argv[]) {
	if (argc == 4) {
		int thread_num = atoi(argv[3]), chunk_size = atoi(argv[2]), schedule_type = atoi(argv[1]);
		values result = piCalc(schedule_type, chunk_size, thread_num);
		printf("environment: %d threads, %s scheduling type, chunk size %d\n", thread_num, schedule_type == 1 ? "static" : schedule_type == 2 ? "dynamic" : "guided", chunk_size);
		printf("pi=%.24lf in %fms\n", result.pi_value, result.exc_time);
	} else if (argc == 1) {
		int thread_nums[] = {1, 2, 4, 6, 8, 10, 12, 14, 16};
        int chunk_nums[] = {1, 5, 10, 100};
        double pi_error[3 * (sizeof(chunk_nums) / sizeof(chunk_nums[0]))][sizeof(thread_nums) / sizeof(thread_nums[0])];
		FILE *fp;
		fp = fopen("prob2_result.csv", "w+");
		fprintf(fp, ",");
		fprintf(fp, "chunk size,");
		for (int i = 0; i < sizeof(thread_nums) / sizeof(thread_nums[0]); i++) {
			char tmp[20];
			itoa(thread_nums[i], tmp, 10);
			fprintf(fp, strcat(tmp, ","));
		}
		fprintf(fp, "\n");
		for(int j = 0; j < 4; j++) {
            for(int i = 0; i < 3; i++) {
			    fprintf(fp, i == 0 ? "static," : i == 1 ? "dynamic," : "guided,");

                char tmp[20];
                itoa(chunk_nums[j], tmp, 10);
                fprintf(fp, strcat(tmp, ","));

                for (int k = 0; k < sizeof(thread_nums) / sizeof(thread_nums[0]); k++) {
                    values result = piCalc(i + 1, chunk_nums[j], thread_nums[k]);
                    char tmp[20];
                    sprintf(tmp, "%f", result.exc_time);
                    fprintf(fp, strcat(tmp, "ms,"));
                    pi_error[3*j + i][k] = result.pi_value - PI;
                    printf("pi error = %.24lf\n", result.pi_value - PI);
                }
                fprintf(fp, "\n");
            }
		}
        fprintf(fp, "\n\n");
        for(int i = 0; i < sizeof(pi_error) / sizeof(pi_error[0]); i++) {
        fprintf(fp, ",,");
            for(int j = 0; j < sizeof(pi_error[0]) / sizeof(pi_error[0][0]); j++) {
                char tmp[20];
                sprintf(tmp, "%.24f", pi_error[i][j]);
                fprintf(fp, strcat(tmp, ","));
            }
            fprintf(fp, "\n");
        }
	} else {
		printf("prob1.c needs 3 input parameters: ./a scheduling_type# chunk_size #_of_thread\n");
		printf("scheduling_type#:\n1:static\n2:dynamic\n3:guided\n");
		printf("\nchunk_size: 1, 5, 10, 100\n");
		printf("\n#_of_thread: 1, 2, 4, 6, 8, 10, 12, 14, 16");
    }
}

// #include <omp.h>
// #include <stdio.h>
// long num_steps = 10000000; 
// double step;
// int main(void) {
// 	long i; double x, pi, sum = 0.0;
// 	double start_time, end_time;

// 	start_time = omp_get_wtime();
// 	step = 1.0/(double) num_steps;
// 	for (i=0;i< num_steps; i++){
// 		x = (i+0.5)*step;
// 		sum = sum + 4.0/(1.0+x*x);
// 	}
// 	pi = step * sum;
// 	end_time = omp_get_wtime();
// 	double timeDiff = end_time - start_time;
//         printf("Execution Time : %lfms\n", timeDiff);

// 	printf("pi=%.24lf\n",pi);
// }