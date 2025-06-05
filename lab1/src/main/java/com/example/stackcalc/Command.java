package com.example.stackcalc;

public interface Command {
    void execute(Context context, String[] args) throws CommandException;
}
