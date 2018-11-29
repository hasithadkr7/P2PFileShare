package com.company.utils;

import com.company.model.GeneralNode;
import com.company.model.NeighbourNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class StringUtils {

    public static final String JOIN_FORMAT = "JOIN %s %s";
    public static final String LEAVE_FORMAT = "LEAVE %s %s";
    public static final String JOIN_OK_FORMAT = "JOINOK %s";
    public static final String LEAVE_OK_FORMAT = "LEAVEOK %s";
    public static final String REG_FORMAT = "REG %s %s %s";
    /**
     *
     * @param response : 0012 REGOK 0, 0027 REGOK 1 127.0.0.1 1234, 0042 REGOK 2 127.0.0.1 1236 127.0.0.1 1235
     * @return pairs of IPs and Ports. (set of GeneralNodes
     */
    public static final List<NeighbourNode> decodeRegResonseToIpPort(String response) {
        List<NeighbourNode> neighbours = new ArrayList<>();

        String[] tokens = response.split(" ");
        try {
            int nNeighbours = Integer.parseInt(tokens[2].trim());
            for (int i = 0; i < nNeighbours && nNeighbours != 9998 && nNeighbours != 9997 ; i++) {
                NeighbourNode node = new NeighbourNode(tokens[2*i + 3].trim(), Integer.parseInt(tokens[2*i + 4].trim()));
                neighbours.add(node);
            }
            return neighbours;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] messageToBytes(String messageFormat, Object... args) throws IllegalFormatException {
        StringBuilder message = new StringBuilder();

        String payLoad =  String.format(messageFormat, args);
        int length = payLoad.length() + 5;
        String len = String.format("%04d", length);
        message.append(len).append(" ").append(payLoad);
        return message.toString().getBytes();
    }

    public static List<String> getRandomFiles(int nFiles) {

        String[] allFiles = {
                "Adventures of Tintin",
                "Jack and Jill",
                "Glee",
                "The Vampire Diarie",
                "King Arthur",
                "Windows XP",
                "Harry Potter",
                "Kung Fu Panda",
                "Lady Gaga",
                "Twilight",
                "Windows 8",
                "Mission Impossible",
                "Turn Up The Music",
                "Super Mario",
                "American Pickers",
                "Microsoft Office 2010",
                "Happy Feet",
                "Modern Family",
                "American Idol",
                "Hacking for Dummies" };
        String[] randomFiles = new String[nFiles];
        // get some random files from the above list.
        for (int i = 0; i < nFiles; i++) {
            randomFiles[i] = allFiles[ThreadLocalRandom.current().nextInt(0, allFiles.length)];
        }
        return Arrays.asList(randomFiles);
    }
}
