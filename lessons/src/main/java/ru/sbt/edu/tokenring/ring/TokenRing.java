package ru.sbt.edu.tokenring.ring;

public interface TokenRing {
    //Nodes
    //Tokens
    // token -> Node1 -> Node2 -> NodeWithDestinationId
    //ring IS A cycle  Node1 -> Node2 -> .. -> NodeN -> ... Node-1 -> Node0 -> Node1
    //todo : add nodes in runtime
    int size();
    void start();
    void stop();
}
