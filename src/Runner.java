import templates.Account;
import templates.Blockchain;
import templates.Config;
import templates.Miner;
import utils.SerializationUtil;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Runner {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {
        int numberOfThreads;

        if (args != null && args.length > 0) {
            try {
                numberOfThreads = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Number of threads must be in numeric format");
                return;
            }
        } else {
            numberOfThreads = Config.MIN_THREADS;
        }

        File file = new File(Config.FILE_PATH);
        if (file.exists() && !file.isDirectory()) {
            try {
                Blockchain importedBlockChain = (Blockchain) SerializationUtil.deserialize(file.getName());
                Blockchain.getInstance().setBlocksList(importedBlockChain.getBlocksList());
            } catch (IOException e) {
                System.out.println("File was not found: " + file.getName());
                return;
            } catch (Exception e) {
                System.out.println("Something went wrong: " + e.getLocalizedMessage());
                return;
            }
        }

        List<Account> accounts = Account.generateMockedUsers();

        for (Account account : accounts) {
            Blockchain.getInstance().registerAccount(account);
        }

        Collection<Thread> mine = generateMinersThreads(numberOfThreads);

        Collection<Account> listOfAccounts = Blockchain.getInstance().getRegisteredAccounts();

        Collection<Thread> listOfAccountThreads = generateAccountThreads(listOfAccounts);

        for (int i = 1; i <= numberOfThreads; i++) {
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

    private static Collection<Thread> generateMinersThreads(int numberOfThreads) throws NoSuchAlgorithmException {
        Collection<Thread> mine = new ArrayList<>();
        for (int i = 1; i <= numberOfThreads; i++) {
            Thread miner = new Thread(new Miner(i));
            miner.start();
            mine.add(miner);
        }
        return mine;
    }

    private static Collection<Thread> generateAccountThreads(Collection<Account> listOfAccounts) {
        Collection<Thread> listOfAccountThreads = new ArrayList<>();
        for (Account account : listOfAccounts) {
            Thread temp = new Thread(account);
            listOfAccountThreads.add(temp);
            temp.start();
        }
        return listOfAccountThreads;
    }
}

