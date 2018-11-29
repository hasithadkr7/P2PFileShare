package com.company.model;

import com.company.Neighbour;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Has the information of neighbouring nodes.
 *
 */
public class RoutingTable {


    // these neigbours should connect to the required node.
    private ArrayList<NeighbourNode> neighbours;

    public RoutingTable() {
        neighbours = new ArrayList<>();
    }

    public synchronized void addNeighBour(NeighbourNode neighbour) {
        // should validate duplicate neighbour addition
        this.neighbours.add(neighbour);
    }

    public synchronized void removeNeighBour(NeighbourNode neighbour) {
        // should validate duplicate neighbour addition
        this.neighbours.remove(neighbour);
    }

    public synchronized void removeNeighBour(String address, int port) {
        // should validate duplicate neighbour addition
        this.neighbours.removeIf(neighbourNode -> (address.equals(neighbourNode.getIp()) && port == neighbourNode.getPort()));
    }

    public List<NeighbourNode> getAllNeighbours() {
        return this.neighbours;
    }

    public void removeAllNeighbours() {
        this.neighbours = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "RoutingTable{" +
                "neighbours=" + neighbours +
                '}';
    }
}
