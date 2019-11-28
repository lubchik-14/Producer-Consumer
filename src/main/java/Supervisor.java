
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * The task, which observes a shared queue in the producer/consumer design pattern.
 */
public class Supervisor implements Runnable, Producer.Observer, Consumer.Observer {
    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(1);
    private final Collection<Item> queue;

    private AtomicInteger producedCount = new AtomicInteger(0);
    private AtomicInteger consumedCount = new AtomicInteger(0);
    private int lastProducedCount;
    private int lastConsumedCount;

    /**
     * Creates an instance of the class.
     */
    public Supervisor(Collection<Item> queue) {
        this.queue = queue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            printStatistics();

            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Prints various statistic information about the producer/consumer system.
     */
    private void printStatistics() {
        final int producedCount = this.producedCount.get();
        final int consumedCount = this.consumedCount.get();

        final int diffProducedCount = producedCount - lastProducedCount;
        final int diffConsumedCount = consumedCount - lastConsumedCount;

        lastProducedCount = producedCount;
        lastConsumedCount = consumedCount;

        final int queueSize;
        final Stream<Item> itemStream;
        synchronized (queue) {
            queueSize = queue.size();
            itemStream = queue.stream();
        }

        System.out.printf("Pending count: %s\n", queueSize);
//        itemStream.forEach(item -> System.out.println("\tItem : " + item.value));
        System.out.println("Producing speed: " + diffProducedCount + " i/s");
        System.out.println("Consuming speed: " + diffConsumedCount + " i/s");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemProduce(Item item) {
        producedCount.incrementAndGet();
//        System.out.println("\u001B[31m" + "\tProduced : " + item.value + "\u001B[0m");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemConsume(Item item) {
        consumedCount.incrementAndGet();
//        System.out.println("\u001B[36m" + "\tConsumed : " + item.value + "\u001B[0m");
    }
}

