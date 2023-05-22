#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include <time.h>
#include <string.h>
#include <omp.h>
const int NUM_END = 200000;
bool isPrime(int x) {
	if (x <= 1)
		return false;
	for (int i = 2; i <= (int)sqrt(x); i++)	{
		if (x % i == 0)
			return false;
	}
	return true;
}
int getPrime(int schedule_type, int thread_num) {
	int sum = 0, i;
	printf("%d %d\n", schedule_type, thread_num);
	clock_t start_time = clock();
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
	clock_t end_time = clock();
	printf("%dms time elapsed in ", end_time - start_time);
	return sum;
}
int main(int argc, char *argv[]) {
	if (argc == 2) {
		int thread_nums[] = {1, 2, 4, 6, 8, 10, 12, 14, 16};
		int schedule_type = atoi(argv[1]);
		for (int i = 0; i < sizeof(thread_nums) / sizeof(thread_nums[0]); i++) {
			int result = getPrime(schedule_type, thread_nums[i]);
			printf("%d threads, %s scheduling type, chunk size %s envinronment\n", thread_nums[i], schedule_type % 2 == 0 ? "dynamic" : "static", schedule_type < 3 ? "default" : "10");
			printf("1... %d has %d prime #\n", NUM_END, result);
		}
	} else if (argc == 3) {
		int thread_num = atoi(argv[2]), schedule_type = atoi(argv[1]);
		clock_t start_time = clock();
		int result = getPrime(schedule_type, thread_num);
		clock_t end_time = clock();
		printf("%d thread w/ %s scheduling type chunk size %s\n", thread_num, schedule_type % 2 == 0 ? "dynamic" : "static", schedule_type < 3 ? "default" : "10");
		printf("1... %d has %d prime #\n", NUM_END, result);
		// getPrime(atoi(argv[1]), atoi(argv[2]));
	} else {
		int thread_nums[] = {1, 2, 4, 6, 8, 10, 12, 14, 16};
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
				clock_t start_time = clock();
				int result = getPrime(i + 1, thread_nums[j]);
				clock_t end_time = clock();
				char tmp[20];
				itoa(end_time - start_time, tmp, 10);
				fprintf(fp, strcat(tmp, "ms,"));
			}
			fprintf(fp, "\n");
		}
		// printf("prob1.c needs at least 1 input parameters(prob1 scheduling_type# (#_of_thread))\n");
		// printf("scheduling_type#:\n1:static w/ default chunk\n");
		// printf("2:dynamic w/ default chunk\n3:static w/ chunk 10\n4:dynamic w/ chunk 10\n");
		// printf("\n#_of_thread: 1, 2, 4, 6, 8, 10, 12, 14, 16");
	}
}