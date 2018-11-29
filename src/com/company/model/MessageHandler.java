package com.company.model;

import com.company.utils.StringUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringJoiner;

/**
 * Handle the messages between peers.
 * Has the ability to send and message from a node to another.
 * Has the ability to send JOIN messages to the System (127.0.0.1:11111)
 */
public final class MessageHandler extends Thread {

    private static MessageHandler handler;

    private MessageHandler() {

    }

    public static MessageHandler getInstance() {
        if (handler == null) {
            handler = new MessageHandler();
        }

        return handler;
    }

    public String joinNode(String ip, int port) {
        String reply = "";
        // we should call the main server and process Join message.
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            DatagramSocket socket = new DatagramSocket();

            byte[] buffer = StringUtils.messageToBytes(StringUtils.JOIN_FORMAT, ip, port);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, ipAddress, port);
            socket.send(packet);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reply;
    }
    public String registerNode(String ip, int port, String userName) {
        // register message is sent to the Bootstrap server.
        try {

            InetAddress ipAddress = InetAddress.getByName("127.0.0.1"); // ip of bootrap server.

            DatagramSocket socket = new DatagramSocket();

//            StringBuilder message = new StringBuilder();
//            String payLoad =  String.format(StringUtils.REG_FORMAT, ip, String.valueOf(port), userName);
//            int length = payLoad.length() + 5;
//            String len = String.format("%04d", length);
//            message.append(len).append(" ").append(payLoad);
            // now this message should be sent to the BS.
            byte[] buffer = StringUtils.messageToBytes(StringUtils.REG_FORMAT, ip, port, userName);

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, ipAddress, 55555);
            socket.send(packet); // sending the packet to host in  ipAddress and port.
            System.out.println("Data Sent. ");

            byte[] recvData = new byte[65536];
            DatagramPacket incomingPacket = new DatagramPacket(recvData, recvData.length);
            socket.receive(incomingPacket);
            // now we should join this received nodes to the this peernode.

            return new String(incomingPacket.getData());

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "sdas";

    }

    @Override
    public void run() {
        // JOIN Message.


    }
}
