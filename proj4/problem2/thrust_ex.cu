#include <thrust/random.h>
#include <thrust/device_vector.h>
#include <thrust/transform_reduce.h>
#include <thrust/sequence.h>
#include <stdio.h>
#include <math.h>
#include <time.h>
template <typename T>
struct square {

    int num_step = 1 << 30;

    __host__ __device__
    double operator()(const T& x) const {
        double step = 1.0 / num_step;
        return 4.0 / (1 + pow((x + 0.5) * step, 2));
    }
};
int main () {
    int num_step = 1 << 30;
    thrust::device_vector<int> X(num_step);

    square<double>         unary_op;
    thrust::plus<double>   binary_op;

    thrust::sequence(X.begin(), X.end());
    clock_t start_time = clock();
    double result = thrust::transform_reduce(X.begin(), X.end(), unary_op, (double) 0, binary_op);
    double exc_time = (double) (clock() - start_time);
    printf("%lf\n", result / (double) num_step);
    printf("execution time: %lf sec\n", exc_time / 1000.0);

    return 0;
}