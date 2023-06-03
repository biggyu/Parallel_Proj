#include <thrust/random.h>
#include <thrust/device_vector.h>
#include <thrust/transform_reduce.h>
#include <thrust/sequence.h>
#include <stdio.h>
#include <math.h>
#include <time.h>
template <typename T>
struct square {

    double num_step = 1 << 30;

    __host__ __device__
    double operator()(const T& x) const {
        // printf("%f\n", 4 / pow(x, 2));
        double sum = 0;
        for(int i = 0; i < 4; i++ ) {
            double step = 1.0 / num_step;
            sum += 4.0 / (1 + pow(((x + i * (1 << 28)) + 0.5) * step, 2));
        }
        return sum;
    }
};
int main () {
    long num_step = 1 << 28;
    thrust::device_vector<long> X(num_step);

    square<double>         unary_op;
    thrust::plus<double>   binary_op;

    thrust::sequence(X.begin(), X.end());
    clock_t start_time = clock();
    double result = thrust::transform_reduce(X.begin(), X.end(), unary_op, (double) 0, binary_op);
    double exc_time = (double) clock() - start_time;
    printf("%lf\n", result / (double) (num_step << 2));
    printf("execution time: %lf sec\n", exc_time / 1000.0);

    return 0;
}