package io.github.charl11e.sat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Proves functionality for running Pure Literal Elimination on a clause set.
 * Clause set must be provided in CNF format
 */
public class PureLiteralElimination {

    /**
     * Run Pure Literal Elimination on a clause set
     * @param clause_set Set of clauses stored as an array within an array
     * @return SATResult object containing the new clause set and assignments after running Pure Literal Elimination
     */
    public static SATResult eliminate (ArrayList<ArrayList<Integer>> clause_set) {
        Set<Integer> pure_literals = PureLiteralElimination.getPureLiterals(clause_set);
        Set<Integer> assignment = new HashSet<>(pure_literals);
        ArrayList<ArrayList<Integer>> new_clause_set = PureLiteralElimination.removeClauses(clause_set, pure_literals);
        return new SATResult(new_clause_set, assignment);
    }

    /**
     * Gets the set of pure literals from a clause set
     * @param clause_set Set of clauses stored as an array within an array
     * @return Set of pure literals from clause set
     */
    private static Set<Integer> getPureLiterals(ArrayList<ArrayList<Integer>> clause_set) {
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

        return pure_literals;
    }

    /**
     * Removes clauses containing pure literals from clause set
     * @param clause_set Set of clauses stored as an array within an array
     * @param pure_literals Set of pure literals from the clause set
     * @return New clause set after the clauses containing the pure literal have been removed
     */
    private static ArrayList<ArrayList<Integer>> removeClauses(ArrayList<ArrayList<Integer>> clause_set, Set<Integer> pure_literals) {
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
        return new_clause_set;
    }
}