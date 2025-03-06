package io.github.charl11e.sat;
import java.util.ArrayList;

public class PureLiteralElimination {

    public static SATResult eliminate (ArrayList<ArrayList<Integer>> clause_set) {
        ArrayList<Integer> pure_literals = new ArrayList<>();
        ArrayList<Integer> not_pure_literals = new ArrayList<>();

        for (ArrayList<Integer> clause : clause_set ) {

            for (Integer literal : clause) {

                // Check if literal is already detected to not be a pure literal
                if (not_pure_literals.contains(literal)) {
                    continue;
                }

                // Check if negation of literal is in pure literals
                int index = pure_literals.indexOf(-literal);
                if (index != -1) {
                    pure_literals.remove(index);
                    not_pure_literals.add(-literal);
                    not_pure_literals.add(literal);
                    continue;
                }

                // Otherwise, add to list of pure literals
                if (!pure_literals.contains(literal)) {
                    pure_literals.add(literal);
                }
            }
        }

        // Add all pure literals to the assignment
        ArrayList<Integer> assignment = new ArrayList<>(pure_literals);

        // Remove all clauses containing pure literal
        ArrayList<ArrayList<Integer>> new_clause_set = new ArrayList<>();
        for (Integer literal : pure_literals) {
            for (ArrayList<Integer> clause : clause_set) {
                if (clause.contains(literal)) {
                    break;
                }
                new_clause_set.add(clause);
            }
        }

        return new SATResult(new_clause_set, assignment);


    }

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> clause_set = DIMACS.load("testsat.txt");
        SATResult result = PureLiteralElimination.eliminate(clause_set);
        System.out.println(result.getAssignment());
        System.out.println(result.getClauseSet());
    }
}
