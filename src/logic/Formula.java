package logic;

import java.util.ArrayList;

/**
 *
 * @author alyacarina
 */
public class Formula {

    public ArrayList<Prop> variables;
    private final Formula[] inputs;
    private final Connective connective;

    public boolean isProp(){
        return (variables.size() == 1) && 
                (inputs.length == 0) && 
                (connective == null);
    }
    
    // Constructor
    public Formula(Formula[] inputs, Connective connective) {
        this.inputs = inputs;
        this.connective = connective;

        variables = new ArrayList<>();

        for (Formula input : inputs) {
            for (Prop p1 : input.variables) {
                boolean add = true;
                for (Prop p2 : variables) {
                    if (p1 == p2) {
                        add = false;
                        break;
                    } else if (p1.getName().equals(p2.getName())) {
                        add = false;
                        input.replaceInputBy(new Formula(p1), new Formula(p2));
                    }
                }
                if (add) {
                    variables.add(p1);
                }
            }
        }

    }

    public Formula(Formula f) {
        connective = f.connective;
        inputs = new Formula[f.inputs.length];
        for (int i = 0; i < f.inputs.length; i++) {
            inputs[i] = new Formula(f.inputs[i]);
        }

        variables = new ArrayList<>();
        for (Formula input : inputs) {
            for (Prop p1 : input.variables) {
                boolean add = true;
                for (Prop p2 : variables) {
                    if (p1 == p2) {
                        add = false;
                        break;
                    } else if (p1.getName().equals(p2.getName())) {
                        add = false;
                        input.replaceInputBy(new Formula(p1), new Formula(p2));
                        break;
                    }
                }
                if (add) {
                    variables.add(p1);
                }
            }
        }
    }

// Atomic constructor
    public Formula(Prop p) {
        this.inputs = new Formula[0];
        this.connective = null;
        variables = new ArrayList<>();
        variables.add(p);
    }

    // Evaluating a formula...
    public boolean evaluate() {
        if (isProp()) {
            return variables.get(0).getValue();
        }
        return connective.evaluate(inputs);
    }

    // Replace all instances of current in other
    public Formula replaceInputBy(Formula current, Formula other) {
        if (Formula.equivalent(this, current)) {
            return other;
        } else if (!isProp()) {
            Formula x = new Formula(this);
            for (int i = 0; i < x.inputs.length; i++) {
                x.inputs[i] = x.inputs[i].replaceInputBy(current, other);
            }

            ArrayList<Prop> x_variables = new ArrayList<>();

            for (Formula input : x.inputs) {
                for (Prop p1 : input.variables) {
                    boolean add = true;
                    for (Prop p2 : x_variables) {
                        if (p1 == p2) {
                            add = false;
                            break;
                        } else if (p1.getName().equals(p2.getName())) {
                            add = false;
                            input.replaceInputBy(new Formula(p1), new Formula(p2));
                            break;
                        }
                    }
                    if (add) {
                        x_variables.add(p1);
                    }
                }
            }

            x.variables = x_variables;

            return x;
        } else {
            return this;
        }
    }

    // evaluate formula
    public boolean evaluate(boolean[] values) {
        int s = variables.size();
        for (int i = 0; i < s; i++) {
            variables.get(i).setValue(values[i]);
        }
        if (isProp()) {
            return variables.get(0).getValue();
        }
        return connective.evaluate(inputs);
    }

    public boolean evaluate(Prop[] values) {
        int s = variables.size();
        for (int i = 0; i < s; i++) {
            variables.get(i).setValue(values[i].getValue());
        }
        if (isProp()) {
            return variables.get(0).getValue();
        }
        return connective.evaluate(inputs);
    }

