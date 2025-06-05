package com.example.db.storage;

import com.example.db.engine.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.util.*;

public class DatabaseStorage {
    private final ObjectMapper mapper;

    public DatabaseStorage() {
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void load(Database db, File mainDbFile) {
        File dir = mainDbFile.getParentFile();
        if (dir == null) dir = new File(".");

        File[] files = dir.listFiles((d, name) ->
                name.endsWith(".db") && !new File(d, name).equals(mainDbFile));

        if (files == null) return;

        for (File file : files) {
            try {
                Table table = mapper.readValue(file, Table.class);
                db.createTable(table.getNameTable(), table.getColumns());
                for (Row row : table.getRows()) {
                    db.getTable(table.getNameTable()).insert(row);
                }
                System.out.println("load table: " + table.getNameTable());
            } catch (IOException e) {
                System.err.println("error " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    public void save(Database db, File outputDir) {
        if (outputDir == null) outputDir = new File(".");

        for (String tableName : db.listTableName()) {
            Table table = db.getTable(tableName);
            File file = new File(outputDir, tableName + ".db");

            try (FileWriter writer = new FileWriter(file)) {
                mapper.writeValue(writer, table);
                System.out.println("save table: " + tableName);
            } catch (IOException e) {
                System.err.println("not saved " + tableName + ": " + e.getMessage());
            }
        }
    }

    public void deleteTableFile(File folder, String tableName) {
        File tableFile = new File(folder, tableName + ".db");
        if (tableFile.exists()) {
            boolean deleted = tableFile.delete();
            if (!deleted) {
                System.err.println("Could not delete file: " + tableFile.getName());
            } else {
                System.out.println("Deleted file: " + tableFile.getName());
            }
        }
    }
}
