package templates;

import utils.StringHashUtil;

import java.util.Date;

public class Block {

    private final Blockchain parent;
    private final int id;
    private final long timeStamp;
    private final String hashOfPrevious;
    private final String hashOfBlock;
    private final long magicNumber;
    private double genTime;

    public Block(Blockchain parent, long magicNumber) {
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

    public void setGenTime(double time) {
        genTime = time;
    }

    public String toHash() {
        return "Block:\n" +
                "Id: " + id + "\n" +
                "Timestamp: " + timeStamp + "\n" +
                "Magic number: " + magicNumber + "\n" +
                "Hash of the previous block:\n" +
                hashOfPrevious + "\n";
    }

    public String getHash() {
        return hashOfBlock;
    }

    @Override
    public String toString() {
        return toHash() + "Hash of the block:\n" + hashOfBlock + "\n" +
                "aBlock was generating for " + String.format("%.2f", genTime) + " seconds";
    }
}
