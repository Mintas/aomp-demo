package ru.sbt.edu.tokenring.node;

import ru.sbt.edu.tokenring.token.Token;
import ru.sbt.edu.utils.ThreadID;

public interface Node {
    default int getId() {
        return ThreadID.get();
    }

    void receive(Token token) throws InterruptedException;
}
