# Version 3.9.6
def load_dimacs(file_name):
    # Make empty list to store clauses
    clauses = []
    file = open(file_name, "r")
    for line in file:
        # Check if first character is p or c, if so skip
        if (line[0] == "p") or (line[0] == "c"):
            continue
        # Otherwise, add clause to list
        else:
            clause = line.split()
            # Remove 0 from end of clause
            clause.pop()
            # Convert to integers
            for literal in range(len(clause)):
                clause[literal] = int(clause[literal])
            # Add clause to list
            clauses.append(clause)
    # Close file and return list of clauses
    file.close()
    return clauses

def simple_sat_solve(clause_set):
    # Check what literals are in the clause set
    literals = []
    for clauses in clause_set:
        for literal in clauses:
            if abs(literal) not in literals:
                literals.append(abs(literal))

    # Start checking all possible assignments
    assignment = [0]*len(literals)
    for i in range(2**len(literals)):
        clausessat = 0
        # Check if current assignment is valid
        for clauses in clause_set:
            clause_satisfied = False
            # Check if single clause is satisfied
            for literal in clauses:
                # Find the index of the literal in the clause
                index = literals.index(abs(literal))
                # Check if single literal is satisfied
                if (literal > 0 and assignment[index] == 1) or (literal < 0 and assignment[index] == 0):
                    clause_satisfied = True
                    break
            # If clause is not satisfied, break the loop
            if not clause_satisfied:
                break
            # If clause is satisfied, add 1 to clausessat
            clausessat += 1
        # If all clauses are satisfied, return assignment
            if clausessat == len(clause_set):
                for literal in range(len(assignment)):
                    if assignment[literal] == 0:
                        assignment[literal] = -literals[literal]
                    else:
                        assignment[literal] = literals[literal]
                return assignment

        # If not, increment assignment
        for literal in range(len(assignment)):
            if assignment[literal] == 0:
                assignment[literal] = 1
                break
            else:
                assignment[literal] = 0

    # If no assignment is found, return False
    return False

def branching_sat_solve(clause_set,partial_assignment):
    # Create a list of all literals in the clause set
    all_literals = []
    for clause in clause_set:
        for literal in clause:
            if abs(literal) not in all_literals:
                all_literals.append(abs(literal))

    # Start by checking if the partial assignment satisfies any clauses in clause set (if partial assignment is not empty)
    unsatisfied_clauses = []
    for clause in clause_set:
        new_clause = []
        for literal in clause:
            if literal in partial_assignment:
                break
            elif -literal in partial_assignment:
                continue
            else:
                new_clause.append(literal)
        # If the new clause is empty, the partial assignment is not valid
        else:
            if new_clause != []:
                unsatisfied_clauses.append(new_clause)
            else:
                return False

    # If all clauses are satisfied, return the partial assignment
    if unsatisfied_clauses == []:
        return partial_assignment
    
    # If not, start branching
    # Heuristic for choosing literal to branch on:
    # 1. Each literal is assigned a 'point' for each clause it appears in
    # 2. Literals get extra points if they are in shorter closes (worked out by 1/len(clause))
    literals = {}
    for clauses in unsatisfied_clauses:
        for literal in clauses:
            if abs(literal) not in literals:
                literals[abs(literal)] = 1
            else:
                literals[abs(literal)] += 1
            literals[abs(literal)] += 1/len(clauses)
    
    # Put all literals into an array, with the most common literals first
    literals = sorted(literals, key=literals.get, reverse=True)
    
    # Start branching on all literals
    for literal in literals:
        # Try positive literal
        partial_assignment.append(literal)
        sat = branching_sat_solve(unsatisfied_clauses, partial_assignment)
        if sat:
            # Check if all literals are in the partial assignment
            for literal in all_literals:
                if literal not in sat and -literal not in sat:
                    # Assign the literal to true (but could equally be false)
                    sat.append(literal)
            return sat
        partial_assignment.pop()
        
        # Try negative literal
        partial_assignment.append(-literal)
        sat = branching_sat_solve(unsatisfied_clauses, partial_assignment)
        if sat:
            # Check if all literals are in the partial assignment
            for literal in all_literals:
                if literal not in sat and -literal not in sat:
                    # Assign the literal to true (but could equally be false)
                    sat.append(literal)
            return sat

        # If neither positive or negative literal works, return False
        partial_assignment.pop()
        return False
        
    # If no assignment is found, return False
    return False

