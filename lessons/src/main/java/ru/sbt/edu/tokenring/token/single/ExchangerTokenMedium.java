package ru.sbt.edu.tokenring.token.single;

import ru.sbt.edu.tokenring.token.Token;

import java.util.concurrent.Exchanger;

public class ExchangerTokenMedium implements SingleTokenMedium {
    private final Exchanger<Token> exchanger;

    public ExchangerTokenMedium(Exchanger<Token> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void push(Token token) throws InterruptedException {
        exchanger.exchange(token);
    }

    @Override
    public Token poll() throws InterruptedException {
        return exchanger.exchange(null);
    }
}
