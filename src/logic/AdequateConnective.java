
package logic;

/**
 *
 * @author alyacarina
 */
public class AdequateConnective extends Connective {
    // Constructor
    public AdequateConnective() {
        super(2);
    }

    // False unless both inputs are false
    @Override
    public boolean evaluate(Formula[] inputs) {
        if(inputs.length != this.getNumPortals())
            throw new UnsupportedOperationException(Connective.INVALID_NUM_INPUTS);
        
        boolean zero = inputs[0].evaluate();
        boolean one = inputs[1].evaluate();
        return (zero == false) && (one == zero);
    }
}
