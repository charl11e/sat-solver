package io.github.charl11e.sat;
import java.util.ArrayList;
import java.util.HashSet;

public class SATResult {
    private final ArrayList<ArrayList<Integer>> clause_set;
    private final HashSet<Integer> assignment;

    public SATResult(ArrayList<ArrayList<Integer>> clause_set, HashSet<Integer> assignment) {
        this.clause_set = clause_set;
        this.assignment = assignment;
    }

    public ArrayList<ArrayList<Integer>> getClauseSet() {
        return clause_set;
    }

    public HashSet<Integer> getAssignment() {
        return assignment;
    }


}
