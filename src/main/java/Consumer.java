
import java.util.Queue;

/**
 * The task, which polls items from a shared queue, and consumes them.
 */
public class Consumer implements Runnable {
    private Queue<Item> queue;
    private Observer observer;

    /**
     * Creates an instance of the class.
     *
     * @throws NullPointerException If any of the parameters are null.
     */
    public Consumer(Queue<Item> queue, Observer observer) {
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
                final Item itemToConsume;
                synchronized (queue) {
                    itemToConsume = queue.poll();
                }
                if (itemToConsume != null) {
                    consumeItem(itemToConsume);
                    observer.onItemConsume(itemToConsume);
                } else {
                    Thread.yield();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void consumeItem(Item item) throws InterruptedException {
        long consumeInterval = (long) (Math.random() * item.value);
        Thread.sleep(consumeInterval);
    }

    /**
     * The observer interface which the consumer uses to notify about complete work.
     */
    public interface Observer {
        /**
         * Called when an item has consumed by a consumer.
         */
        void onItemConsume(Item item);
    }
}
