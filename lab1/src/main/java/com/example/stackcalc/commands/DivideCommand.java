package com.example.stackcalc.commands;

import com.example.stackcalc.Command;
import com.example.stackcalc.Context;
import com.example.stackcalc.CommandException;

public class DivideCommand implements Command {
    @Override
    public void execute(Context ctx, String[] args) throws CommandException {
        if (ctx.getStack().size() < 2) {
            throw new CommandException("Not enough elements in stack for DIV");
        }
        double b = ctx.getStack().pop();
        double a = ctx.getStack().pop();
        if (b == 0) {
            throw new CommandException("Division by zero");
        }
        ctx.getStack().push(a / b);
    }
}
