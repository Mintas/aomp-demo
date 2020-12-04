package ru.sbt.edu.tokenring.node;

import ru.sbt.edu.tokenring.token.Token;
import ru.sbt.edu.tokenring.token.TokenMedium;

public class NodeRoutine implements Runnable {
    private final Node node;
    private final TokenMedium medium;

    public NodeRoutine(Node node, TokenMedium medium) {
        this.node = node;
        this.medium = medium;
    }

    @Override
    public void run() {
        try {
            job();
        } catch (InterruptedException e) {
            System.err.format("Node %d has been interrupted!\n", node.getId());
        }
    }

    private void job() throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            Token token = medium.poll();
            node.receive(token);
        }
    }
}
