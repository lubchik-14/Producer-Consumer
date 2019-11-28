import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * The task, which produces items to a shared queue, based on a
 * random numbers generator.
 */
public class Producer implements Runnable {
    private static final long MIN_BASE_INTERVAL = TimeUnit.MILLISECONDS.toMillis(100);
    private static final long MAX_BASE_INTERVAL = TimeUnit.MILLISECONDS.toMillis(300);
    private Queue<Item> queue;
    private Observer observer;

    /**
     * Creates an instance of the class.
     *
     * @throws NullPointerException If any of the parameters are null.
     */
    public Producer(Queue<Item> queue, Observer observer) {
        if (queue == null) {
            throw new NullPointerException("queue is null");
        }

        if (observer == null) {
            throw new NullPointerException("observer is null");
        }

        this.queue = queue;
        this.observer = observer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                final Item producedItem = produceItem();

                while (!queue.offer(producedItem)) {
                    Thread.yield();
                }
                observer.onItemProduce(producedItem);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private Item produceItem() throws InterruptedException {
        long workInterval = (long) (MIN_BASE_INTERVAL + Math.random() * (MAX_BASE_INTERVAL - MIN_BASE_INTERVAL));
        Thread.sleep(workInterval);
        return new Item(workInterval);
    }

    /**
     * The observer interface which the producer uses to notify about complete work.
     */
    public interface Observer {
        /**
         * Called when an item has been produced by a producer.
         */
        void onItemProduce(Item item);
    }
}
