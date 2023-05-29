#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <omp.h>
#include <math.h>

#define SPHERE_NUM 20
#define rnd( x ) (x * rand() / RAND_MAX)
#define INF 2e10f
#define DIM 2048
int thread_num;
typedef struct Sphere {
    float   r,b,g;
    float   radius;
    float   x,y,z;
    float hit( float ox, float oy, float *n ) {
        float dx = ox - x;
        float dy = oy - y;
        if (pow(dx, 2) + pow(dy, 2) < pow(radius, 2)) {
            float dz = sqrtf(pow(radius, 2) - pow(dx, 2) - pow(dy, 2));
            *n = dz / sqrtf(pow(radius, 2));
            return dz + z;
        }
        return -INF;
    }
} Sphere;
double openmp_ray(Sphere* s, unsigned char* ptr) {
	int i, j, k;
	// float rgb[4] = {0, 0, 0, 1}, maxz = -INF;
	double start_time = omp_get_wtime();
	// printf("%d\n", DIM);
#pragma omp parallel num_threads(thread_num) private(i, j, k) shared(ptr)
#pragma omp for collapse(2)
	for(i = 0; i < DIM; i++) {
		for(j = 0; j < DIM; j++) {
			float ox = (i - DIM / 2), oy = (j - DIM / 2), maxz = -INF;
			float rgb[4] = {0, 0, 0, 1};
			for(k = 0; k < SPHERE_NUM; k++) {
				float n, t = s[k].hit(ox, oy, &n);
				if (t > maxz) {
					float fscale = n;
					rgb[0] = s[k].r * fscale;
					rgb[1] = s[k].g * fscale;
					rgb[2] = s[k].b * fscale;
					maxz = t;
				}
			}
			
			for (k = 0; k < 4; k++) {
				int offset = i + j * DIM;
				ptr[offset * 4 + k] = (int)(rgb[k] * 255);
				// ptr[offset * 4 + 0] = (int)(r * 255);
				// ptr[offset * 4 + 1] = (int)(g * 255);
				// ptr[offset * 4 + 2] = (int)(b * 255);
				// ptr[offset * 4 + 3] = 255;
			}
		}
	}
// #pragma omp barrier
// #pragma omp for collapse(3)
// 	for(i = 0; i < DIM; i++) {
// 		for(j = 0; j < DIM; j++) {
// 			for (k = 0; k < 4; k++) {
// 				int offset = i + j * DIM;
// 				ptr[offset * 4 + k] = (int)(rgb[k] * 255);
// 				// ptr[offset * 4 + 0] = (int)(r * 255);
// 				// ptr[offset * 4 + 1] = (int)(g * 255);
// 				// ptr[offset * 4 + 2] = (int)(b * 255);
// 				// ptr[offset * 4 + 3] = 255;
// 			}
// 		}
// 	}
	return omp_get_wtime() - start_time;
}
void ppm_write(unsigned char* bitmap, int xdim,int ydim, FILE* fp) {
	int i,x,y;
	fprintf(fp,"P3\n");
	fprintf(fp,"%d %d\n",xdim, ydim);
	fprintf(fp,"255\n");
	for (y = 0; y < ydim; y++) {
		for (x = 0; x < xdim; x++) {
			i = x + y * xdim;
			fprintf(fp,"%d %d %d ", bitmap[4*i], bitmap[4*i + 1], bitmap[4*i + 2]);
		}
		fprintf(fp,"\n");
	}
}
int main(int argc, char *argv[]) {
	if (argc == 2) {
		thread_num = atoi(argv[1]);
		FILE* rp = fopen("result.ppm","w");
		Sphere *spheres = (Sphere *)malloc(sizeof(Sphere) * SPHERE_NUM);
		for (int i = 0; i < SPHERE_NUM; i++) {
			spheres[i].r = rnd(1.0f);
			spheres[i].g = rnd(1.0f);
			spheres[i].b = rnd(1.0f);
			spheres[i].x = rnd(2000.0f) - 1000;
			spheres[i].y = rnd(2000.0f) - 1000;
			spheres[i].z = rnd(2000.0f) - 1000;
			spheres[i].radius = rnd(200.0f) + 40;
		}
		unsigned char* bitmap = (unsigned char *)malloc(sizeof(unsigned char) * DIM * DIM * 4);
		
		double exc_time = openmp_ray(spheres, bitmap);
		
		// for (int i = 0; i < DIM; i++)
		// 	for (int j = 0; j < DIM; j++)
		// 		kernel(x, y, temp_s, bitmap);
		ppm_write(bitmap, DIM, DIM, rp);

		fclose(rp);
		free(bitmap);
		free(spheres);

		printf("Openmp (%d threads) ray tracing: %lf sec\n", thread_num, exc_time);
		printf("[result.ppm] generated\n");
	} else {
		// printf("prob1.c needs 1 input parameter: ./a #_of_thread\n");
		// printf("\n#_of_thread: 1, 2, 4, 6, 8, 10, 12, 14, 16, 32\n");
		int thread_nums[] = {1, 2, 4, 6, 8, 10, 12, 14, 16, 32, 64};
		Sphere *spheres = (Sphere *)malloc(sizeof(Sphere) * SPHERE_NUM);
		for (int i = 0; i < SPHERE_NUM; i++) {
			spheres[i].r = rnd(1.0f);
			spheres[i].g = rnd(1.0f);
			spheres[i].b = rnd(1.0f);
			spheres[i].x = rnd(2000.0f) - 1000;
			spheres[i].y = rnd(2000.0f) - 1000;
			spheres[i].z = rnd(2000.0f) - 1000;
			spheres[i].radius = rnd(200.0f) + 40;
		}
		unsigned char* bitmap;
		FILE* rp, *fp = fopen("prob1_result.csv","w");
		fprintf(fp, "\n,thread num,");
		for (int i = 0; i < sizeof(thread_nums) / sizeof(thread_nums[0]); i++) {
			char tmp[20];
			itoa(thread_nums[i], tmp, 10);
			fprintf(fp, strcat(tmp, ","));
		}
		fprintf(fp, "\n,execution time,");
		for(int i = 0; i < sizeof(thread_nums) / sizeof(thread_nums[0]); i++) {
			char tmp[20];
			itoa(thread_nums[i], tmp, 10);
			strcat(tmp, "_result.ppm");
			rp = fopen(tmp,"w");
			bitmap = (unsigned char *)malloc(sizeof(unsigned char) * DIM * DIM * 4);
			thread_num = thread_nums[i];
			double exc_time = openmp_ray(spheres, bitmap);
			
			sprintf(tmp, "%lf", exc_time);
			fprintf(fp, strcat(tmp, ","));
			printf("%d threads took %lf sec\n", thread_num, exc_time);
			ppm_write(bitmap, DIM, DIM, rp);
		}
		fprintf(fp, "\n,");

		fclose(rp);
		fclose(fp);
		free(bitmap);
		free(spheres);
	}
}


//g++ -fopenmp -o a openmp_ray.cpp
//./a result.ppm