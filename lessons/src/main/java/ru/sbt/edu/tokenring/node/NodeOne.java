package ru.sbt.edu.tokenring.node;

import ru.sbt.edu.tokenring.token.Token;

import java.util.function.Consumer;

public class NodeOne implements Node {
    private final Node next; //problems?
    private final Consumer<Token> tokenConsumer;


    public NodeOne(Node next, Consumer<Token> tokenConsumer) {
        this.next = next;
        this.tokenConsumer = tokenConsumer;
    }

    @Override
    public void receive(Token token) throws InterruptedException {
        if (getId() == token.getDestinationId()) {
            tokenConsumer.accept(token); //can be very very long
        }
        next.receive(token); //synchronous execution
    }
}
