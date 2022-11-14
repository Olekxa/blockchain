package templates;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class TransactionOperation {

    public synchronized static Optional<TransactionBasis> generateRandomTransactionBasis(Account senderAccount) {
        Random rand = new Random();
        List<Account> listOfAccount = Blockchain.getInstance().getRegisteredAccounts();
        if (listOfAccount.isEmpty()) {
            return Optional.empty();
        }
        int receiverIndex = rand.nextInt(listOfAccount.size());
        Account receiverAccount = listOfAccount.get(receiverIndex);
        if (senderAccount.equals(receiverAccount)) {
            return Optional.empty(); // do not generate transaction if it is from account  to itself;
        }

        long value = rand.nextInt(100) + 1;

        if (senderAccount.getCapital() < value) {
            // if the sender doesn't have funds enough to make this transaction
            //we are establishing a 95% probability that it will not be even generated. However, there's a slight
            //chance it is and thus the blockchain shall be able to block it.
            if (rand.nextInt(100) + 1 < 95) {
                return Optional.empty();
            }
        }
        return Optional.of(new TransactionBasis(senderAccount, receiverAccount, value));
    }
}
