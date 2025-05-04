package com.example.stackcalc;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Context {
    private final Deque<Double> stack = new LinkedList<>();
    private final Map<String, Double> defines = new HashMap<>();

    public Deque<Double> getStack() {
        return stack;
    }

    public Map<String, Double> getDefines() {
        return defines;
    }
}
