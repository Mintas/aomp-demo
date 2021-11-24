package ru.sbt.edu;

import java.util.ArrayList;
import java.util.List;

public class SomeEntity {
    private final List<Object> first;
    private final List<Object> second;
    private volatile String blinking;

    public SomeEntity(List<Object> first, List<Object> second, String blinking) {
        this.blinking = blinking;
        this.first = first;
        first.add(this);
        //...
        this.second = second;
    }
}