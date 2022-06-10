package templates;

import utils.KeyGenerator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Random;
import java.util.UUID;

public class Account implements Runnable {

    private final UUID uuid = UUID.randomUUID();
    private final String name;
    private final boolean isSending;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public Account(String name, boolean isSending) throws NoSuchAlgorithmException{
        this.name = name;
        KeyGenerator keyGenerator = new KeyGenerator(1024);
        this.privateKey = keyGenerator.getPrivateKey();
        this.publicKey = keyGenerator.getPublicKey();
        this.isSending = isSending;
    }

    public long getCapital() {
        return Blockchain.getInstance().getFundsByAccountUUID(this.uuid);
    }

    @Override
    public void run() {
        Random rand = new Random();
        if (isSending) {
            while (Blockchain.getInstance().underConstruction()) {
                try {
                    Thread.sleep(rand.nextInt(5) * 500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TransactionBasis transactionBasis = TransactionOperation.generateRandomTransactionBasis(this);
                if (transactionBasis != null) {
                    try {
                        Transaction transaction = createSignedTransaction(transactionBasis);
                        Blockchain.getInstance().receiveFromAccount(this, transaction);
                    } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private synchronized Transaction createSignedTransaction(TransactionBasis transactionBasis) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        long transactionId = IdCounter.generateTransactionCounter();
        String transactionMsg = transactionBasis.buildMessage();
        byte[] signatureInput = (transactionId + transactionMsg + getLedgerSignature()).getBytes();
        return new Transaction(transactionBasis, transactionId, generateSignatureThroughPrivateKey(signatureInput), this.publicKey);
    }

    private synchronized byte[] generateSignatureThroughPrivateKey(byte[] signatureInput) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(this.privateKey);
        rsa.update(signatureInput);
        return rsa.sign();
    }

    public String getLedgerSignature() {
        return this.name.replaceAll("[aeiou]", "x");
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

}