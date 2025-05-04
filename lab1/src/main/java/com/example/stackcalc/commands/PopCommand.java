package com.example.stackcalc.commands;

import com.example.stackcalc.Command;
import com.example.stackcalc.Context;
import com.example.stackcalc.CommandException;

public class PopCommand implements Command {
    @Override
    public void execute(Context context, String[] args) throws CommandException {
        if (context.getStack().isEmpty()) {
            throw new CommandException("The stack is empty, nothing to POP");
        }
        context.getStack().pop();
    }
}