    // Returns a truth table for a given formula
    // The last column represents the value of the formula
    // The rest of the rows represent the value of each variable
    // for each valuation
    // in the order in which they were placed in the array list.
    public boolean[][] generateTruthTable() {
        int rows = (int) Math.pow(2, variables.size());
        int cols = variables.size() + 1;

        boolean[][] table = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols - 1; j++) {
                if ((int) (i / (Math.pow(2, j))) % 2 == 0) {
                    table[i][j] = false;
                } else {
                    table[i][j] = true;
                }

                variables.get(j).setValue(table[i][j]);
            }
            table[i][cols - 1] = evaluate();
        }

        return table;
    }

    // Returns an array of variable names
    public String[] getVariableNamesInOrder() {
        String[] names = new String[variables.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = variables.get(i).getName();
        }
        return names;
    }

    // Get number of variables
    public int getNumVariables() {
        return variables.size();
    }

    // Matches two formula's variable pools- if no match, 
    //    return null, otherwise returns corresponding vertices of one and other's
    //    equivalent variables
    private static int[] shareVariables(Formula f1, Formula f2) {
        if (f1 == null || f2 == null) {
            return new int[0];
        }

        if (f1.getNumVariables() != f2.getNumVariables()) {
            return null;
        }

        String[] v1 = f1.getVariableNamesInOrder();
        String[] v2 = f2.getVariableNamesInOrder();

        int[] corr_indices = new int[v1.length];

        // props must have the same name-
        // prop names are their unique identifiers
        // checks to see if prop lists match up
        for (int i = 0; i < v1.length; i++) {
            boolean has_prop = false;
            for (int j = 0; j < v2.length; j++) {
                if (v2[j].equals(v1[i])) {
                    has_prop = true;
                    corr_indices[i] = j;
                    break;
                }
            }
            if (!has_prop) {
                return null;
            }
        }

        return corr_indices;
    }

    // Valuation comparison of two formulas, to see if they are equivalent
    // Works under assumption that they derive their values from the same set of
    // Props
    public static boolean equivalent(Formula f1, Formula f2) {
        if (f1 == null && f2 == null) {
            return true;
        } else if (f1 == null || f2 == null) {
            return false;
        }

        int[] corr_indices;
        if ((corr_indices = shareVariables(f1, f2)) == null) {
            return false;
        }

        // checks that for the same prop valuation,
        //   the two formulas evaluate to true
        int num_vals = (int) Math.pow(2, f1.getNumVariables());

        for (int i = 0; i < num_vals; i++) {
            boolean[] valuation1 = new boolean[f1.getNumVariables()];
            boolean[] valuation2 = new boolean[f2.getNumVariables()];
            for (int j = 0; j < f1.getNumVariables(); j++) {
                if ((int) (i / (Math.pow(2, j))) % 2 == 0) {
                    valuation1[j] = false;
                    valuation2[corr_indices[j]] = false;
                } else {
                    valuation1[j] = true;
                    valuation2[corr_indices[j]] = true;
                }
                if (f1.evaluate(valuation1) != f2.evaluate(valuation2)) {
                    return false;
                }
            }
        }

        return true;
    }

    // returns if this formula is a tautology
    public boolean isValid() {
        boolean[][] table = generateTruthTable();

        for (int i = 0; i < table[0].length; i++) {
            if (!table[i][table[0].length - 1]) {
                return false;
            }
        }

        return true;
    }

    // returns if this formula can have a valuation to be true
    public boolean isSatisfiable() {
        boolean[][] table = generateTruthTable();

        for (int i = 0; i < table[0].length; i++) {
            if (table[i][table[0].length - 1]) {
                return true;
            }
        }

        return false;
    }

    // returns if this formula is unsatisfiable (false for all valuations)
    public boolean isUnsatisfiable() {
        return !isSatisfiable();
    }

    @Override
    public String toString() {
        String output = "";

        if (connective == null && variables.size() > 0) {
            output = variables.get(0).toString();
        } else if (connective != null) {
            output = "( " + connective.toString();
            for (Formula f : inputs) {
                output += " " + f.toString();
            }
            output += " )";
        }

        return output;
    }
}
