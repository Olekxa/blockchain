package templates;

import utils.StringHashUtil;

import java.io.Serializable;
import java.util.Date;


public class Block implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Blockchain parent;
    private final int id;
    private final long timeStamp = new Date().getTime();;
    private final String prevBlockHash;
    private final String hash;
    private final long magicNumber;
    private final long minerNumber;
    private double genTime;

    public Block(Blockchain parent, long magicNumber, long minerNumber) {
        this.parent = parent;
        this.id = parent.getBlockCount() + 1;
        this.magicNumber = magicNumber;
        this.minerNumber = minerNumber;

        if(this.id == 1) {
            this.prevBlockHash = "0";
        } else {
            this.prevBlockHash = parent.getBlock(id - 1).getHash();
        }

        this.hash = StringHashUtil.applySha256(this.toHash());
    }

    public void setGenTime(double time) {
        genTime = time;
    }

    public double getGenTime() {
        return genTime;
    }

    public String toHash() {
        return "Block:\n" +
                "Created by miner # " + minerNumber + "\n" +
                "Id: " + id + "\n" +
                "Timestamp: " + timeStamp + "\n" +
                "Magic number: " + magicNumber + "\n" +
                "Hash of the previous block:\n" +
                prevBlockHash + "\n";
    }

    String toValidate() {
        return "Block:\n" +
                "Created by miner # " + minerNumber + "\n" +
                "Id: " + id + "\n" +
                "Timestamp: " + timeStamp + "\n" +
                "Magic number: " + magicNumber + "\n" +
                "Hash of the previous block:\n";
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return toHash() + "Hash of the block:\n" + hash + "\n" +
                "Block was generating for " + String.format("%.2f", genTime) + " seconds";
    }
}