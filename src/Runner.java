import templates.Account;
import templates.Blockchain;
import templates.Miner;
import utils.SerializationUtil;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Runner {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {
        String path = "blockchain.txt";
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            try {
                Blockchain importedBlockChain = (Blockchain) SerializationUtil.deserialize(path);
                Blockchain.getInstance().setBlocksList(importedBlockChain.getBlocksList());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }


        List<Account> accounts = List.of(
                new Account("Tom Catalin", true),
                new Account("Jakar Taleb", true),
                new Account("Tom Catalin", true),
                new Account("Jakar Taleb", true),
                new Account("Hybe Ernath", true),
                new Account("Worker1", false),
                new Account("Worker2", false),
                new Account("Worker3", false),
                new Account("Director1", false),
                new Account("CarPartsShop", false),
                new Account("ShoesShop", false),
                new Account("CarShop", true),
                new Account("FastFood", false)
        );
        for (Account account : accounts) {
            Blockchain.getInstance().registerAccount(account);
        }

        Collection<Thread> mine = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Thread miner = new Thread(new Miner(i));
            miner.start();
            mine.add(miner);
        }

        List<Account> listOfAccounts = Blockchain.getInstance().getRegisteredAccounts();

        List<Thread> listOfAccountThreads = new ArrayList<>();
        for (Account account : listOfAccounts) {
            Thread temp = new Thread(account);
            listOfAccountThreads.add(temp);
            temp.start();
        }
        for (int i = 1; i <= 4; i++) {
            Thread miner = new Thread(new Miner(i));
            miner.start();
        }

        for (Thread accountThread : listOfAccountThreads) {
            accountThread.join();
        }

        for (Thread thread : mine) {
            thread.join();
        }

    }
}

