# Consistent Hashing in Java

A small, dependency-light implementation of a consistent hash ring with virtual nodes,  built for learning purposes.

Consistent hashing maps both nodes and keys onto the same circular hash space, so adding  or removing a node only remaps the keys that belong to that node's arc of the ring  instead of reshuffling everything (as `hash(key) % N` would).

## Get Started

```java
// 1. Describe the physical nodes
List<ServiceNode> physicalNodes = List.of(
        new ServiceNode("Node1", "127.0.0.1", 8080),
        new ServiceNode("Node2", "127.0.0.1", 8081),
        new ServiceNode("Node3", "127.0.0.1", 8082)
);

// 2. Build the ring: 3 virtual nodes per physical node, hashed with MurmurHash3
ConsistentHashRing<ServiceNode> hashRing = new ConsistentHashRing<>(physicalNodes, 3, new MurmurHash());

// 3. Route a key to the node that owns it
ServiceNode owner = hashRing.routeNode("Key-1");

// 4. Grow or shrink the cluster at runtime
hashRing.addNode(new ServiceNode("Node4", "127.0.0.1", 8084), 3);
hashRing.removeNode(physicalNodes.get(2));
```

Sample output:

```
Routing keys:
Key1 -> ServiceNode{id='Node3', ip='127.0.0.1', port=8082}
Key2 -> ServiceNode{id='Node3', ip='127.0.0.1', port=8082}
Key3 -> ServiceNode{id='Node1', ip='127.0.0.1', port=8080}
...

Added Node4. Routing keys again:
Key1 -> ServiceNode{id='Node4', ip='127.0.0.1', port=8084}   # moved
Key2 -> ServiceNode{id='Node3', ip='127.0.0.1', port=8082}   # unchanged
Key3 -> ServiceNode{id='Node1', ip='127.0.0.1', port=8080}   # unchanged
```

Notes on the implementation:

- **Virtual nodes.** Each physical node is placed on the ring `vNodeCount` times under the key `<node-key>-<replicaIndex>`. More replicas means a smoother key distribution
- **Lookup is `O(log n)` by using `NavigableMap`**
- **Replica bookkeeping.** The ring tracks how many replicas each physical node has, so removal can reconstruct and delete exactly the virtual node keys that were added.

## Custom Nodes and Hash Functions

Implement `Node` to put your own type on the ring:

```java
record CacheServer(String host) implements Node {
    @Override public String key() { return host; }
}
```

Implement `HashFunction` to swap the hashing strategy:

```java
ConsistentHashRing<CacheServer> ring = new ConsistentHashRing<>(servers, 100, new MD5Hash());
```
