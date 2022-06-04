package templates;

import utils.SerializationUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedRunner {
    public static void run() {
        String filename = "blockchain.data";
        File blockchainDataFile = new File(filename);
        Blockchain blockchain = null;

        if(blockchainDataFile.exists() && blockchainDataFile.isFile()) {
            try {
                blockchain = (Blockchain) SerializationUtil.deserialize(filename);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }

        if(blockchain == null || !blockchain.validate()) {
            blockchain = new Blockchain(filename);
        }

        // seting initial value of zeros for hash string
        blockchain.setZeroCount(1);

        ExecutorService executor = Executors.newFixedThreadPool(8);

        double secondsThreshold = 30;

        for (int i = 0; i < 5; i++) {
            List<Miner> miners = makeMinerList(blockchain);
            try {
                blockchain.addBlock(executor.invokeAny(miners));
                System.out.println(blockchain.getBlock(blockchain.getBlockCount()));

                if (blockchain.getBlock(blockchain.getBlockCount()).getGenTime() >= secondsThreshold) {
                    blockchain.setZeroCount(blockchain.getZeroCount() - 1);
                    System.out.printf("N was decreased to %s\n\n", blockchain.getZeroCount());
                } else {
                    blockchain.setZeroCount(blockchain.getZeroCount() + 1);
                    System.out.printf("N was increased to %s\n\n", blockchain.getZeroCount());
                }

            } catch (InterruptedException | ExecutionException | IOException exc) {
                exc.printStackTrace();
            }
        }
        executor.shutdown();
    }

    private static List<Miner> makeMinerList(Blockchain currentBlockchain) {
        List<Miner> minerList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Miner miner = new Miner(i, currentBlockchain, currentBlockchain.getZeroCount());
            minerList.add(miner);
        }
        return minerList;
    }
}