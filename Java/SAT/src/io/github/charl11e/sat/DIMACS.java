package io.github.charl11e.sat;
import java.nio.file.*;
import java.util.ArrayList;
import java.io.IOException;

public class DIMACS {

    public static ArrayList<ArrayList<Integer>> load (String file) {
        // Declare ArrayList to store list of lists of clauses
        ArrayList<ArrayList<Integer>> clauses = new ArrayList<>();

        // Load file path and get contents of file into String
        Path filePath = Paths.get("./files", file);
        String content;
        try {
            content = Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return clauses;
        }

        // Split String by lines
        String[] lines = content.split("\\R");

        // Parse individual clauses from file into clauses
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
