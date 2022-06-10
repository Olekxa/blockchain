package templates;

public class IdCounter {
    private static long blockCounter = 0;
    private static int threadCounter = 0;
    private static long transactionCounter = 0;

    private IdCounter() {}

    public static long generateBlockId() {
        return (++blockCounter);
    }

    public static int generateThreadId() {
        return (++threadCounter);
    }

    public static long generateTransactionCounter() {
        return (++transactionCounter);
    }
}
