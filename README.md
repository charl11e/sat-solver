# SAT Solver
# By Charlie Livesey-Shorrock

This was originally created as a university assignment which was to create a SAT Solver in Python (original code can be found in 'original.py')

This is my first Java project, outside of small projects just to learn the fundamentals of the language, so porting over the code allowed me to get an experience with building a full java project. Furthermore, there is an additional project (a sudoku solver), which uses the logic from this SAT Solver, found at: https://github.com/charl11e/sudoku

This is a quick rundown of the boolean satisfiability problem, and DPLL SAT Solvers (paraphrased from the respective wikipedia articles):
- Propositional Logic includes variables (e.g. x, y, z), operators (OR, NOT, AND), and clauses (e.g. x OR y)
- A literal is defined as either being a variable or the negation (-) of a variable (e.g. x or -x).
- A formula is in CNF (Conjunctive normal form), if it is a conjunction (AND) of disjunctions (ORs of literals)
- Every propositional logic formula can be converted to CNF
- The goal of the boolean satisfiability problem is to determine if there is an assignment of literals that that satisfies a given formula
- If such an assignment exists, the formula is satisfiable (SAT), otherwise it is unsatisfiable (UNSAT)

There are two main rules used in DPLL SAT Solvers:
- Unit Propagation: If a clause contains only one literal, it must be true. All clauses containing the literal are satisfied, and -x is removed from the rest
- Pure Literal Elimination: If a literal occurs with only one polarity, it can be set to true to satisfy all clauses containing it

The DPLL algorithm is a recursive algorithm for SAT Solving:
- Base Case: If the clause set is empty, the formula is satisfiable. If a clause is empty, it is unsatisfiable.
1) Apply Unit Propagation and Pure Literal Elimination
2) Pick an unassigned variable and recursively assign it true and false
3) If one branch leads to SAT, return SAT (and maybe the assignment). Otherwise, return UNSAT

My DPLL algorithm implements both of these rules. In the Java edition, the latest version of the solver is located in: Java/SAT/SAT.jar, and the docs are located in: docs

More details can be found about the boolean satisfiability problem here: https://en.wikipedia.org/wiki/Boolean_satisfiability_problem

More details can be found about DPLL SAT Solvers here: https://en.wikipedia.org/wiki/DPLL_algorithm
