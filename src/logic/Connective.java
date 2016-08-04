
package logic;

/**
 *
 * @author alyacarina
 */
public abstract class Connective {
    // Number of Inputs to a Connective
    private int num_portals;
    private final String INVALID_NUM_PORTALS = "Invalid number or portals."
            + " Must be a Math Nat.";
    
    // Get number of inputs
    public int getNumPortals(){
        return num_portals;
    }
    
    // Constructor:
    public Connective(int num_portals){
        if(num_portals<=0) throw new IllegalStateException(INVALID_NUM_PORTALS);
        this.num_portals = num_portals;
    }
    
    public static final String INVALID_NUM_INPUTS = "Number of inputs must match"
            + " number of portals";
    
    // Function to evaluate value of some inputs, must be overridden
    public abstract boolean evaluate(Formula[] inputs);
    
    @Override
    public String toString(){
        return "|";
    }
}
