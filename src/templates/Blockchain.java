package templates;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private static final long serialVersionUID = 1L;
    private final List<Block> blockchain;
    private int blockCount;
    private int hashZerosDelta = 0;
    private double timeSpent;

    public Blockchain() {
        blockchain = new ArrayList<>();
        this.blockCount = 0;
        this.timeSpent = 0;

    }

    private int countZeros() {
        if (blockCount == 0) {
            return 0;
        } else if (timeSpent < 10L) {
            return hashZerosDelta++;
        } else if (timeSpent > 20L) {
            return hashZerosDelta++;
        } else {
            return hashZerosDelta;
        }
    }

    public void addBlock() {
        StringBuilder match = new StringBuilder("0");
        match.append("0".repeat(Math.max(0, countZeros())));

        long startTime = System.nanoTime();
        long i = 0;

        if (hashZerosDelta == 0) {
            Block temp = new Block(this, 0);
            this.blockchain.add(temp);
            blockCount++;
            return;
        }

        while (true) {
            Block temp = new Block(this, i++);
            if (temp.getHash().substring(0, hashZerosDelta).equals(match.toString())) {

                long endTime = System.nanoTime();
                double genTime = (endTime - startTime) * 1e-9;
                temp.setGenTime(genTime);

                if (hashZerosDelta > 0) {
                    temp.toString().concat(String.format("N was increased to %d\n\n", hashZerosDelta));
                } else if (hashZerosDelta < 0) {
                    temp.toString().concat(String.format("N was decreased to %d\n\n", 1));
                } else {
                    temp.toString().concat(String.format("N stays the same\n\n"));
                }

                this.blockchain.add(temp);
                this.timeSpent = genTime;
                blockCount++;


                break;
            }
        }
    }

    public Block getBlock(int id) {
        return blockchain.get(id - 1);
    }

    public int getBlockCount() {
        return blockCount;
    }

    public int getHashZerosDelta() {
        return hashZerosDelta;
    }

    public void setHashZerosDelta(int hashZerosDelta) {
        this.hashZerosDelta = hashZerosDelta;
    }
}