def unit_propagate(clause_set):
    assignment = {}

    while True:
        new_clause_set = []
        # Find all unit clauses:
        for clause in clause_set:
            if len(clause) == 1:
                # SPECIAL CASE: If a unit literal and it's unit negation are in the clause set, return false as the clause set is unsatisfiable
                if -clause[0] in assignment.values():
                    return False
                assignment[abs(clause[0])] = clause[0]
            else:
                new_clause_set.append(clause)
        
        # If there are no unit clauses, return the clause set
        if new_clause_set == clause_set:
            return new_clause_set
        
        # If there are unit clauses, remove all clauses containing the unit literal, and remove -literal from all other clauses
        clause_set = []
        for clause in new_clause_set:
            append = True
            new_clause = []
            # If the clause contains -literal, remove it, if it contains neither literal or -literal, add it to the new clause
            for literal in clause:
                if abs(literal) not in assignment:
                    new_clause.append(literal)
                elif assignment[abs(literal)] == literal:
                    append = False
                    break
            if append == True:
                clause_set.append(new_clause)
  
def dpll_sat_solve(clause_set,partial_assignment):
    # Start by getting a list of all literals in the clause set
    all_literals = []
    for clause in clause_set:
        for literal in clause:
            if abs(literal) not in all_literals:
                all_literals.append(abs(literal))
    
    # Start with unit propagation (the unit propagation function is not used in this case as this version directly modifies the partial assignment)
    # This also updates the clause set during recursive calls too
    while True:
        # Update the clause set for partial assignment
        new_clause_set = []
        for clause in clause_set:
            new_clause = []
            for literal in clause:
                if literal in partial_assignment:
                    break
                elif -literal in partial_assignment:
                    continue
                else:
                    new_clause.append(literal)
            # If the new clause is empty, the partial assignment is not valid
            else:
                if new_clause != []:
                    new_clause_set.append(new_clause)
                else:
                    return False

        # Unit propagate the new clause set
        clause_set = []
        for clause in new_clause_set:
            if len(clause) == 1:
                # SPECIAL CASE: If a unit literal and it's unit negation are in the clause set, return false as the clause set is unsatisfiable
                if -clause[0] in partial_assignment:
                    return False
                partial_assignment.append(clause[0])
            else:
                clause_set.append(clause)
        # If there are no unit clauses, return the clause set
        if new_clause_set == clause_set:
            break

    # If all clauses are satisfied, return the partial assignment
    if clause_set == []:
        # Check if all literals are in the partial assignment
        for literal in all_literals:
            if literal not in partial_assignment and -literal not in partial_assignment:
                # Assign the literal to true (but could equally be false)
                partial_assignment.append(literal)
        return list(set(partial_assignment))
    
    # If not start branching
    # Heuristic for choosing literal to branch on:
    # 1. Each literal is assigned a 'point' for each clause it appears in
    # 2. Literals get extra points if they are in shorter closes (worked out by 1/len(clause))
    literals = {}
    for clauses in clause_set:
        for literal in clauses:
            if abs(literal) not in literals:
                literals[abs(literal)] = 1
            else:
                literals[abs(literal)] += 1
            literals[abs(literal)] += 1/len(clauses)
    
    # Put all literals into an array, with the most common literals first
    literals = sorted(literals, key=literals.get, reverse=True)
    # Start branching on all literals
    for literal in literals:
        # Try positive literal
        partial_assignment.append(literal)
        sat = dpll_sat_solve(clause_set, partial_assignment + [literal])
        if sat:
            return sat
        partial_assignment.pop()

        # Try negative literal
        partial_assignment.append(-literal)
        sat = dpll_sat_solve(clause_set, partial_assignment + [-literal])
        if sat:
            return sat
        
        # If neither positive or negative literal works, return False
        partial_assignment.pop()
        return False
    
    # If no assignment is found, return False
    return False