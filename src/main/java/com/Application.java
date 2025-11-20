package com;

import com.core.ConsistentHashRing;
import com.hashing.MD5Hash;
import com.node.ServiceNode;

import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        // Create a list of physical nodes
        List<ServiceNode> physicalNodes = new ArrayList<>();
        physicalNodes.add(new ServiceNode("Node1", "127.0.0.1", 8080));
        physicalNodes.add(new ServiceNode("Node2", "127.0.0.1", 8081));
        physicalNodes.add(new ServiceNode("Node3", "127.0.0.1", 8082));

        // Create a ConsistentHashRing with 3 virtual nodes per physical node
        ConsistentHashRing<ServiceNode> hashRing = new ConsistentHashRing<>(physicalNodes, 3, new MD5Hash());

        System.out.println(hashRing);

        // Route some keys
        System.out.println("Routing keys:");
        routeKeys(hashRing);

        // Add a new physical node
        ServiceNode newNode = new ServiceNode("Node4", "127.0.0.1", 8084);
        hashRing.addNode(newNode, 3);
        System.out.println("\nAdded Node4. Routing keys again:");
        routeKeys(hashRing);

        // Remove a physical node
        hashRing.removeNode(physicalNodes.get(2)); // Remove Node3
        System.out.println("\nRemoved Node3. Routing keys again:");
        routeKeys(hashRing);
    }

    private static void routeKeys(ConsistentHashRing<ServiceNode> hashRing) {
        System.out.println("Key1 -> " + hashRing.routeNode("Key-1"));
        System.out.println("Key2 -> " + hashRing.routeNode("Key-2"));
        System.out.println("Key3 -> " + hashRing.routeNode("Key-3"));
        System.out.println("Key4 -> " + hashRing.routeNode("Key-4"));
        System.out.println("Key5 -> " + hashRing.routeNode("Key-5"));
    }
}
