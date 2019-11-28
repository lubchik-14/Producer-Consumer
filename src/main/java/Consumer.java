
import java.util.Queue;

public class Consumer implements Runnable {
    private Queue<Item> queue;
    private Observer observer;

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

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Item itemToConsume = queue.poll();
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

    public interface Observer {
        void onItemConsume(Item item);
    }
}
