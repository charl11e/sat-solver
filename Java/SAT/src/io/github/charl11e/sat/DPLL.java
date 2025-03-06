package io.github.charl11e.sat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DPLL {

    public static SATResult solve (ArrayList<ArrayList<Integer>> clause_set) {
        Set<Integer> assignment = new HashSet<>();

        return null;
    }

    // Methods for calling Unit Propagation & Pure Literal elimination and merging the new assignments with current assignment
    private static SATResult callUnitPropagate (ArrayList<ArrayList<Integer>> clause_set, HashSet<Integer> assignment) {
        SATResult result = UnitPropagate.propagate(clause_set);
        Set<Integer> new_assignment = new HashSet<>();
        return new SATResult();
    }

}
