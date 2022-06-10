import templates.Account;
import templates.Blockchain;
import templates.Miner;
import utils.SerializationUtil;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;

public class Runner {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException, NoSuchProviderException {

        try {
            Blockchain importedBlockChain = (Blockchain) SerializationUtil.deserialize("blockchain.txt");
            Blockchain.getInstance().setBlocksList(importedBlockChain.getBlocksList());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            return;
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
        Blockchain.getInstance();//todo
        for (Account account : accounts) {
            Blockchain.getInstance().registerAccount(account);
        }

        List<Account> listOfAccounts = Blockchain.getInstance().getRegisteredAccounts();
        Thread miner1 = new Thread(new Miner());
        Thread miner2 = new Thread(new Miner());
        Thread miner3 = new Thread(new Miner());
        Thread miner4 = new Thread(new Miner());

        List<Thread> listOfAccountThreads = new ArrayList<>();
        for (Account account : listOfAccounts) {
            Thread temp = new Thread(account);
            listOfAccountThreads.add(temp);
            temp.start();
        }

        miner1.start();
        miner2.start();
        miner3.start();
        miner4.start();

        while (Blockchain.getInstance().underConstruction()) {
        }

        for (Thread accountThread : listOfAccountThreads) {
            accountThread.join();
        }
    }
}
