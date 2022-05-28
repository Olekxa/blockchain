package utils;

import templates.Blockchain;

import java.util.Scanner;


public class Runner {
    public static void run() {
        Blockchain blockchain = new Blockchain();

        for (int i = 0; i < 5; i++) {
            blockchain.addBlock();
        }

        for (int i = 1; i <= blockchain.getBlockCount(); i++) {
            System.out.println(blockchain.getBlock(i) + "\n");
        }
    }

}
