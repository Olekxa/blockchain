package templates;

import utils.SerializationUtil;
import utils.StringHashUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Blockchain implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Block> blockchain;
    private int blockCount = 0;
    private int zeroCount = 0;
    private String filename;

    public Blockchain(String filename) {
        blockchain = new ArrayList<>();
        this.filename = filename;
    }

    public Block getBlock(int id) {
        return blockchain.get(id - 1);
    }

    public void addBlock(Block b) throws IOException {
        if (validateAdd(b)) {
            this.blockchain.add(b);
            blockCount++;

            SerializationUtil.serialize(this, filename);
        }
    }

    public void setZeroCount(int zeroCount) {
        this.zeroCount = zeroCount;
    }

    public int getZeroCount() {
        return zeroCount;
    }

    private String getHash(String toHash) {
        return StringHashUtil.applySha256(toHash);
    }

    public int getBlockCount() {
        return blockCount;
    }

    public boolean validate() {
        for (int i = 1; i < blockchain.size(); i++) {
            String prevBlockHash = blockchain.get(i - 1).getHash();
            String currBlockHash = blockchain.get(i).getHash();

            String currBlockToVerify = blockchain.get(i).toValidate() + prevBlockHash + "\n";

            if (!currBlockHash.equals(getHash(currBlockToVerify))) {
                return false;
            }
        }
        return true;
    }

    private boolean validateAdd(Block b) {
        if (blockCount == 0 || blockCount == 1) {
            return true;
        }

        String newBlockHash = b.getHash();
        String lastBlockHash = getBlock(blockCount).getHash();

        String blockToVerify = b.toValidate() + lastBlockHash + "\n";

        if (newBlockHash.equals(getHash(blockToVerify))) {
            return true;
        } else {
            return false;
        }
    }
}
