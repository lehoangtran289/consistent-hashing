package com.node;

public record VirtualNode<T extends Node>(
        T physicalNode,
        int replicaIndex
) implements Node {

    @Override
    public String getKey() {
        return physicalNode.getKey() + "-" + replicaIndex;
    }

    public boolean isVirtualNodeOf(T pNode) {
        return physicalNode.getKey().equals(pNode.getKey());
    }
}
