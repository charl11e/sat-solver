package io.github.charl11e.sat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UnitPropagate {

    public static SATResult propagate (ArrayList<ArrayList<Integer>> clause_set) {

        ArrayList<Integer> assignment = new ArrayList<>();
        boolean changed;

        // Find all unit clauses
        while (true) {
            changed = false;
            ArrayList<ArrayList<Integer>> new_clause_set = new ArrayList<>();

            for (ArrayList<Integer> clause : clause_set) {

                // Special case: If there is an empty clause, formula is unsatisfiable
                if (clause.isEmpty()) {
                    ArrayList<ArrayList<Integer>> unsat = new ArrayList<>();
                    unsat.add(new ArrayList<>());
                    return new SATResult(unsat, assignment);
                }

                if (clause.size() == 1) {
                    int unitLiteral = clause.getFirst();

                    // Special case: If unit literal and unit negation in same clause set, return an empty clause within clause set (as original expression is unsatisfiable)
                    if (assignment.contains(-unitLiteral)) {
                        ArrayList<ArrayList<Integer>> unsat = new ArrayList<>();
                        unsat.add(new ArrayList<>());
                        return new SATResult(unsat, assignment);
                    }

                    if (!assignment.contains(unitLiteral)) {
                        assignment.add(unitLiteral);
                        changed = true;
                    }

                } else {
                    new_clause_set.add(new ArrayList<>(clause));
                }
            }

            // If there are no unit clauses, return the clause set
            Set<Set<Integer>> set1 = new HashSet<>();
            Set<Set<Integer>> set2 = new HashSet<>();
            for (ArrayList<Integer> clause : clause_set) {
                set1.add(new HashSet<> (clause));
            }
            for (ArrayList<Integer> clause : new_clause_set) {
                set2.add(new HashSet<> (clause));
            }

            if (set1.equals(set2)) {
                return new SATResult(new_clause_set, assignment);
            }

            // If there are unit clauses, remove all clauses containing the unit literal, and remove -literal from all other clauses
            ArrayList<ArrayList<Integer>> processed_clauses = new ArrayList<>();

            for (ArrayList<Integer> clause : new_clause_set) {
                boolean sat = false;
                ArrayList<Integer> modified_clause = new ArrayList<>();

                // Remove clauses containing literal
                for (int literal : clause) {
                    if (assignment.contains(literal)) {
                        sat = true;
                        break;
                    }

                    // Remove -literal from clause if clause contains it
                    if (!assignment.contains(-literal)) {
                        modified_clause.add(literal);
                    }
                }

                // Add back modified clause
                if (!sat && !modified_clause.isEmpty()) {
                    processed_clauses.add(modified_clause);
                }
            }

            clause_set = processed_clauses;

            if (!changed) {
                return new SATResult(clause_set, assignment);
            }

        }

    }
}
