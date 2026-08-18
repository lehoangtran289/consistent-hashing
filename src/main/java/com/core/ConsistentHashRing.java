package com.core;

import com.hashing.HashFunction;
import com.hashing.MurmurHash;
import com.node.Node;
import com.node.VirtualNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

public class ConsistentHashRing<T extends Node> {
    private final NavigableMap<Long, VirtualNode<T>> ring = new TreeMap<>();
    private final Map<String, Integer> replicaCounts = new HashMap<>();
    private final HashFunction hashFunction;

    public ConsistentHashRing(Collection<T> pNodes, int vNodeCount) {
        this(pNodes, vNodeCount, new MurmurHash());
    }

    /**
     * @param pNodes       collections of physical nodes
     * @param vNodeCount   amounts of virtual nodes
     * @param hashFunction hash Function to hash Node instances
     */
    public ConsistentHashRing(Collection<T> pNodes, int vNodeCount, HashFunction hashFunction) {
        Objects.requireNonNull(hashFunction, "hashFunction must not be null");
        this.hashFunction = hashFunction;

        if (pNodes != null && !pNodes.isEmpty()) {
            for (T pNode : pNodes) {
                addNode(pNode, vNodeCount);
            }
        }
    }

    /**
     * add physic node to the hash ring with some virtual nodes
     *
     * @param pNode      physical node needs added to hash ring
     * @param vNodeCount the number of virtual node of the physical node. Value should be greater than or equals to 0
     */
    public void addNode(T pNode, int vNodeCount) {
        if (vNodeCount < 0) {
            throw new IllegalArgumentException("illegal virtual node counts :" + vNodeCount);
        }

        int existingReplicas = replicaCounts.getOrDefault(pNode.key(), 0);

        for (int i = 0; i < vNodeCount; i++) {
            VirtualNode<T> vNode = new VirtualNode<>(pNode, i + existingReplicas);
            ring.put(hashFunction.hash(vNode.key()), vNode);
        }

        replicaCounts.put(pNode.key(), existingReplicas + vNodeCount);
    }

    /**
     * remove the physical node from the hash ring
     *
     * @param pNode physical node to drop, together with all of its virtual nodes
     */
    public void removeNode(T pNode) {
        Integer replicas = replicaCounts.remove(pNode.key());
        if (replicas == null) return;

        for (int i = 0; i < replicas; i++) {
            VirtualNode<T> vNode = new VirtualNode<>(pNode, i);
            ring.remove(hashFunction.hash(vNode.key()), vNode);
        }
    }

    /**
     * with a specified key, route the nearest Node instance in the current hash ring
     *
     * @param objectKey the object key to find a nearest Node
     * @return the physical node owning the key, or null when the ring is empty
     */
    public T routeNode(String objectKey) {
        if (ring.isEmpty()) {
            return null;
        }

        Long hashVal = hashFunction.hash(objectKey);

        // single tree traversal; wrap around to the head of the ring when past the last node
        Map.Entry<Long, VirtualNode<T>> entry = ring.ceilingEntry(hashVal);

        return entry != null ?
                entry.getValue().physicalNode() :
                ring.firstEntry().getValue().physicalNode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ConsistentHashRing{");
        sb.append("hashFunction=").append(hashFunction.getClass().getSimpleName());
        sb.append(", ring={");
        boolean first = true;
        for (var entry : ring.entrySet()) {
            if (!first) sb.append(",");
            sb.append(entry.getKey()).append(":").append(entry.getValue().physicalNode().key());
            first = false;
        }
        sb.append("}}");
        return sb.toString();
    }
}
