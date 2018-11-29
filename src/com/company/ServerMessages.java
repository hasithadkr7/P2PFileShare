package com.company;

import com.company.model.NeighbourNode;
import com.company.model.PeerNode;

import java.util.StringTokenizer;

public enum ServerMessages {

    JOIN {
        @Override
        public String processMessage(String s, PeerNode node) {
            // JOIN ip_address port.
            String[] token = s.split(" ");

            String neighbourIP = token[2]; //// neighbourIp
            int neighbourPort = Integer.parseInt(token[3]); //// neighbourPort

//            routingTable.addNeighBour(new NeighbourNode(neighbourIP, neighbourPort));
//            System.out.println(routingTable.toString());

            // here we should connect the current node with exisiting nodes.
            // have to get the nodes.
            System.out.println("Joining Message " + s);
            return null;
        }
    },
    LEAVE {
        @Override
        public String processMessage(String s, PeerNode node) {
            return null;
        }
    };


    public abstract String processMessage(String s, PeerNode node);
}
