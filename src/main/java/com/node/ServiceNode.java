package com.node;

public record ServiceNode(
        String id,
        String ip,
        int port
) implements Node {

    @Override
    public String key() {
        return id + "-" + ip + "-" + port;
    }

    @Override
    public String toString() {
        return "ServiceNode{" +
                "id='" + id + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
