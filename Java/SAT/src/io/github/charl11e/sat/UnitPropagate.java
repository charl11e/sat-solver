package io.github.charl11e.sat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides functionality for running Unit Propagation on a clause set.
 * Clause set must be provided in CNF format
 */
public class UnitPropagate {

    /**
     * Run Unit Propagation on a clause set
     * @param clause_set Set of clauses stored as an array within an array
     * @return SATResult object containing a new clause set and assignments after Unit Propagation
     */
    public static SATResult propagate (ArrayList<ArrayList<Integer>> clause_set) {

        Set<Integer> assignment = new HashSet<>();
        boolean changed;

        // Find all unit clauses
        while (true) {
            changed = false;
            ArrayList<ArrayList<Integer>> new_clause_set = new ArrayList<>();

            for (ArrayList<Integer> clause : clause_set) {

                // Special case: If there is an empty clause, formula is unsatisfiable
                if (clause.isEmpty()) {
                    return DPLL.UNSAT_RESULT;
                }

                if (clause.size() == 1) {
                    Integer unitLiteral = clause.getFirst();

                    // Special case: If unit literal and unit negation in same clause set, formula is unsatisfiable
                    if (assignment.contains(-unitLiteral)) {
                        return DPLL.UNSAT_RESULT;
                    }

                    if (!assignment.contains(unitLiteral)) {
                        assignment.add(unitLiteral);
                        changed = true;
                    }

                } else {
                    new_clause_set.add(clause);
                }
            }

            ArrayList<ArrayList<Integer>> processed_clauses = removeUnitLiterals(new_clause_set, assignment);

            if (!changed) {
                return new SATResult(processed_clauses, assignment);
            }

            clause_set = processed_clauses;

        }
    }

    /**
     * Remove clauses containing unit literal from clause set, and remove -literal from all other clauses
     * @param new_clause_set Set of clauses stored as an array within an array
     * @param assignment Current assignment of literals
     * @return SATResult object containing a new clause set and assignment
     */
    private static ArrayList<ArrayList<Integer>> removeUnitLiterals(ArrayList<ArrayList<Integer>> new_clause_set, Set<Integer> assignment) {
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
            if (!sat) {
                processed_clauses.add(modified_clause);
            }
        }
        return processed_clauses;
    }


}
