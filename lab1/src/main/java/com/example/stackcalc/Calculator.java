package com.example.stackcalc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.*;

public class Calculator {
    private static final Logger LOGGER = Logger.getLogger(Calculator.class.getName());

    public static void main(String[] args) throws Exception {
        Logger root = Logger.getLogger("");
        for (Handler handler : root.getHandlers()) {
            root.removeHandler(handler);
        }

        Handler fileHandler = new FileHandler("calc.log", true);
        fileHandler.setFormatter(new SimpleFormatter());
        root.addHandler(fileHandler);
        root.setLevel(Level.INFO);

        CommandFactory factory = new CommandFactory();
        Context ctx = new Context();

        BufferedReader reader;
        if (args.length > 0) {
            reader = new BufferedReader(new FileReader(args[0]));
            LOGGER.info("Reading commands from file: " + args[0]);
        } else {
            reader = new BufferedReader(new InputStreamReader(System.in));
            LOGGER.info("Reading commands from STDIN");
        }

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            LOGGER.info("Line: '" + line + "'");

            String[] parts = line.split("\\s+");
            String commandName = parts[0];
            String[] commandArgs = Arrays.copyOfRange(parts, 1, parts.length);


            if (!"DEFINE".equals(commandName)) {
                for (int i = 0; i < commandArgs.length; i++) {
                    if (ctx.getDefines().containsKey(commandArgs[i])) {
                        String var = commandArgs[i];
                        commandArgs[i] = ctx.getDefines().get(var).toString();
                        LOGGER.fine("Substituted: " + var + " => " + commandArgs[i]);
                    }
                }
            }

            try {
                LOGGER.info("Executing: " + commandName + " " + Arrays.toString(commandArgs));
                Command cmd = factory.create(commandName);
                cmd.execute(ctx, commandArgs);
                LOGGER.fine("Stack after execution: " + ctx.getStack());
            } catch (CommandException e) {
                LOGGER.log(Level.SEVERE, "Error in line '" + line + "': " + e.getMessage(), e);
                System.err.println("Error in line '" + line + "': " + e.getMessage());
            }
        }

        LOGGER.info("Calculator finished execution");

        // Close all handlers
        for (Handler handler : root.getHandlers()) {
            handler.close();
        }
    }
}
