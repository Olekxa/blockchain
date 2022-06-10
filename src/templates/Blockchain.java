package templates;

import java.io.Serial;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.UUID;

public class Blockchain implements Serializable {

    private static final long MINING_REWARD = 100;
    private static final int MAX_NUM_OF_BLOCKS = 15;
    private static final int MAX_NUM_OF_ZEROS = 4;

    private static final long THRESHOLD_SAME = 5L;
    private static final long THRESHOLD_MINUS = 10L;

    private static final String N_UPDATE_MINUS = "N was decreased by 1";
    private static final String N_UPDATE_SAME = "N stays the same";
    private static final String N_UPDATE_PLUS = "N was increased to ";

    @Serial
    private static final long serialVersionUID = 7L;


    private static volatile Blockchain instance;
    private static final List<Account> registeredAccounts = new ArrayList<>();
    private static Deque<Block> blocksList = new ArrayDeque<>();
    private static int numOfStartingZeros;
    private static Block blockIsCreating;

    private Blockchain() {
        numOfStartingZeros = 0;
        blockIsCreating = new Block(null);
    }

    public static Blockchain getInstance() {
        if (instance == null) {
            instance = new Blockchain();
        }
        return instance;
    }

    public synchronized long getFundsByAccountUUID(UUID uuid) {
        long ret = 0;
        for (Block block : blocksList) {
            if (block.getMinerPaymentTransaction().getReceiver().getUuid() == uuid) {
                ret += block.getMinerPaymentTransaction().getValue();
            }
            for (Transaction transaction : block.getData()) {
                if (transaction.getReceiver().getUuid() == uuid) {
                    ret += transaction.getValue();
                } else if (transaction.getSender().getUuid() == uuid) {
                    ret -= transaction.getValue();
                }
            }
        }
        for (Transaction transaction : blockIsCreating.getData()) {
            if (transaction.getReceiver().getUuid() == uuid) {
                ret += transaction.getValue();
            } else if (transaction.getSender().getUuid() == uuid) {
                ret -= transaction.getValue();
            }
        }
        return ret;
    }

    public synchronized void receiveFromAccount(Account senderAccount, Transaction transaction) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        if (!blocksList.isEmpty()) {
            if (validateTransaction(senderAccount, transaction)) {
                blockIsCreating.addTransaction(transaction);
            }
        }
    }

    private synchronized boolean validateTransaction(Account senderAccount, Transaction transaction) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(transaction.getPublicKey());
        byte[] signatureInput = (transaction.getTransactionId() + transaction.buildMessage() + senderAccount.getLedgerSignature()).getBytes();
        sig.update(signatureInput);
        List<Transaction> previousListOfTransactions = blocksList.getLast().getData();
        long maxTransactionId;
        maxTransactionId = (previousListOfTransactions.isEmpty()) ? 0 : Collections.max(previousListOfTransactions.stream().map(Transaction::getTransactionId).toList());
        return (sig.verify(transaction.getSignature())
                && transaction.getTransactionId() > maxTransactionId
                && senderAccount.getCapital() >= transaction.getValue()
        );
    }

    public synchronized void receiveFromMiner(Miner miner, String hash, long magicNum, long timeToGenerate) {
        if (hash.substring(0, numOfStartingZeros).matches("0*")) {
            concludeBlock(miner, hash, magicNum, timeToGenerate);
        }
    }

    private synchronized void concludeBlock(Miner miner, String hash, long magicNum, long timeToGenerate) {
        blockIsCreating.setMinerId(miner.getId());
        blockIsCreating.setHash(hash);
        blockIsCreating.setMagicNum(magicNum);
        blockIsCreating.setTimeToGenerate(timeToGenerate);
        if (blockIsCreating.getTimeToGenerate() > THRESHOLD_MINUS) {
            numOfStartingZeros--;
            blockIsCreating.setZeroUpdateMessage(N_UPDATE_MINUS);
        } else if (blockIsCreating.getTimeToGenerate() > THRESHOLD_SAME || numOfStartingZeros == MAX_NUM_OF_ZEROS) {
            blockIsCreating.setZeroUpdateMessage(N_UPDATE_SAME);
        } else {
            numOfStartingZeros++;
            blockIsCreating.setZeroUpdateMessage(N_UPDATE_PLUS + Blockchain.getInstance().getNumOfStartingZeros());
        }
        blockIsCreating.setMinerPaymentTransaction(new PaymentForTransaction(miner.getAccount(), MINING_REWARD));
        blocksList.addLast(blockIsCreating);
        System.out.println(blockIsCreating);
        startNewBlock();
    }

    private void startNewBlock() {
        blockIsCreating = new Block(instance.getBlocksList().getLast());
    }

    public void setBlocksList(Deque<Block> blocksList) {
        Blockchain.blocksList = blocksList;
    }

    public Deque<Block> getBlocksList() {
        return blocksList;
    }

    public int getNumOfStartingZeros() {
        return numOfStartingZeros;
    }

    public boolean underConstruction() {
        return blocksList.size() < MAX_NUM_OF_BLOCKS;
    }

    public Block getBlockUnderConstruction() {
        return blockIsCreating;
    }

    public void registerAccount(Account newAccount) {
        registeredAccounts.add(newAccount);
    }

    public List<Account> getRegisteredAccounts() {
        return registeredAccounts;
    }
}