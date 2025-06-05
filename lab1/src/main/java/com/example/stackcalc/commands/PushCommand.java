package com.example.stackcalc.commands;

import com.example.stackcalc.Command;
import com.example.stackcalc.Context;
import com.example.stackcalc.CommandException;

public class PushCommand implements Command {
    @Override
    public void execute(Context context, String[] args) throws CommandException {
        if (args.length != 1) {
            throw new CommandException("PUSH requires exactly 1 argument");
        }
        double value = Double.parseDouble(args[0]);
        context.getStack().push(value);
    }
}
