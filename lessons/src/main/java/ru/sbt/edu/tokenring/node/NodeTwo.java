package ru.sbt.edu.tokenring.node;

import ru.sbt.edu.tokenring.token.Token;
import ru.sbt.edu.tokenring.token.TokenMedium;

import java.util.function.Consumer;

public class NodeTwo implements Node {
    private final TokenMedium next;
    private final Consumer<Token> tokenConsumer;

    public NodeTwo(TokenMedium next, Consumer<Token> tokenConsumer) {
        this.next = next;
        this.tokenConsumer = tokenConsumer;
    }

    @Override
    public void receive(Token token) throws InterruptedException {
        if (getId() == token.getDestinationId()) {
            tokenConsumer.accept(token); //can be very very long 1min
        }
        next.push(token); //
    }
}
