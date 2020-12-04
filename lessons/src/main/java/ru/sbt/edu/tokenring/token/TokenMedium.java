package ru.sbt.edu.tokenring.token;

public interface TokenMedium {
    void push(Token token) throws InterruptedException;
    Token poll() throws InterruptedException;
}
