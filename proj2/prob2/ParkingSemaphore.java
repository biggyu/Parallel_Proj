import java.util.concurrent.Semaphore;

public class ParkingSemaphore {
    private static int PARKING_SPACE = 7, CAR_NUM = 10;
    public static void main(String[] args) {
//        BlockingQueue<Car> parkingGarage = new ArrayBlockingQueue<>(PARKING_SPACE);
        Semaphore parkingGarage = new Semaphore(PARKING_SPACE);
        for (int i =  1; i <= 10; i++) {
            SemaphoreCar c = new SemaphoreCar(parkingGarage, "Car " + i);
        }
        System.out.println("Garage using Semaphore");
    }
}
