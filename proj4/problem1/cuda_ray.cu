// #include <stdio.h>
// #include <stdlib.h>
// #include <math.h>
// #include <time.h>
// #define SPHERE_NUM 20
// #define rnd( x ) (x * rand() / RAND_MAX)
// #define INF 2e10f
// #define DIM 2048
// #define N (2048*2048)
// #define THREADS_PER_BLOCK 512
// typedef struct Sphere {
// 	float	r,g,b;
//     float   radius;
//     float   x,y,z;
//     __device__ float hit( float ox, float oy, float *n ) {
//         float dx = ox - x;
//         float dy = oy - y;
//         if (dx*dx + dy*dy < radius*radius) {
//             float dz = sqrtf( radius*radius - dx*dx - dy*dy );
//             *n = dz / sqrtf( radius * radius );
//             return dz + z;
//         }
//         return -INF;
//     }
// } Sphere;
// __global__ void cuda_ray(Sphere* s, unsigned char* ptr) {
// 	int ix = threadIdx.x + blockIdx.x * blockDim.x;
// 	int iy = threadIdx.y + blockIdx.y * blockDim.y;

// 	float ox = (ix - DIM / 2), oy = (iy - DIM / 2), maxz = -INF;
// 	float rgb[4] = {0, 0, 0, 1};

// 	for (int k = 0; k < SPHERE_NUM; k++) {
// 		float n, t = s[k].hit(ox, oy, &n);
// 		if (t > maxz) {
// 			float fscale = n;
// 			rgb[0] = s[k].r * fscale;
// 			rgb[1] = s[k].g * fscale;
// 			rgb[2] = s[k].b * fscale;
// 			maxz = t;
// 		}
// 	}

// 	int offset = ix + iy * DIM;
// 	for (int k = 0; k < 4; k++)	{
// 		ptr[offset * 4 + k] = (int)(rgb[k] * 255);
// 	}
// }
// void ppm_write(unsigned char* bitmap, int xdim,int ydim, FILE* fp) {
// 	int i,x,y;
// 	fprintf(fp,"P3\n");
// 	fprintf(fp,"%d %d\n",xdim, ydim);
// 	fprintf(fp,"255\n");
// 	for (y = 0; y < ydim; y++) {
// 		for (x = 0; x < xdim; x++) {
// 			i = x + y * xdim;
// 			fprintf(fp,"%d %d %d ", bitmap[4*i], bitmap[4*i + 1], bitmap[4*i + 2]);
// 		}
// 		fprintf(fp,"\n");
// 	}
// }
// int main(void) {
//     int sph_size = 4 * sizeof(Sphere) * SPHERE_NUM, bmp_size = 4 * sizeof(unsigned char) * DIM * DIM * 4;
// 	unsigned char* bitmap = (unsigned char *) malloc(bmp_size), *d_bitmap;
//     FILE* rp = fopen("result_cuda.ppm", "w");
// 	Sphere *spheres = (Sphere *)malloc(sph_size), *d_spheres;
// 	srand((unsigned int)time(NULL));
// 	for (int i = 0; i < SPHERE_NUM; i++) {
// 		spheres[i].r = rnd( 1.0f );
// 		spheres[i].g = rnd( 1.0f );
// 		spheres[i].b = rnd( 1.0f );
// 		spheres[i].x = rnd( 2000.0f ) - 1000;
// 		spheres[i].y = rnd( 2000.0f ) - 1000;
// 		spheres[i].z = rnd( 2000.0f ) - 1000;
// 		spheres[i].radius = rnd( 200.0f ) + 40;
// 	}
	
//     cudaMalloc((void **)&d_spheres, sph_size);
//     cudaMalloc((void **)&d_bitmap, bmp_size);

//     cudaMemcpy(d_spheres, spheres, sph_size, cudaMemcpyHostToDevice);
//     cudaMemcpy(d_bitmap, bitmap, bmp_size, cudaMemcpyHostToDevice);

// 	cudaEvent_t startTime, endTime;
// 	cudaEventCreate(&startTime);
// 	cudaEventCreate(&endTime);

// 	cudaEventRecord(startTime);
	
// 	dim3 blocks(1 << 6, 1 << 6, 1);
// 	dim3 threads(1 << 5, 1 << 5, 1);
// 	cuda_ray<<<blocks, threads>>>(d_spheres, d_bitmap);

//     cudaEventRecord(endTime);

// 	cudaEventSynchronize(endTime);
// 	float excTime = 0.0;
// 	cudaEventElapsedTime(&excTime, startTime, endTime);

