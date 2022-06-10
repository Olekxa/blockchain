package templates;

import java.util.List;
import java.util.Random;

public class TransactionOperation {

    public synchronized static TransactionBasis generateRandomTransactionBasis(Account senderAccount) {
        Random rand = new Random();
        List<Account> listOfAccount = Blockchain.getInstance().getRegisteredAccounts();
        if (listOfAccount.isEmpty()) {
            return null;
        }
        int receiverIndex = rand.nextInt(listOfAccount.size());
        Account receiverAccount = listOfAccount.get(receiverIndex);
        if (senderAccount.equals(receiverAccount)) {
            return null;
        }

        long value = rand.nextInt(100) + 1;

        if (senderAccount.getCapital() < value) {
            if (rand.nextInt(100) + 1 < 95) {
                return null;
            }
        }
        return new TransactionBasis(senderAccount, receiverAccount, value);
    }
}
