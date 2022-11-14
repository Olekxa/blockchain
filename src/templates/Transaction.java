package templates;

import java.security.PublicKey;

public class Transaction {
    private TransactionBasis transactionBasis;
    private final long transactionId;
    private final byte[] signature;
    private final PublicKey publicKey;

    public Transaction(TransactionBasis transactionBasis, long transactionId, byte[] signature, PublicKey publicKey) {
        this.transactionBasis = transactionBasis;
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

    public TransactionBasis getTransactionBasis() {
        return transactionBasis;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "sender=" + transactionBasis.getSender().getName() +
                ", receiver=" + transactionBasis.getReceiver().getName() +
                ", value=" + transactionBasis.getValue() +
                ", transactionId=" + transactionId +
                '}';
    }
}
