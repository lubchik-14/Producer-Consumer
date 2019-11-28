import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Queue<Item> queue = new LinkedBlockingDeque<>(50);
        final Supervisor supervisor = new Supervisor(queue);

        executorService.submit(new Consumer(queue, supervisor));

        executorService.submit(new Producer(queue, supervisor));
        executorService.submit(new Producer(queue, supervisor));
        executorService.submit(new Producer(queue, supervisor));
        executorService.submit(new Producer(queue, supervisor));
        executorService.submit(new Producer(queue, supervisor));


        executorService.submit(supervisor);


        executorService.shutdown();
    }
}
