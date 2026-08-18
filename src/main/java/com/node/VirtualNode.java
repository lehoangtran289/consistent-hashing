package com.node;

public record VirtualNode<T extends Node>(
        T physicalNode,
        int replicaIndex
) implements Node {

    @Override
    public String key() {
        return physicalNode.key() + "-" + replicaIndex;
    }
}
