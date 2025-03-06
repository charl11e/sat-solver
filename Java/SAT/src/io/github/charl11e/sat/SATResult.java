package io.github.charl11e.sat;
import java.util.ArrayList;
import java.util.Set;

public class SATResult {
    private final ArrayList<ArrayList<Integer>> clause_set;
    private final Set<Integer> assignment;

    public SATResult(ArrayList<ArrayList<Integer>> clause_set, Set<Integer> assignment) {
        this.clause_set = clause_set;
        this.assignment = assignment;
    }

    public ArrayList<ArrayList<Integer>> getClauseSet() {
        return clause_set;
    }

    public Set<Integer> getAssignment() {
        return assignment;
    }


}
