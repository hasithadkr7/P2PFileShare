package com.company;

import com.company.model.PeerNode;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A representation for the App run in a single node.
 * This class should get the messages once they are
 */
public class Main {

    public static void main(String[] args) {
	// write your code here
        // this is the client. Where Nodes are created and registerd in the bootstrap.


        int port = ThreadLocalRandom.current().nextInt(1000, 50000);
        int nFiles = ThreadLocalRandom.current().nextInt(3,6);
        PeerNode node1 = new PeerNode("127.0.0.1", port, "node" + port, nFiles);

        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.println("What you want to do?");
            System.out.println("1. Search for a file");
            System.out.println("2. Print File Names for a node.");
            System.out.println("3. Add a new File");
            System.out.println("4. Comment on a File");
            System.out.println("5. Rank a file");
            System.out.println("6. Summary of files with ranks");

            if (in.nextLine().equals("1")) {
                System.out.println("Enter Search Query :");
                String query = in.nextLine();
                node1.searchFile(query);
            }

        }
    }
}
