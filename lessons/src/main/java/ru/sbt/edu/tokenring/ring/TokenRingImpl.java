package ru.sbt.edu.tokenring.ring;

import ru.sbt.edu.tokenring.node.Node;
import ru.sbt.edu.tokenring.node.NodeRoutine;
import ru.sbt.edu.tokenring.node.NodeTwo;
import ru.sbt.edu.tokenring.token.Token;
import ru.sbt.edu.tokenring.token.TokenMedium;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public class TokenRingImpl implements TokenRing {
    private final int size;
    private final List<TokenMedium> mediums;
    private final List<Node> nodes;
    private final List<Thread> nodeThreads;


    public TokenRingImpl(int size, List<TokenMedium> mediums, List<Node> nodes, List<Thread> nodeRoutineThreads) {
        this.size = size;
        this.mediums = mediums;
        this.nodes = nodes;
        this.nodeThreads = nodeRoutineThreads;
    }

    public static TokenRingImpl factory(List<TokenMedium> mediums) {
        int count = mediums.size();

        Consumer<Token> tokenConsumer = token -> {}; //todo

        List<Node> nodes = mediums.stream()
                .map(medium -> new NodeTwo(medium, tokenConsumer))
                .peek(Node::getId)
                .collect(toList());

        List<Thread> nodeRoutineThreads = nodes.stream()
                .map(n -> {
                    int prevId = (n.getId() - 1) % count;
                    return new NodeRoutine(n, mediums.get(prevId));
                }).map(Thread::new)
                .collect(toList());

        return new TokenRingImpl(count, mediums, nodes, nodeRoutineThreads);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void start() {
        nodeThreads.forEach(Thread::start);
    }

    @Override
    public void stop() {
        nodeThreads.stream()
                .peek(Thread::interrupt)
                .forEach(thread -> {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    }
}
