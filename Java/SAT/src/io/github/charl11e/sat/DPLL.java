package io.github.charl11e.sat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Runs the DPLL SAT Solver algorithm on a clause set.
 * Clause set must be provided in CNF format
 */
public class DPLL {

    public static final SATResult UNSAT_RESULT;
    static {
        ArrayList<ArrayList<Integer>> unsat_clause_set = new ArrayList<>();
        unsat_clause_set.add(new ArrayList<>());
        UNSAT_RESULT = new SATResult(unsat_clause_set, new HashSet<>());
    }

    /**
     * Solves a clause set using DPLL algorithm. This is an entry point which saves a list of all the
     * literals to be used later to ensure all literals are assigned a value
     * @param clause_set Clause set to be solved
     * @return SATResult object containing final assignment (if clause set is satisfiable)
     */
    public static SATResult solve (ArrayList<ArrayList<Integer>> clause_set) {
        Set<Integer> all_literals = new HashSet<>();
        for (ArrayList<Integer> clause : clause_set) {
            for (Integer literal : clause) {
                all_literals.add(Math.abs(literal));
            }
        }
        return solve(clause_set, new HashSet<>(), all_literals);
    }

    /**
     * Solves a clause set using DPLL algorithm. Not intended to be used as an entry point as a set of all the literals
     * is saved through recursion to be used later to ensure all literals are assigned a value
     * @param clause_set Clause set to be solved
     * @param partial_assignment Current partial assignment of clause set
     * @param all_literals Set of all literals of original clause set
     * @return SATResult object containing final assignment (if clause set is satisfiable)
     */
    public static SATResult solve (ArrayList<ArrayList<Integer>> clause_set, Set<Integer> partial_assignment, Set<Integer> all_literals) {
        // Run unit propagation
        SATResult result = callUnitPropagate(clause_set, partial_assignment);
        clause_set = new ArrayList<>(result.getClauseSet());
        partial_assignment = new HashSet<>(result.getAssignment());

        // Base cases
        // If clause set is empty, formula is satisfiable, return results
        if (clause_set.isEmpty()) {
            assignRemainingLiterals(partial_assignment, all_literals);
            return new SATResult(clause_set, partial_assignment);
        }

        // If clause contains empty clause, formula is unsatisfiable
        if (clause_set.contains(new ArrayList<Integer>())) {
            return UNSAT_RESULT;
        }

        // Run Pure Literal Elimination
        result = callPureLiteralElimination(clause_set, partial_assignment);
        clause_set = new ArrayList<>(result.getClauseSet());
        partial_assignment = new HashSet<>(result.getAssignment());

        // Base cases
        // If clause set is empty, formula is satisfiable, return results
        if (clause_set.isEmpty()) {
            assignRemainingLiterals(partial_assignment, all_literals);
            return new SATResult(clause_set, partial_assignment);
        }

        // If clause contains empty clause, formula is unsatisfiable
        if (clause_set.contains(new ArrayList<Integer>())) {
            return UNSAT_RESULT;
        }

        // Pick literal to branch on, add literal to clause_set, so it becomes pure and recurse
        Integer literal = clause_set.getFirst().getFirst();

        // Add literal to clause_set and check result
        ArrayList<ArrayList<Integer>> new_clause_set = new ArrayList<>(clause_set);
        Set<Integer> new_partial_assignment = new HashSet<>(partial_assignment);
        ArrayList<Integer> new_clause = new ArrayList<>();
        new_clause.add(literal);
        new_clause_set.add(new_clause);
        SATResult new_result = DPLL.solve(new_clause_set, new_partial_assignment, all_literals);
        if (new_result != UNSAT_RESULT) {
            return new_result;
        }

        // Add -literal to clause_set and check result
        new_clause_set = new ArrayList<>(clause_set);
        new_partial_assignment = new HashSet<>(partial_assignment);
        new_clause = new ArrayList<>();
        new_clause.add(-literal);
        new_clause_set.add(new_clause);
        new_result = DPLL.solve(new_clause_set, new_partial_assignment, all_literals);
        if (new_result != UNSAT_RESULT) {
            return new_result;
        }

        // If neither works, return UNSAT
        return UNSAT_RESULT;
    }

    /**
     * Adds remaining literals that were not given an assignment to the final assignment
     * All remaining literals are assigned to 1, as they do not affect the satisfiability of the clause set
     * @param assignment Final assignment for clause set
     * @param all_literals Set containing all literals from initial clause set
     */
    private static void assignRemainingLiterals(Set<Integer> assignment, Set<Integer> all_literals) {
        for (Integer literal : all_literals) {
            if (!assignment.contains(-literal) && !assignment.contains(literal)) {
                assignment.add(literal);
            }
        }
    }

    /**
     * Runs Unit Propagation on clause set and automatically adds the new assignment to the existing assignment
     * @param clause_set Set of clauses stored as an array within an array
     * @param assignment Current assignment of literals
     * @return SATResult object containing new clause set and assignment
     */
    private static SATResult callUnitPropagate (ArrayList<ArrayList<Integer>> clause_set, Set<Integer> assignment) {
        SATResult result = UnitPropagate.propagate(clause_set);
        assignment.addAll(result.getAssignment());
        return new SATResult(result.getClauseSet(), assignment);
    }

    /**
     * Runs Pure Literal Elimination on clause set and automatically adds the new assignment to the existing assignment
     * @param clause_set Set of clauses stored as an array within an array
     * @param assignment Current assignment of literals
     * @return SATResult object containing new clause set and assignment
     */
    private static SATResult callPureLiteralElimination (ArrayList<ArrayList<Integer>> clause_set, Set<Integer> assignment) {
        SATResult result = PureLiteralElimination.eliminate(clause_set);
        assignment.addAll(result.getAssignment());
        return new SATResult(result.getClauseSet(), assignment);
    }

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> clause_set = DIMACS.load("8queens.txt");
        SATResult result = DPLL.solve(clause_set);
        System.out.println(result.getClauseSet());
        System.out.println(result.getAssignment());
    }

}
