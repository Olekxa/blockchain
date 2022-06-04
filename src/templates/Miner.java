package templates;

import java.util.Random;
import java.util.concurrent.Callable;

public class Miner implements Callable<Block> {
    private final long minerNum;
    private final Blockchain blockchain;
    private final int zeroCount;

    public Miner(int number, Blockchain bc, int count) {
        minerNum = number;
        blockchain = bc;
        zeroCount = count;
    }

    @Override
    public Block call() throws Exception {
        String match = "0";
        for (int i = 1; i < zeroCount; i++) {
            match += "0";
        }

        long startTime = System.nanoTime();
        Random random = new Random();

        while(true) {
            Block temp = new Block(blockchain, random.nextLong(), minerNum);

            if(temp.getHash().substring(0, zeroCount).equals(match)) {
                long endTime = System.nanoTime();
                double genTime = (endTime - startTime) * 1e-9;
                temp.setGenTime(genTime);

                return temp;
            }
        }
    }
}