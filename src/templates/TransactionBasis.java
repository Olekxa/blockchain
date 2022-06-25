package templates;

public class TransactionBasis {

    private final Account sender;
    private final Account receiver;
    private final long value;

    public TransactionBasis(Account sender, Account receiver, long value) {
        this.sender = sender;
        this.receiver = receiver;
        this.value = value;
    }

    public Account getSender() {
        return sender;
    }

    public Account getReceiver() {
        return receiver;
    }

    public long getValue() {
        return value;
    }

    public String buildMessage() {
        return this.sender.getName() +
                " sent " +
                this.value +
                " VC to " +
                this.receiver.getName();
    }
}
