package io.github.charl11e.sat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PureLiteralElimination {

    public static SATResult eliminate (ArrayList<ArrayList<Integer>> clause_set) {
        Set<Integer> pure_literals = new HashSet<>();
        Set<Integer> not_pure_literals = new HashSet<>();

        for (ArrayList<Integer> clause : clause_set ) {
            for (Integer literal : clause) {

                // Check if literal is already detected to not be a pure literal
                if (not_pure_literals.contains(literal)) {
                    continue;
                }

                // Check if negation of literal is in pure literals
                if (pure_literals.contains(-literal)) {
                    pure_literals.remove(-literal);
                    not_pure_literals.add(-literal);
                    not_pure_literals.add(literal);
                }

                else {
                    pure_literals.add(literal);
                }
            }
        }

        // Add all pure literals to the assignment
        ArrayList<Integer> assignment = new ArrayList<>(pure_literals);

        // Remove all clauses containing pure literal
        ArrayList<ArrayList<Integer>> new_clause_set = new ArrayList<>();
        for (ArrayList<Integer> clause : clause_set) {
            boolean add_clause = true;
            for (Integer literal : pure_literals) {
                if (clause.contains(literal)) {
                    add_clause = false;
                    break;
                }
            }

            if (add_clause) {
                new_clause_set.add(new ArrayList<>(clause));
            }
        }

        return new SATResult(new_clause_set, assignment);


    }

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> clause_set = DIMACS.load("testsat.txt");
        SATResult result = UnitPropagate.propagate(clause_set);
        System.out.println(result.getAssignment());
        System.out.println(result.getClauseSet());
    }
}