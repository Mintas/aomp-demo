package ru.sbt.edu.tokenring.token.buffer;

import ru.sbt.edu.tokenring.token.Token;
import ru.sbt.edu.tokenring.token.TokenMedium;

import java.util.concurrent.BlockingQueue;

public class BlockingQueueTokenMedium implements BufferedTokenMedium {
    private final BlockingQueue<Token> blockingQueue;

    public BlockingQueueTokenMedium(BlockingQueue<Token> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void push(Token token) throws InterruptedException {
        blockingQueue.put(token);
    }

    @Override
    public Token poll() throws InterruptedException {
        return blockingQueue.take();
    }
}
