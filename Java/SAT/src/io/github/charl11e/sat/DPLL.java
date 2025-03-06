package io.github.charl11e.sat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DPLL {

    public static SATResult solve (ArrayList<ArrayList<Integer>> clause_set) {
        Set<Integer> assignment = new HashSet<>();

        // Base cases
        // If clause set is empty, solution has been found
        if (clause_set.isEmpty()) {
            return new SATResult(clause_set, assignment);
        }

        // If clause set contains empty clause, current branch/clause set is unsatisfiable
        if (clause_set.contains(new ArrayList<Integer>())) {

        }
    }

    // Methods for calling Unit Propagation & Pure Literal elimination and merging the new assignments with current assignment
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
