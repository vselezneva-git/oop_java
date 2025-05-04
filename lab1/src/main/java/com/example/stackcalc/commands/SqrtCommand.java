package com.example.stackcalc.commands;

import com.example.stackcalc.Command;
import com.example.stackcalc.Context;
import com.example.stackcalc.CommandException;

public class SqrtCommand implements Command {
    @Override
    public void execute(Context ctx, String[] args) throws CommandException {
        if (ctx.getStack().isEmpty()) {
            throw new CommandException("Stack is empty, nothing to pop for SQRT");
        }
        double a = ctx.getStack().pop();
        if (a < 0) {
            throw new CommandException("You can't take the root of a negative number.");
        }
        ctx.getStack().push(Math.sqrt(a));
    }
}
