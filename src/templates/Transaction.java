package templates;

import java.security.PublicKey;

public class Transaction extends TransactionBasis {

    private final long transactionId;
    private final byte[] signature;
    private final PublicKey publicKey;

    public Transaction(TransactionBasis transactionBasis, long transactionId, byte[] signature, PublicKey publicKey) {
        super(transactionBasis.getSender(), transactionBasis.getReceiver(), transactionBasis.getValue());
        this.transactionId = transactionId;
        this.signature = signature;
        this.publicKey = publicKey;
    }

    public byte[] getSignature() {
        return signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public long getTransactionId() {
        return transactionId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "sender=" + getSender().getName() +
                ", receiver=" + getReceiver().getName() +
                ", value=" + getValue() +
                ", transactionId=" + transactionId +
                '}';
    }
}
