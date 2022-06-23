package templates;

import utils.StringHashUtil;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

public class Miner implements Runnable {

    private final int id;
    private final Account account;

    public Miner() throws NoSuchAlgorithmException {
        this.id = IdCounter.generateThreadId();
        this.account = new Account("miner" + this.id, true);
        Blockchain.getInstance().registerAccount(this.account);
    }

    @Override
    public void run() {

        while (Blockchain.getInstance().underCreation()) {
            Proof proof = generateProof();
            Blockchain.getInstance().receiveFromMiner(this, proof.getHash(), proof.getMagicNum(), proof.getTimeToGenerate());
        }
    }

    private Proof generateProof() {
        Proof proof = new Proof();
        String compatibleHash;
        Random rand = new Random();
        long randLong;
        int initialN = Blockchain.getInstance().getNumOfStartingZeros();
        do {
            randLong = Math.abs(rand.nextLong());
            compatibleHash = StringHashUtil.applySha256(this.id
                    + Blockchain.getInstance().getBlockUnderCreation().getHashOfPrevious()
                    + Blockchain.getInstance().getBlockUnderCreation().getTimestamp()
                    + randLong);
        } while ((!compatibleHash.substring(0, Blockchain.getInstance().getNumOfStartingZeros()).matches("0*")) && (Blockchain.getInstance().getNumOfStartingZeros() == initialN));
        proof.setHash(compatibleHash);
        proof.setMagicNum(randLong);
        proof.setTimeToGenerate(((new Date().getTime()) - Blockchain.getInstance().getBlockUnderCreation().getTimestamp()) / 1000);
        return proof;
    }

    public Account getAccount() {
        return account;
    }

    public int getId() {
        return id;
    }

    protected static class  Proof {
        private String hash;
        private long magicNum;
        private long timeToGenerate;

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public long getMagicNum() {
            return magicNum;
        }

        public void setMagicNum(long magicNum) {
            this.magicNum = magicNum;
        }

        public long getTimeToGenerate() {
            return timeToGenerate;
        }

        public void setTimeToGenerate(long timeToGenerate) {
            this.timeToGenerate = timeToGenerate;
        }
    }
}
