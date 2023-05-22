#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include <string.h>
#include <omp.h>
const int NUM_END = 200000;
typedef struct value {
	double exc_time;
	int total_prime;
} values;
bool isPrime(int x) {
	if (x <= 1)
		return false;
	for (int i = 2; i <= (int)sqrt(x); i++)	{
		if (x % i == 0)
			return false;
	}
	return true;
}
values getPrime(int schedule_type, int thread_num) {
	values v;
	int sum = 0, i;
	// printf("%d %d\n", schedule_type, thread_num);
	double start_time = omp_get_wtime();
#pragma omp parallel num_threads(thread_num)
	if (schedule_type == 1) { // static w/ default chunk
#pragma omp for private(i) schedule(static)
		for (i = 0; i < NUM_END; i++) {
			// printf("%d %d\n", omp_get_thread_num(), i);
			if (isPrime(i + 1)) {
#pragma omp critical
				sum++;
			}
		}
	} else if (schedule_type == 2) { // dynamic w/ defualt chunk
#pragma omp for private(i) schedule(dynamic)
		for (i = 0; i < NUM_END; i++) {
			// printf("%d %d\n", omp_get_thread_num(), i);
			if (isPrime(i + 1))	{
#pragma omp critical
				sum++;
			}
		}
	} else if (schedule_type == 3) { // static w/ chunk 10
#pragma omp for private(i) schedule(static, 10)
		for (i = 0; i < NUM_END; i++) {
			// printf("%d %d\n", omp_get_thread_num(), i);
			if (isPrime(i + 1))	{
#pragma omp critical
				sum++;
			}
		}
	} else if (schedule_type == 4) { // dynamic w/ chunk 10
#pragma omp for private(i) schedule(dynamic, 10)
		for (i = 0; i < NUM_END; i++) {
			// printf("%d %d\n", omp_get_thread_num(), i);
			if (isPrime(i + 1))	{
#pragma omp critical
				sum++;
			}
		}
	}
	double end_time = omp_get_wtime();
	v.exc_time = (end_time - start_time) * 1000;
	v.total_prime = sum;
	return v;
}
int main(int argc, char *argv[]) {
	if (argc == 3) {
		int thread_num = atoi(argv[2]), schedule_type = atoi(argv[1]);
		double start_time = omp_get_wtime();
		values result = getPrime(schedule_type, thread_num);
		double end_time = omp_get_wtime();
		printf("%lfms time elapsed in %d thread w/ %s scheduling type chunk size %s\n", result.exc_time, thread_num,
		schedule_type % 2 == 0 ? "dynamic" : "static", schedule_type < 3 ? "default" : "10");
		printf("1... %d has %d prime #\n", NUM_END, result.total_prime);
		// getPrime(atoi(argv[1]), atoi(argv[2]));
	} else if(argc == 1) {
		int thread_nums[] = {1, 2, 4, 6, 8, 10, 12, 14, 16, 32};
		FILE *fp;
		fp = fopen("prob1_result.csv", "w+");
		fprintf(fp, ",");
		fprintf(fp, "chunk size,");
		for (int i = 0; i < sizeof(thread_nums) / sizeof(thread_nums[0]); i++) {
			char tmp[20];
			itoa(thread_nums[i], tmp, 10);
			fprintf(fp, strcat(tmp, ","));
		}
		fprintf(fp, "\n");
		for(int i = 0; i < 4; i++) {
			fprintf(fp, i % 2 == 0 ? "static," : "dynamic,");
			fprintf(fp, i < 2 ? "default," : "10,");
			for(int j = 0; j < sizeof(thread_nums) / sizeof(thread_nums[0]); j++) {
				values result = getPrime(i + 1, thread_nums[j]);
				char tmp[20];
				sprintf(tmp, "%f", result.exc_time);
				fprintf(fp, strcat(tmp, "ms,"));
				printf("%d primes\n", result.total_prime);
			}
			fprintf(fp, "\n");
		};
	} else {
		printf("prob1.c needs 2 input parameters: ./a scheduling_type# #_of_thread\n");
		printf("scheduling_type#:\n1:static w/ default chunk\n");
		printf("2:dynamic w/ default chunk\n3:static w/ chunk 10\n4:dynamic w/ chunk 10\n");
		printf("\n#_of_thread: 1, 2, 4, 6, 8, 10, 12, 14, 16");
	}
}