package com.company.model;

public class NeighbourNode extends GeneralNode {


    public NeighbourNode(String ip, int port) {
        super(ip, port);
    }

    // this neightbour node should  be subjected to added.


    @Override
    public String toString() {
        return "NeighbourNode{" + getIp() + " " + " " + getPort() + "}";
    }
}
