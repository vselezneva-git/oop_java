package com.example.db.command;

import com.example.db.engine.*;
import com.example.db.storage.DatabaseStorage;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {

    private static final Pattern CREATE_RE = Pattern.compile("^CREATE TABLE (\\w+) \\((.+)\\)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern DROP_RE = Pattern.compile("^DROP TABLE (\\w+)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern INSERT_RE = Pattern.compile("^INSERT INTO (\\w+) \\((.+)\\)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern DELETE_RE = Pattern.compile("^DELETE FROM (\\w+) WHERE (.+)$", Pattern.CASE_INSENSITIVE);

    private final Database db;
    private final File folder;
    private final DatabaseStorage storage;

    public CommandParser(Database db, File folder, DatabaseStorage storage) {
        this.db = db;
        this.folder = folder;
        this.storage = storage;
    }

    public Command parse(String sql) {
        sql = sql.trim();
        Matcher m;

        m = CREATE_RE.matcher(sql);
        if (m.matches()) {
            String name = m.group(1);
            String cols = m.group(2);
            List<Column> columns = parseColumns(cols);
            return new CreateTableCommand(name, columns);
        }

        m = INSERT_RE.matcher(sql);
        if (m.matches()) {
            String name = m.group(1);
            List<String> rawValues = splitArgs(m.group(2));
            return new InsertCommand(name, rawValues);
        }

        m = DROP_RE.matcher(sql);
        if (m.matches()) {
            String name = m.group(1);
            return new DropTableCommand(name, folder, storage);
        }

        m = DELETE_RE.matcher(sql);
        if (m.matches()) {
            String name = m.group(1);
            String whereClause = m.group(2);
            Predicate<Row> pred = buildPredicate(name, whereClause);
            return new DeleteCommand(name, pred);
        }

        throw new IllegalArgumentException("Unknown command " + sql);
    }

    private List<Column> parseColumns(String body) {
        String[] parts = body.split(";");
        List<Column> cols = new ArrayList<>();
        for (String part : parts) {
            part = part.trim();
            if (part.isEmpty()) continue;
            String[] tok = part.split("\\s+");
            DataType dt = DataType.valueOf(tok[0].toUpperCase());
            String colName = tok[1];
            boolean unique = Arrays.asList(tok).contains("unique");
            boolean notNull = Arrays.asList(tok).contains("notNull");
            cols.add(new Column(colName, dt, unique, notNull));
        }
        return cols;
    }

    private List<String> splitArgs(String body) {
        List<String> res = new ArrayList<>();
        int depth = 0;
        int start = 0;
        for (int i = 0; i < body.length(); i++) {
            char sym = body.charAt(i);
            if (sym == '[') depth++;
            else if (sym == ']') depth--;
            else if (sym == ',' && depth == 0) {
                res.add(body.substring(start, i).trim());
                start = i + 1;
            }
        }
        res.add(body.substring(start).trim());
        return res;
    }

    private Predicate<Row> buildPredicate(String tableName, String cond) {
        Table table = db.getTable(tableName);
        String[] parts = cond.split("\\s+AND\\s+");
        List<Predicate<Row>> preds = new ArrayList<>();

        for (String part : parts) {
            part = part.trim();
            Matcher m = Pattern.compile("(\\w+)\\s*=\\s*(\"?.+?\"?)").matcher(part);
            if (!m.matches()) {
                throw new IllegalArgumentException("Bad cond: " + part);
            }

            String colName = m.group(1);
            String literal = m.group(2).replaceAll("^\"|\"$", "");
            int idx = -1;
            List<Column> cols = table.getColumns();
            for (int i = 0; i < cols.size(); i++) {
                if (cols.get(i).getName().equals(colName)) {
                    idx = i;
                    break;
                }
            }
            if (idx < 0) {
                throw new IllegalArgumentException("Unknown column: " + colName);
            }

            DataType dt = cols.get(idx).getType();
            Object val = dt.parse(literal);

            int colIdx = idx;
            preds.add(row -> Objects.equals(row.getValues().get(colIdx), val));
        }

        return preds.stream().reduce(x -> true, Predicate::and);
    }
}
