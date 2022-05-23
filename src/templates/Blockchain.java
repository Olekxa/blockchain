package templates;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private static final long serialVersionUID = 1L;
    private final List<Block> blockchain;
    private int blockCount;
    private final int numberOfZeros;

    public Blockchain(int numberOfZeros) {
        blockchain = new ArrayList<>();
        this.blockCount = 0;
        this.numberOfZeros = numberOfZeros;
    }

    public void addBlock(){
        StringBuilder match = new StringBuilder("0");
        match.append("0".repeat(Math.max(0, numberOfZeros - 1)));

        long startTime = System.nanoTime();
        long i = 0;

        if(numberOfZeros == 0) {
            Block temp = new Block(this, 0);
            this.blockchain.add(temp);
            blockCount++;
            return;
        }

        while(true) {
            Block temp = new Block(this, i++);
            if(temp.getHash().substring(0, numberOfZeros).equals(match.toString())) {

                long endTime = System.nanoTime();
                double genTime = (endTime - startTime) * 1e-9;
                temp.setGenTime(genTime);

                this.blockchain.add(temp);

                blockCount++;

                break;
            }
        }
    }

    public Block getBlock(int id) {
        return blockchain.get(id-1);
    }

    public int getBlockCount() {
        return blockCount;
    }
}
