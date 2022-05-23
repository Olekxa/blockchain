package utils;

import templates.Blockchain;

import java.util.Scanner;

public class Runner {
    public static void run() {
        System.out.println("Enter how many zeros the hash must start with: ");
        int number;
        try (Scanner scanner = new Scanner(System.in)) {
            number = scanner.nextInt();
        }

        Blockchain blockchain = new Blockchain(number);

        for (int i = 0; i < 5; i++) {
            blockchain.addBlock();
        }

        for (int i = 1; i <= blockchain.getBlockCount(); i++) {
            System.out.println(blockchain.getBlock(i) + "\n");
        }
    }
}
