package templates;

import java.util.concurrent.atomic.AtomicLong;

public class IdCounter {
    private static AtomicLong transactionCounter = new AtomicLong(0);

    private IdCounter() {
    }

    public synchronized static long generateTransactionCounter() {
        return (transactionCounter.incrementAndGet());
    }
}
