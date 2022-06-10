package templates;

public class PaymentForTransaction {
    private final Account receiver;
    private final long value;

    public PaymentForTransaction(Account receiver, long value) {
        this.receiver = receiver;
        this.value = value;
    }

    public Account getReceiver() {
        return receiver;
    }

    public long getValue() {
        return value;
    }

    public String buildMessage() {
        return this.receiver.getName() + " gets " + this.value + " VC";
    }
}
