package com.example.stackcalc;

import com.example.stackcalc.commands.AddCommand;
import com.example.stackcalc.commands.DefineCommand;
import com.example.stackcalc.commands.DivideCommand;
import com.example.stackcalc.commands.MultiplyCommand;
import com.example.stackcalc.commands.PopCommand;
import com.example.stackcalc.commands.PrintCommand;
import com.example.stackcalc.commands.PushCommand;
import com.example.stackcalc.commands.SqrtCommand;
import com.example.stackcalc.commands.SubtractCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.*;

public class StackCalcTests {
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final InputStream originalIn = System.in;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);
    }

    @Test
    public void testPushCommand() throws CommandException {
        Context ctx = new Context();
        PushCommand cmd = new PushCommand();
        cmd.execute(ctx, new String[]{"3.14"});
        Deque<Double> stack = ctx.getStack();
        assertEquals(1, stack.size());
        assertEquals(3.14, stack.peek(), 1e-9);
    }

    @Test
    public void testPushCommandInvalidArgCount() {
        Context ctx = new Context();
        PushCommand cmd = new PushCommand();
        CommandException ex = assertThrows(CommandException.class,
                () -> cmd.execute(ctx, new String[]{"1", "2"}));
        assertTrue(ex.getMessage().contains("PUSH requires exactly 1 argument"));
    }

    @Test
    public void testPopCommand() throws CommandException {
        Context ctx = new Context();
        // empty stack
        PopCommand pop = new PopCommand();
        assertThrows(CommandException.class, () -> pop.execute(ctx, new String[]{}));
        // non-empty
        ctx.getStack().push(42.0);
        pop.execute(ctx, new String[]{});
        assertTrue(ctx.getStack().isEmpty());
    }

    @Test
    public void testAddCommand() throws CommandException {
        Context ctx = new Context();
        AddCommand add = new AddCommand();
        assertThrows(CommandException.class, () -> add.execute(ctx, new String[]{}));
        ctx.getStack().push(2.0);
        ctx.getStack().push(3.0);
        add.execute(ctx, new String[]{});
        assertEquals(1, ctx.getStack().size());
        assertEquals(5.0, ctx.getStack().pop(), 1e-9);
    }

    @Test
    public void testSubtractCommand() throws CommandException {
        Context ctx = new Context();
        SubtractCommand sub = new SubtractCommand();
        assertThrows(CommandException.class, () -> sub.execute(ctx, new String[]{}));
        ctx.getStack().push(5.0);
        ctx.getStack().push(2.0);
        sub.execute(ctx, new String[]{});
        assertEquals(1, ctx.getStack().size());
        assertEquals(3.0, ctx.getStack().pop(), 1e-9);
    }

    @Test
    public void testMultiplyCommand() throws CommandException {
        Context ctx = new Context();
        MultiplyCommand mul = new MultiplyCommand();
        assertThrows(CommandException.class, () -> mul.execute(ctx, new String[]{}));
        ctx.getStack().push(4.0);
        ctx.getStack().push(3.0);
        mul.execute(ctx, new String[]{});
        assertEquals(1, ctx.getStack().size());
        assertEquals(12.0, ctx.getStack().pop(), 1e-9);
    }

    @Test
    public void testDivideCommand() throws CommandException {
        Context ctx = new Context();
        DivideCommand div = new DivideCommand();
        assertThrows(CommandException.class, () -> div.execute(ctx, new String[]{}));
        // division by zero
        ctx.getStack().push(1.0);
        ctx.getStack().push(0.0);
        CommandException ex = assertThrows(CommandException.class, () -> div.execute(ctx, new String[]{}));
        assertTrue(ex.getMessage().contains("Division by zero"));
        // normal division
        ctx.getStack().clear();
        ctx.getStack().push(10.0);
        ctx.getStack().push(2.0);
        div.execute(ctx, new String[]{});
        assertEquals(1, ctx.getStack().size());
        assertEquals(5.0, ctx.getStack().pop(), 1e-9);
    }

    @Test
    public void testSqrtCommand() throws CommandException {
        Context ctx = new Context();
        SqrtCommand sqrt = new SqrtCommand();
        assertThrows(CommandException.class, () -> sqrt.execute(ctx, new String[]{}));
        ctx.getStack().push(-1.0);
        CommandException ex = assertThrows(CommandException.class, () -> sqrt.execute(ctx, new String[]{}));
        assertTrue(ex.getMessage().contains("You can't take the root of a negative number."));
        ctx.getStack().clear();
        ctx.getStack().push(9.0);
        sqrt.execute(ctx, new String[]{});
        assertEquals(3.0, ctx.getStack().pop(), 1e-9);
    }

    @Test
    public void testPrintCommand() throws CommandException {
        Context ctx = new Context();
        PrintCommand print = new PrintCommand();

        assertThrows(CommandException.class,
                () -> print.execute(ctx, new String[]{}));

        ctx.getStack().push(7.5);
        print.execute(ctx, new String[]{});

        String expected = "7.5" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testDefineCommand() throws CommandException {
        Context ctx = new Context();
        DefineCommand define = new DefineCommand();
        assertThrows(CommandException.class, () -> define.execute(ctx, new String[]{"A"}));
        define.execute(ctx, new String[]{"X", "4.2"});
        assertTrue(ctx.getDefines().containsKey("X"));
        assertEquals(4.2, ctx.getDefines().get("X"), 1e-9);
    }

    @Test
    public void testCommandFactory() throws CommandException {
        CommandFactory factory = new CommandFactory();
        Command push = factory.create("PUSH");
        assertTrue(push instanceof PushCommand);
        assertThrows(CommandException.class, () -> factory.create("UNKNOWN_CMD"));
    }



}
