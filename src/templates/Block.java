package templates;

import utils.StringHashUtil;

import java.util.Date;

public class Block {
    private final long minerId;
    private final Blockchain parent;
    private final int id;
    private final long timeStamp;
    private final String hashOfPrevious;
    private final String hashOfBlock;
    private final long magicNumber;
    private double genTime;

    public Block(Blockchain parent, long magicNumber) {
        this.minerId = Thread.currentThread().getId();
        this.parent = parent;
        this.id = parent.getBlockCount() + 1;
        this.timeStamp = new Date().getTime();
        this.magicNumber = magicNumber;

        if (this.id == 1) {
            this.hashOfPrevious = "0";
        } else {
            this.hashOfPrevious = this.parent.getBlock(this.id - 1).hashOfBlock;
        }

        this.hashOfBlock = StringHashUtil.applySha256(this.toHash());
    }

    public String toHash() {
        return "Block:\n" +
                "Created by miner # " + minerId + "\n" +
                "Id: " + id + "\n" +
                "Timestamp: " + timeStamp + "\n" +
                "Magic number: " + magicNumber + "\n" +
                "Hash of the previous block:\n" +
                hashOfPrevious + "\n";
    }

    @Override
    public String toString() {
        String n;
        if (parent.getHashZerosDelta() > 0) {
            n = String.format("N was increased to %d\n\n", parent.getHashZerosDelta());
        } else if (parent.getHashZerosDelta() < 0) {
            n = String.format("N was decreased to %d\n\n", 1);
        } else {
            n = String.format("N stays the same\n\n");
        }
        return toHash() + "Hash of the block:\n" + hashOfBlock + "\n" +
                "Block was generating for " + String.format("%.2f", genTime) + " seconds\n"
                + n;
    }

    public String getHash() {
        return hashOfBlock;
    }

    public void setGenTime(double time) {
        genTime = time;
    }
}