//     cudaMemcpy(bitmap, d_bitmap, bmp_size, cudaMemcpyDeviceToHost);
//     ppm_write(bitmap, DIM, DIM, rp);

// 	cudaEventDestroy(startTime);
// 	cudaEventDestroy(endTime);
// 	fclose(rp);
// 	free(bitmap); free(spheres);
//     cudaFree(d_bitmap); cudaFree(d_spheres);
//     printf("%lfsec\n",  (double)excTime / 1000.0);
//     return 0;
// }

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
#pragma warning(disable: 4819)
#define SPHERE_NUM 20
#define rnd( x ) (x * rand() / RAND_MAX)
#define INF 2e10f
#define DIM 2048
typedef struct Sphere {
	float	rgb[3];
    float   radius;
    float   x,y,z;
    __device__ float hit( float ox, float oy, float *n ) {
        float dx = ox - x;
        float dy = oy - y;
        if (dx*dx + dy*dy < radius*radius) {
            float dz = sqrtf( radius*radius - dx*dx - dy*dy );
            *n = dz / sqrtf( radius * radius );
            return dz + z;
        }
        return -INF;
    }
} Sphere;
__global__ void cuda_ray(Sphere* s, unsigned char* ptr) {
	int ix = threadIdx.x + blockIdx.x * blockDim.x;
	int iy = threadIdx.y + blockIdx.y * blockDim.y;
	int iz = threadIdx.z + blockIdx.z * blockDim.z;
	
	float ox = (ix - DIM / 2), oy = (iy - DIM / 2), maxz = -INF;
	float rgb = iz != 3 ? 0 : 1;

	for (int k = 0; k < SPHERE_NUM; k++) {
		float n, t = s[k].hit(ox, oy, &n);
		if (t > maxz) {
			float fscale = n;
			rgb = s[k].rgb[iz] * fscale;
			maxz = t;
		}
	}

	ptr[(ix + iy * DIM) * 4 + iz] = (int)(rgb * 255);
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
int main(void) {
    unsigned int sph_size = 4 * sizeof(Sphere) * SPHERE_NUM, bmp_size = 4 * sizeof(unsigned char) * DIM * DIM * 4;
	unsigned char* bitmap = (unsigned char *) malloc(bmp_size), *d_bitmap;
	char file_name[20] = "result_cuda.ppm";
    FILE* rp = fopen(file_name, "w");
	Sphere *spheres = (Sphere *)malloc(sph_size), *d_spheres;
	srand((unsigned int)time(NULL));
	for (int i = 0; i < SPHERE_NUM; i++) {
		for(int j = 0; j < 3; j++) {
			spheres[i].rgb[j] = rnd(1.0f);
		}
		spheres[i].x = rnd( 2000.0f ) - 1000;
		spheres[i].y = rnd( 2000.0f ) - 1000;
		spheres[i].z = rnd( 2000.0f ) - 1000;
		spheres[i].radius = rnd( 200.0f ) + 40;
	}
	
    cudaMalloc((void **)&d_spheres, sph_size);
    cudaMalloc((void **)&d_bitmap, bmp_size);

    cudaMemcpy(d_spheres, spheres, sph_size, cudaMemcpyHostToDevice);
    cudaMemcpy(d_bitmap, bitmap, bmp_size, cudaMemcpyHostToDevice);

	cudaEvent_t startTime, endTime;
	cudaEventCreate(&startTime);
	cudaEventCreate(&endTime);

	cudaEventRecord(startTime);
	
	dim3 blocks(1 << 7, 1 << 7, 2);
	dim3 threads(1 << 4, 1 << 4, 2);
	cuda_ray<<<blocks, threads>>>(d_spheres, d_bitmap);
	
	cudaEventRecord(endTime);

	cudaEventSynchronize(endTime);
	
	float excTime = 0.0;
	cudaEventElapsedTime(&excTime, startTime, endTime);

    cudaMemcpy(bitmap, d_bitmap, bmp_size, cudaMemcpyDeviceToHost);
    ppm_write(bitmap, DIM, DIM, rp);

	cudaEventDestroy(startTime);
	cudaEventDestroy(endTime);
	fclose(rp);
	free(bitmap); free(spheres);
    cudaFree(d_bitmap); cudaFree(d_spheres);
    printf("CUDA ray tracing: %lfsec\n",  (double)excTime / 1000.0);
	printf("[%s] generated\n", file_name);
    return 0;
}