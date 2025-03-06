package io.github.charl11e.sat;
import java.util.ArrayList;

public class SATResult {
    private final ArrayList<ArrayList<Integer>> clause_set;
    private final ArrayList<Integer> assignment;

    public SATResult(ArrayList<ArrayList<Integer>> clause_set, ArrayList<Integer> assignment) {
        this.clause_set = clause_set;
        this.assignment = assignment;
    }

    public ArrayList<ArrayList<Integer>> getClauseSet() {
        return clause_set;
    }

    public ArrayList<Integer> getAssignment() {
        return assignment;
    }


}
