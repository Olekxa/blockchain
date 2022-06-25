package templates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Block implements Serializable {

    private final long id;
    private final long timestamp;
    private final String hashOfPrevious;

    private int minerId;
    private PaymentForTransaction minerPaymentForTransaction;
    private final ArrayList<Transaction> data;
    private String hash;
    private long magicNum;
    private long timeToGenerate;
    private String zeroUpdateMessage;

    public Block(Block previous) {
        this.id = previous != null ? previous.getId() + 1 : 1;
        this.timestamp = new Date().getTime();
        this.hashOfPrevious = previous == null ? "0" : previous.getHash();
        this.minerId = 0;
        this.minerPaymentForTransaction = null;
        this.data = new ArrayList<>();
        this.hash = null;
        this.magicNum = 0L;
        this.timeToGenerate = 0L;
        this.zeroUpdateMessage = "";
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(String.format("""
                        Block:
                        Created by miner # %s
                        %s
                        Id: %s
                        Timestamp: %s
                        Magic number: %s
                        Hash of the previous block:
                        %s%nHash of the block:
                        %s%n""",
                this.getMinerId(), this.getMinerPaymentTransaction().buildMessage(), this.getId(), this.getTimestamp(), this.getMagicNum(), this.getHashOfPrevious(), this.getHash()));
        ret.append("Block data: ");
        if (this.data.isEmpty()) {
            ret.append("\nNo transactions");
        } else {
            for (Transaction data : this.getData()) {
                ret.append("\n");
                ret.append(data.getTransactionBasis().buildMessage());
            }
        }
        ret.append("\n");
        ret.append(String.format("Block was generating for %s seconds\n" + "%s%n", this.getTimeToGenerate(), this.getZeroUpdateMessage()));
        return ret.toString();
    }

    public synchronized void addTransaction(Transaction transaction) {
        this.data.add(transaction);
    }

    public String getZeroUpdateMessage() {
        return zeroUpdateMessage;
    }

    public int getMinerId() {
        return minerId;
    }

    public void setMinerId(int minerId) {
        this.minerId = minerId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getId() {
        return id;
    }

    public String getHash() {
        return hash;
    }

    public String getHashOfPrevious() {
        return hashOfPrevious;
    }

    public long getMagicNum() {
        return magicNum;
    }

    public long getTimeToGenerate() {
        return timeToGenerate;
    }

    public void setZeroUpdateMessage(String zeroUpdateMessage) {
        this.zeroUpdateMessage = zeroUpdateMessage;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public ArrayList<Transaction> getData() {
        return data;
    }

    public void setMagicNum(long magicNum) {
        this.magicNum = magicNum;
    }

    public void setTimeToGenerate(long timeToGenerate) {
        this.timeToGenerate = timeToGenerate;
    }


    public PaymentForTransaction getMinerPaymentTransaction() {
        return minerPaymentForTransaction;
    }

    public void setMinerPaymentTransaction(PaymentForTransaction minerPaymentForTransaction) {
        this.minerPaymentForTransaction = minerPaymentForTransaction;
    }
}