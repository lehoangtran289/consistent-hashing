# Consistent Hash Implementation in Java

Reference http://www.codeproject.com/Articles/56138/Consistent-hashing

## Get Started

        // initialize 3 physical node
        physicalNodes.add(new ServiceNode("Node1", "127.0.0.1", 8080));
        physicalNodes.add(new ServiceNode("Node2", "127.0.0.1", 8081));
        physicalNodes.add(new ServiceNode("Node3", "127.0.0.1", 8082));

        // hash them to hash ring
        ConsistentHashRouter<ServiceNode> consistentHashRouter = new ConsistentHashRouter<>(Arrays.asList(node1,node2,node3), 3); // 3 virtual nodes

        String requestIp = "192.168.0.1";
        System.out.println(requestIp + " is route to " + consistentHashRouter.routeNode(requestIp));

Please see `ServiceNode.java` for more usage details