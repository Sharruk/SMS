package sms.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading from and writing to plain .txt files.
 *
 * Demonstrates FILE HANDLING using:
 *   - BufferedReader / FileReader  for reading
 *   - BufferedWriter / FileWriter  for writing
 *
 * All methods are static — no need to create an object of this class.
 */
public class FileHandler {

    /**
     * Read all non-empty lines from a file.
     * Returns an empty list if the file does not exist yet.
     */
    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            return lines; // file not created yet — return empty list
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file '" + filePath + "': " + e.getMessage());
        }

        return lines;
    }

    /**
     * Write a list of lines to a file (overwrites the existing content).
     * Creates parent directories if they don't exist.
     */
    public static void writeLines(String filePath, List<String> lines) {
        // Create the data/ directory if it doesn't exist
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file '" + filePath + "': " + e.getMessage());
        }
    }

    /**
     * Append a single line to an existing file without overwriting.
     * Useful for adding new records quickly.
     */
    public static void appendLine(String filePath, String line) {
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error appending to file '" + filePath + "': " + e.getMessage());
        }
    }
}
