package io.github.charl11e.sat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DPLL {

    private static final SATResult UNSAT_RESULT = new SATResult (new ArrayList<>(), new HashSet<>());

    // Entry point
    public static SATResult solve (ArrayList<ArrayList<Integer>> clause_set) {
        Set<Integer> all_literals = new HashSet<>();
        for (ArrayList<Integer> clause : clause_set) {
            for (Integer literal : clause) {
                all_literals.add(Math.abs(literal));
            }
        }
        return solve(clause_set, new HashSet<>(), all_literals);
    }

    // Overloaded DPLL Function that stores a list of all the literals
    public static SATResult solve (ArrayList<ArrayList<Integer>> clause_set, Set<Integer> partial_assignment, Set<Integer> all_literals) {
        // Run unit propagation
        SATResult result = callUnitPropagate(clause_set, partial_assignment);
        clause_set = result.getClauseSet();
        partial_assignment = new HashSet<>(result.getAssignment());

        // Run Pure Literal Elimination
        result = callPureLiteralElimination(clause_set, partial_assignment);
        clause_set = result.getClauseSet();
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
        new_partial_assignment.add(literal);
        if (DPLL.solve(new_clause_set, new_partial_assignment, all_literals) != UNSAT_RESULT) {
            return new SATResult(new_clause_set, new_partial_assignment);
        }

        // Add -literal to clause_set and check result
        new_clause_set = new ArrayList<>(clause_set);
        new_partial_assignment = new HashSet<>(partial_assignment);
        new_clause = new ArrayList<>();
        new_clause.add(-literal);
        new_clause_set.add(new_clause);
        new_partial_assignment.add(-literal);
        if (DPLL.solve(new_clause_set, new_partial_assignment, all_literals) != UNSAT_RESULT) {
            return new SATResult(new_clause_set, new_partial_assignment);
        }

        // If neither works, return UNSAT
        return UNSAT_RESULT;
    }

    // Assign remaining literals to true (could equally be false)
    private static void assignRemainingLiterals(Set<Integer> assignment, Set<Integer> all_literals) {
        for (Integer literal : all_literals) {
            if (!assignment.contains(-literal) && !assignment.contains(literal)) {
                assignment.add(literal);
            }
        }
    }

    // Call Unit Propagation & Pure Literal elimination and merging the new assignments with current assignment
    private static SATResult callUnitPropagate (ArrayList<ArrayList<Integer>> clause_set, Set<Integer> assignment) {
        SATResult result = UnitPropagate.propagate(clause_set);
        assignment.addAll(result.getAssignment());
        return new SATResult(result.getClauseSet(), assignment);
    }

    private static SATResult callPureLiteralElimination (ArrayList<ArrayList<Integer>> clause_set, Set<Integer> assignment) {
        SATResult result = PureLiteralElimination.eliminate(clause_set);
        assignment.addAll(result.getAssignment());
        return new SATResult(result.getClauseSet(), assignment);
    }

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> clause_set = DIMACS.load("testsat.txt");
        SATResult result = DPLL.solve(clause_set);
        System.out.println(result.getClauseSet());
        System.out.println(result.getAssignment());
    }

}
