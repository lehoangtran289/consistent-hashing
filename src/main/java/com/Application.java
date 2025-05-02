package com;

import com.core.ConsistentHashRing;
import com.hashing.MD5Hash;
import com.node.SimpleNode;

import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        // Create a list of physical nodes
        List<SimpleNode> physicalNodes = new ArrayList<>();
        physicalNodes.add(new SimpleNode("Node1"));
        physicalNodes.add(new SimpleNode("Node2"));
        physicalNodes.add(new SimpleNode("Node3"));

        // Create a ConsistentHashRing with 3 virtual nodes per physical node
        ConsistentHashRing<SimpleNode> hashRing = new ConsistentHashRing<>(physicalNodes, 3, new MD5Hash());

        System.out.println(hashRing);

        // Route some keys
        System.out.println("Routing keys:");
        routeKeys(hashRing);

        // Add a new physical node
        SimpleNode newNode = new SimpleNode("Node4");
        hashRing.addNode(newNode, 3);
        System.out.println("\nAdded Node4. Routing keys again:");
        routeKeys(hashRing);

        // Remove a physical node
        hashRing.removeNode(physicalNodes.get(2)); // Remove Node3
        System.out.println("\nRemoved Node3. Routing keys again:");
        routeKeys(hashRing);
    }

    private static void routeKeys(ConsistentHashRing<SimpleNode> hashRing) {
        System.out.println("Key1 -> " + hashRing.routeNode("Key-1"));
        System.out.println("Key2 -> " + hashRing.routeNode("Key-2"));
        System.out.println("Key3 -> " + hashRing.routeNode("Key-3"));
        System.out.println("Key4 -> " + hashRing.routeNode("Key-4"));
        System.out.println("Key5 -> " + hashRing.routeNode("Key-5"));
    }
}
