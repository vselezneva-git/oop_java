package com.example.stackcalc.commands;

import com.example.stackcalc.Command;
import com.example.stackcalc.Context;
import com.example.stackcalc.CommandException;

public class PrintCommand implements Command {
    @Override
    public void execute(Context ctx, String[] args) throws CommandException {
        if (ctx.getStack().isEmpty()) {
            throw new CommandException("The stack is empty, nothing to PRINT");
        }
        System.out.println(ctx.getStack().peek());
    }
}
