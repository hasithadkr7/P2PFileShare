package com.company.model;

import com.company.Neighbour;
import com.company.ServerMessages;
import com.company.utils.StringUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;


/**
 * A Peer request to Join the network. or Join another peer. this is a peer node of some nodes
 */
public class PeerNode extends GeneralNode {


    // has neighbours.
    private MessageHandler handler;
    // list of files.
    private List<String> files;
    // each has a receiver and a sender.
    // each has a routing table.
    private RoutingTable routingTable;
    private DatagramSocket recvSocket = null;
    private DatagramSocket sendSocket = null;

    /**
     * Peer should be able to recieve and send the messages at a time. Which means, it listens for any incoming always.
     * @param ip
     * @param port
     * @param userName
     * @param nFiles
     */
    public PeerNode(String ip, int port, String userName, int nFiles) {
        super(ip, port);
        setUserName(userName);
        System.out.println(this.toString());
        try {
            recvSocket = new DatagramSocket(port, InetAddress.getByName(ip));
            sendSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        files = StringUtils.getRandomFiles(nFiles);
        System.out.println(files);
        routingTable = new RoutingTable();
        handler = MessageHandler.getInstance();
        // node should be saving some random files from the list.
        // let the node be registered.
        String neighbourNodes = handler.registerNode(ip, port, userName);
        // after registration, we have to join with other regitered nodes.
        // we should join the two neightbours
        // decrypt the
        System.out.println(neighbourNodes);
        List<NeighbourNode> neighboursList = StringUtils.decodeRegResonseToIpPort(neighbourNodes);
        // above are the contacted nodes of this node. So routing table of this node should updated with the
        // ip and port of contacted nodes. this should using JOIN Messages
        startRecieve();

        neighboursList.forEach(neighbourNode -> {
            // send a JOIN request to Neighbour node.
            join(neighbourNode.getIp(), neighbourNode.getPort());
            routingTable.addNeighBour(neighbourNode);
        });

    }

    public String searchFile(String query) {
        String [] tokens = query.split(" ");
        if (tokens.length < 5 || "SER".equals(tokens[1].trim())) {
            return  "INVALID SEARCH QUERY";
        }
        String fileName = tokens[4].trim();
        List<String> searchedFile = this.files.stream().filter(s -> s.contains(fileName)).collect(Collectors.toList());
        if (searchedFile.isEmpty()) {
            // send the query to the neightbours. and
            // reduce the hop count on the query.
            int hops = Integer.parseInt(tokens[5].trim());
            hops-- ;
            if (hops == 0) {
                return "FILE NOT FOUND";
            }
            query.replace(tokens[5], String.valueOf(hops));

            routingTable.getAllNeighbours().forEach(neighbourNode -> {

                byte[] buffer = query.getBytes();
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(neighbourNode.getIp()), neighbourNode.getPort());
                    sendSocket.send(packet);
                    // send th data to a neighbour.
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        // create the query here once files are found.
        return searchedFile.toString();
    }


    public void printRoutingTable() {
        System.out.println(this.routingTable.toString());
    }

    public String join(String toNodeAddress, int toNodePort) {
        String reply = "JOINOK";
        // this Join request is for the neightbour node. So send the DatagramSocket to the neighbour
       startSend(toNodeAddress, toNodePort);
       return reply;
    }

    public void startSend(String toNodeAddress, int toNodePort) {
        try {
            // JOIN message that I am going to JOIN the network.
            byte[] buffer = StringUtils.messageToBytes(StringUtils.JOIN_FORMAT, getIp(), getPort());
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(toNodeAddress), toNodePort);
            sendSocket.send(packet); // sending the JOIN Message to the required neighbour node (ip, port)
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startRecieve() {
        (new Thread() {
            @Override
            public void run() {
                try {
                    // here we should implement works for different message formats.

                    byte[] recvBuffer = new byte[65536];
                    DatagramPacket recvPacket = new DatagramPacket(recvBuffer, recvBuffer.length);
                    recvSocket.receive(recvPacket);
                    String message = new String(recvPacket.getData());
                    System.out.println(message);
                    // now update the routing table
                    StringTokenizer tokenizer = new StringTokenizer(new String(recvPacket.getData()), " ");
                    tokenizer.nextToken(); //length
                    String messageType = tokenizer.nextToken().trim(); // JOIN
                    if ("JOIN".equals(messageType)) {
                        String neighbourIP = tokenizer.nextToken(); //// neighbourIp
                        int neighbourPort = Integer.parseInt(tokenizer.nextToken().trim()); //// neighbourPort
                        routingTable.addNeighBour(new NeighbourNode(neighbourIP, neighbourPort));

                        // send the JOINOK
                    }
                    if ("LEAVE".equals(messageType)) {
                        // remove data from the routing table. need to get the ip port out of the routing tables.
                        String neighbourIP = tokenizer.nextToken(); //// neighbourIp
                        int neighbourPort = Integer.parseInt(tokenizer.nextToken().trim()); //// neighbourPort
                        routingTable.removeNeighBour(neighbourIP, neighbourPort);
                    }

                    if ("SER".equals(messageType)) {
                        // first search within the node.
                        // neighbour start search for the file.
                        searchFile(message);
                    }
                    System.out.println(routingTable.toString());
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "PeerNode{" + getIp() + ":" + getPort() + "-" + getUserName() + "}";
    }
}
