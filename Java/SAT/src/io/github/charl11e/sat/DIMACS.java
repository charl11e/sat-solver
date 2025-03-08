package io.github.charl11e.sat;
import java.nio.file.*;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Provides functionality for loading a file in DIMACS format
 * Each clause must be on a new line, each line must end with a "0" and each literal must be separated by a space
 */
public class DIMACS {

    /**
     * Loads DIMACs file and stores contents as a set of clauses
     * @param file File to be loaded. Must be located in folder named 'files'
     * @return List of clauses stored as an array within an array
     */
    public static ArrayList<ArrayList<Integer>> load (String file) {
        String content = DIMACS.getContents(file);
        return DIMACS.parseFile(content.split("\\R"));
    }

    /**
     * Loads file path and stores content of file in string
     * @param file File to be loaded. Must be located in folder named 'files'
     * @return String with contents of file
     */
    private static String getContents (String file) {
        Path filePath = Paths.get("./files", file);
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Parses a list of lines into an array of arrays of clause sets
     * @param lines Set of lines to be parsed
     * @return List of lists of clause sets
     */
    private static ArrayList<ArrayList<Integer>> parseFile (String[] lines) {
        ArrayList<ArrayList<Integer>> clauses = new ArrayList<>();
        for (String line : lines) {
            // If first character is p or c, skip
            if (line.charAt(0) == 'p' || line.charAt(0) == 'c') {
                continue;
            }

            // Otherwise, split up line by spaces
            String[] parts = line.split("\\s+");
            ArrayList<Integer> clause = new ArrayList<>();

            // Parse into integers
            for (String part : parts) {
                int literal = Integer.parseInt(part);
                if (literal == 0 ) {
                    break;
                }
                clause.add(literal);
            }

            // Add clause into list of clauses
            clauses.add(clause);
        }
        return clauses;
    }
}
