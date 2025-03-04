package io.github.charl11e.sat;
import java.nio.file.*;
import java.util.ArrayList;
import java.io.IOException;

public class DIMACS {

    public static void load (String file) {

        ArrayList<ArrayList> clauses = new ArrayList<>();
        Path filePath = Paths.get("./files", file);

        try {
            String content = Files.readString(filePath);
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };


    public static void main(String[] args) {
        DIMACS.load("testsat.txt");
    }

}
