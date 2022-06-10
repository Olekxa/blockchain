package templates;

public record PaymentForTransaction(Account receiver, long value) {

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
