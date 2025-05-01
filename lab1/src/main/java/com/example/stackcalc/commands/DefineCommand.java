package com.example.stackcalc.commands;

import com.example.stackcalc.Command;
import com.example.stackcalc.Context;
import com.example.stackcalc.CommandException;

public class DefineCommand implements Command {
    @Override
    public void execute(Context ctx, String[] args) throws CommandException {
        if (args.length != 2) {
            throw new CommandException("DEFINE requires two arguments: name and value.");
        }
        String name = args[0];
        double value = Double.parseDouble(args[1]);
        ctx.getDefines().put(name, value);
    }
}
